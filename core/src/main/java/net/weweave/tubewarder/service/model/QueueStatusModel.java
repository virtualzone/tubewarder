package net.weweave.tubewarder.service.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class QueueStatusModel {
    public Integer inDatabase;
    public Integer inQueue;
    public Integer currentThreads;
    public Integer retry;
}
