package net.weweave.tubewarder.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Entity
public class Log extends AbstractPersistentObject {
    private Date date;
    private String appToken;
    private String appTokenName;
    private String keyword;
    private String details;
    private String templateName;
    private String templateId;
    private Long templateIdInt;
    private String channelName;
    private String channelId;
    private Long channelIdInt;
    private String senderName;
    private String senderAddress;
    private String recipientName;
    private String recipientAddress;
    private String subject;
    @Column(length = 100000)
    private String content;
    @Enumerated(EnumType.STRING)
    private QueueItemStatus status;
    private String queueId;


    public Date getDate() {
        return (date == null ? null : new Date(date.getTime()));
    }

    public void setDate(Date date) {
        this.date = (date == null ? null : new Date(date.getTime()));
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    public String getAppTokenName() {
        return appTokenName;
    }

    public void setAppTokenName(String appTokenName) {
        this.appTokenName = appTokenName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public QueueItemStatus getStatus() {
        return status;
    }

    public void setStatus(QueueItemStatus status) {
        this.status = status;
    }

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }

    public Long getTemplateIdInt() {
        return templateIdInt;
    }

    public void setTemplateIdInt(Long templateIdInt) {
        this.templateIdInt = templateIdInt;
    }

    public Long getChannelIdInt() {
        return channelIdInt;
    }

    public void setChannelIdInt(Long channelIdInt) {
        this.channelIdInt = channelIdInt;
    }
}
