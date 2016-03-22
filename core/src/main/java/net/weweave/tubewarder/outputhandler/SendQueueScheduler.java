package net.weweave.tubewarder.outputhandler;

import net.weweave.tubewarder.dao.SendQueueItemDao;
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

    private final Queue<Long> sendQueue;
    private final SendQueueCallback callbackHandler = new SendQueueCallback() {
        @Override
        public void success(SendQueueItem item) {
            try {
                getSendQueueItemDao().delete(item);
            } finally {
                decrementCurrentThreads();
            }
        }

        @Override
        public void temporaryError(SendQueueItem item) {
            try {
                if (item.getTryCount() < getMaxRetries()) {
                    item.setTryCount(item.getTryCount() + 1);
                    item.setLastTryDate(new Date());
                    item.setInProcessing(false);
                    getSendQueueItemDao().update(item);
                } else {
                    getSendQueueItemDao().delete(item);
                }
            } finally {
                decrementCurrentThreads();
            }
        }

        @Override
        public void permanentError(SendQueueItem item) {
            try {
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


    public SendQueueScheduler() {
        sendQueue = new ConcurrentLinkedQueue<>();
        schedulerActive = false;
        currentThreads = 0;
    }

    @PostConstruct
    public void init() {
        LOG.info("Recovering unprocessed items...");
        getSendQueueItemDao().recoverUnprocessedItems();
        sendQueue.addAll(getSendQueueItemDao().getUnprocessedItemIds());
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
        SendQueueItem item = null;
        int maxConcurrentThreads = getMaxConcurrentThreads();
        do {
            item = getNextSendQueueItem();
            if (item != null) {
                incrementCurrentThreads();
                item.setInProcessing(true);
                getSendQueueItemDao().update(item);
                getDispatcher().processSendQueueItem(item, callbackHandler);
            }
        } while ((item != null) && (getCurrentThreads() < maxConcurrentThreads));
    }

    private int getCurrentThreads() {
        return currentThreads;
    }

    private synchronized void incrementCurrentThreads() {
        currentThreads++;
    }

    private synchronized void decrementCurrentThreads() {
        currentThreads--;
    }

    private int getMaxConcurrentThreads() {
        // TODO Make this configurable
        return 10;
    }

    private int getMaxRetries() {
        // TODO Make this configurable
        return 5;
    }

    private int getRetryWaitTimeSeconds() {
        // TODO Make this configurable
        return 5*60;
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
}
