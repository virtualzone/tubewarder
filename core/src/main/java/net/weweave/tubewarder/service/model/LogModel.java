package net.weweave.tubewarder.service.model;

import net.weweave.tubewarder.domain.Log;
import net.weweave.tubewarder.util.DateTimeFormat;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LogModel extends AbstractRestModel {
    public String date;
    public String appToken;
    public String appTokenName;
    public String keyword;
    public String details;
    public String templateName;
    public String templateId;
    public String channelName;
    public String channelId;
    public String senderName;
    public String senderAddress;
    public String recipientName;
    public String recipientAddress;
    public String subject;
    public String content;
    public String queueId;
    public String status;

    public static LogModel factory(Log log, boolean includePayload) {
        LogModel model = new LogModel();
        model.id = log.getExposableId();
        model.date = DateTimeFormat.format(log.getDate());
        model.appToken = log.getAppToken();
        model.appTokenName = log.getAppTokenName();
        model.keyword = log.getKeyword();
        model.templateName = log.getTemplateName();
        model.templateId = log.getTemplateId();
        model.channelName = log.getChannelName();
        model.channelId = log.getChannelId();
        model.senderName = log.getSenderName();
        model.senderAddress = log.getSenderAddress();
        model.recipientName = log.getRecipientName();
        model.recipientAddress = log.getRecipientAddress();
        model.queueId = log.getQueueId();
        model.status = (log.getStatus() != null ? log.getStatus().toString() : "");
        if (includePayload) {
            model.subject = log.getSubject();
            model.content = log.getContent();
            model.details = log.getDetails();
        }
        return model;
    }
}
