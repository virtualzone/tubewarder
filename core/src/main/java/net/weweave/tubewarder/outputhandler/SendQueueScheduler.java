package net.weweave.tubewarder.outputhandler;

import net.weweave.tubewarder.dao.ConfigItemDao;
import net.weweave.tubewarder.dao.LogDao;
import net.weweave.tubewarder.dao.SendQueueItemDao;
import net.weweave.tubewarder.domain.Log;
import net.weweave.tubewarder.domain.QueueItemStatus;
import net.weweave.tubewarder.domain.SendQueueItem;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.util.ConfigManager;
import net.weweave.tubewarder.util.SystemIdentifier;

import javax.ejb.*;
import javax.inject.Inject;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
public class SendQueueScheduler {
    private static final Logger LOG = Logger.getLogger(SendQueueScheduler.class.getName());

    private final Queue<Long> sendQueue;
    private final Queue<Long> backupQueue;
    private final SendQueueCallback callbackHandler = new SendQueueCallback() {
        @Override
        public void success(SendQueueItem item) {
            try {
                log(item, "Successfully processed, deleting queue item");
                updateLog(item, QueueItemStatus.SUCCESS);
                getSendQueueItemDao().delete(item);
            } finally {
                decrementCurrentThreads();
            }
        }

        @Override
        public void temporaryError(SendQueueItem item) {
            try {
                if (item.getTryCount() < getMaxRetries()) {
                    log(item, "Scheduling for retry " + (item.getTryCount()+1) + "/" + getMaxRetries());
                    updateSendQueueItemForRetry(item);
                    updateLog(item, QueueItemStatus.RETRY);
                } else {
                    log(item, "Too many retries, giving up and deleting queue item");
                    updateLog(item, QueueItemStatus.FAILED);
                    getSendQueueItemDao().delete(item);
                }
            } finally {
                decrementCurrentThreads();
            }
        }

        @Override
        public void permanentError(SendQueueItem item) {
            try {
                log(item, "Permanent error, giving up and deleting queue item");
                updateLog(item, QueueItemStatus.FAILED);
                getSendQueueItemDao().delete(item);
            } finally {
                decrementCurrentThreads();
            }
        }
    };

    private boolean schedulerActive;
    private int currentThreads;

    @Inject
    private SendQueueItemDao sendQueueItemDao;

    @Inject
    private OutputHandlerDispatcher dispatcher;

    @Inject
    private ConfigItemDao configItemDao;

    @Inject
    private LogDao logDao;

    public SendQueueScheduler() {
        sendQueue = new ConcurrentLinkedQueue<>();
        backupQueue = new ConcurrentLinkedQueue<>();
        schedulerActive = false;
        currentThreads = 0;
    }

    public void recover() {
        LOG.info("Recovering unprocessed items...");
        getSendQueueItemDao().recoverUnprocessedItems(SystemIdentifier.getIdentifier());
        sendQueue.addAll(getSendQueueItemDao().getUnprocessedItemIds(SystemIdentifier.getIdentifier()));
    }

    @Schedule(minute = "*", hour = "*", second = "*", persistent = false)
    public void scheduledProcessQueue() {
        if (schedulerActive) {
            return;
        }
        try {
            schedulerActive = true;
            processQueue();
        } finally {
            schedulerActive = false;
        }
    }

    @Schedule(minute = "*", hour = "*", second = "*", persistent = false)
    private void scheduledProcessBackupQueue() {
        Long id;
        do {
            id = backupQueue.poll();
            if (id != null) {
                addSendQueueItem(id);
            }
        } while (id != null);
    }

    @Schedule(minute = "*", hour = "*", second = "*/30", persistent = false)
    public void scheduledReQueueItems() {
        List<Long> ids = getSendQueueItemDao().getFailedUnqueuedItemIds(SystemIdentifier.getIdentifier(), getRetryWaitTimeSeconds());
        for (Long id : ids) {
            if (!sendQueue.contains(id)) {
                sendQueue.add(id);
            }
        }
    }

    public void addSendQueueItem(Long itemId) {
        this.sendQueue.add(itemId);
    }

    private void processQueue() {
        SendQueueItem item;
        int maxConcurrentThreads = getMaxConcurrentThreads();
        do {
            item = getNextSendQueueItem();
            if (item != null) {
                incrementCurrentThreads();
                updateSendQueueItemSetProcessing(item);
                getDispatcher().processSendQueueItem(item, callbackHandler);
            }
        } while ((item != null) && (getCurrentThreads() < maxConcurrentThreads));
    }

    public int getCurrentThreads() {
        return currentThreads;
    }

    public int getNumItemsInQueue() {
        return sendQueue.size();
    }

    private void incrementCurrentThreads() {
        addCurrentThreads(+1);
    }

    private void decrementCurrentThreads() {
        addCurrentThreads(-1);
    }

    private synchronized void addCurrentThreads(int diff) {
        currentThreads = currentThreads + diff;
    }

    private int getMaxConcurrentThreads() {
        return getConfigItemDao().getInt(ConfigManager.CONFIG_MAX_CONCURRENT_THREADS, 10);
    }

    private int getMaxRetries() {
        return getConfigItemDao().getInt(ConfigManager.CONFIG_MAX_RETRIES, 5);
    }

    private int getRetryWaitTimeSeconds() {
        return getConfigItemDao().getInt(ConfigManager.CONFIG_RETRY_WAIT_TIME_SECONDS, 5*60);
    }

    private SendQueueItem getNextSendQueueItem() {
        SendQueueItem item = null;
        Long id = sendQueue.poll();
        if (id != null) {
            try {
                item = getSendQueueItemDao().get(id);
            } catch (ObjectNotFoundException e) {
                LOG.warning("Could not read queue item from database (id = "+id+"), moving to backup queue");
                backupQueue.add(id);
                item = null;
            }
        }
        return item;
    }

    private void updateSendQueueItemSetProcessing(SendQueueItem item) {
        getSendQueueItemDao().setItemInProcessing(item);
    }

    private void updateSendQueueItemForRetry(SendQueueItem item) {
        getSendQueueItemDao().setItemNextTry(item);
    }

    private void updateLog(SendQueueItem sendQueueItem, QueueItemStatus status) {
        Log log = sendQueueItem.getLog();
        log.setStatus(status);
        getLogDao().update(log);
    }

    private void log(SendQueueItem item, String message) {
        LOG.info("Queue item ID = " + item.getExposableId() + ": " + message);
    }

    public SendQueueItemDao getSendQueueItemDao() {
        return sendQueueItemDao;
    }

    public void setSendQueueItemDao(SendQueueItemDao sendQueueItemDao) {
        this.sendQueueItemDao = sendQueueItemDao;
    }

    public OutputHandlerDispatcher getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(OutputHandlerDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public ConfigItemDao getConfigItemDao() {
        return configItemDao;
    }

    public void setConfigItemDao(ConfigItemDao configItemDao) {
        this.configItemDao = configItemDao;
    }

    public LogDao getLogDao() {
        return logDao;
    }

    public void setLogDao(LogDao logDao) {
        this.logDao = logDao;
    }
}
