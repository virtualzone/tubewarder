package net.weweave.tubewarder.domain;

public enum QueueItemStatus {
    WAITING,
    PROCESSING,
    RETRY,
    FAILED,
    SUCCESS
}
