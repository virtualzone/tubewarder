package net.weweave.tubewarder.outputhandler;

import net.weweave.tubewarder.domain.SendQueueItem;

public interface SendQueueCallback {
    void success(SendQueueItem item);
    void temporaryError(SendQueueItem item);
    void permanentError(SendQueueItem item);
}
