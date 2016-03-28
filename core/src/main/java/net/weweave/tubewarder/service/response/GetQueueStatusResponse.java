package net.weweave.tubewarder.service.response;

import net.weweave.tubewarder.service.model.QueueStatusModel;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GetQueueStatusResponse extends AbstractResponse {
    public QueueStatusModel status = new QueueStatusModel();
}
