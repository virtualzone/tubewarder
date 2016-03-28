package net.weweave.tubewarder.outputhandler;

import net.weweave.tubewarder.dao.ConfigItemDao;
import net.weweave.tubewarder.dao.LogDao;
import net.weweave.tubewarder.dao.SendQueueItemDao;
import net.weweave.tubewarder.domain.Log;
import net.weweave.tubewarder.domain.QueueItemStatus;
import net.weweave.tubewarder.domain.SendQueueItem;
import net.weweave.tubewarder.exception.ObjectNotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

@Singleton
@Startup
public class SendQueueScheduler {
    private static final Logger LOG = Logger.getLogger(SendQueueScheduler.class.getName());
    private static final String CONFIG_MAX_CONCURRENT_THREADS = "QUEUE_MAX_CONCURRENT_THREADS";
    private static final String CONFIG_MAX_RETRIES = "QUEUE_MAX_RETRIES";
    private static final String CONFIG_RETRY_WAIT_TIME_SECONDS = "QUEUE_RETRY_WAIT_TIME_SECONDS";

    private final Queue<Long> sendQueue;
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
                    updateSendQueueItemForRetry(item);
                    log(item, "Scheduling for retry " + item.getTryCount() + "/" + getMaxRetries());
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
        schedulerActive = false;
        currentThreads = 0;
    }

    @PostConstruct
    public void init() {
        checkCreateConfig();
        LOG.info("Recovering unprocessed items...");
        getSendQueueItemDao().recoverUnprocessedItems();
        sendQueue.addAll(getSendQueueItemDao().getUnprocessedItemIds());
    }

    private void checkCreateConfig() {
        if (!getConfigItemDao().hasKey(CONFIG_MAX_CONCURRENT_THREADS)) {
            getConfigItemDao().setValue(CONFIG_MAX_CONCURRENT_THREADS, 10, "Max. concurrent threads");
        }
        if (!getConfigItemDao().hasKey(CONFIG_MAX_RETRIES)) {
            getConfigItemDao().setValue(CONFIG_MAX_RETRIES, 5, "Max. retries");
        }
        if (!getConfigItemDao().hasKey(CONFIG_RETRY_WAIT_TIME_SECONDS)) {
            getConfigItemDao().setValue(CONFIG_RETRY_WAIT_TIME_SECONDS, 5*60, "Retry wait time (seconds)");
        }
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

    @Schedule(minute = "*", hour = "*", second = "*/30", persistent = false)
    public void scheduledReQueueItems() {
        List<Long> ids = getSendQueueItemDao().getFailedUnqueuedItemIds(getRetryWaitTimeSeconds());
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

    private synchronized void incrementCurrentThreads() {
        currentThreads++;
    }

    private synchronized void decrementCurrentThreads() {
        currentThreads--;
    }

    private int getMaxConcurrentThreads() {
        return getConfigItemDao().getInt(CONFIG_MAX_CONCURRENT_THREADS, 10);
    }

    private int getMaxRetries() {
        return getConfigItemDao().getInt(CONFIG_MAX_RETRIES, 5);
    }

    private int getRetryWaitTimeSeconds() {
        return getConfigItemDao().getInt(CONFIG_RETRY_WAIT_TIME_SECONDS, 5*60);
    }

    private SendQueueItem getNextSendQueueItem() {
        SendQueueItem item = null;
        Long id = sendQueue.poll();
        if (id != null) {
            try {
                item = getSendQueueItemDao().get(id);
            } catch (ObjectNotFoundException e) {
                item = null;
            }
        }
        return item;
    }

    private void updateSendQueueItemSetProcessing(SendQueueItem item) {
        updateLog(item, QueueItemStatus.PROCESSING);
        item.setInProcessing(true);
        getSendQueueItemDao().update(item);
    }

    private void updateSendQueueItemForRetry(SendQueueItem item) {
        item.setTryCount(item.getTryCount() + 1);
        item.setLastTryDate(new Date());
        item.setInProcessing(false);
        getSendQueueItemDao().update(item);
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
