package net.weweave.tubewarder.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class SendQueueItem extends AbstractPersistentObject {
    @ManyToOne
    private ChannelTemplate channelTemplate;
    private String recipientAddress;
    private String recipientName;
    private String subject;
    @Column(length = 100000)
    private String content;
    @OneToMany(mappedBy = "sendQueueItem", fetch = FetchType.EAGER)
    private List<Attachment> attachments = new ArrayList<>();
    private String keyword;
    @Column(length = 100000)
    private String details;
    @Column(length = 100000)
    private String configJson;
    private Date createDate = null;
    private Date lastTryDate = null;
    private Integer tryCount = 0;
    private Boolean inProcessing = false;

    public ChannelTemplate getChannelTemplate() {
        return channelTemplate;
    }

    public void setChannelTemplate(ChannelTemplate channelTemplate) {
        this.channelTemplate = channelTemplate;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
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

    public String getConfigJson() {
        return configJson;
    }

    public void setConfigJson(String configJson) {
        this.configJson = configJson;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastTryDate() {
        return lastTryDate;
    }

    public void setLastTryDate(Date lastTryDate) {
        this.lastTryDate = lastTryDate;
    }

    public Integer getTryCount() {
        return tryCount;
    }

    public void setTryCount(Integer tryCount) {
        this.tryCount = tryCount;
    }

    public Boolean getInProcessing() {
        return inProcessing;
    }

    public void setInProcessing(Boolean inProcessing) {
        this.inProcessing = inProcessing;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }
}
