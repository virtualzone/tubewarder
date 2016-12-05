package net.weweave.tubewarder.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class SendQueueItem extends AbstractPersistentObject {
    @ManyToOne
    private ChannelTemplate channelTemplate;
    private String recipientAddress;
    private String recipientName;
    private String subject;
    @Column(length = 100000)
    private String content;
    private String keyword;
    @Column(length = 100000)
    private String details;
    @Column(length = 100000)
    private String configJson;
    @ManyToOne
    private Log log;
    private Date createDate = null;
    private Date lastTryDate = null;
    private Integer tryCount = 0;
    private Boolean inProcessing = false;
    private Integer systemId;

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
        return (createDate == null ? null : new Date(createDate.getTime()));
    }

    public void setCreateDate(Date createDate) {
        this.createDate = (createDate == null ? null : new Date(createDate.getTime()));
    }

    public Date getLastTryDate() {
        return (lastTryDate == null ? null : new Date(lastTryDate.getTime()));
    }

    public void setLastTryDate(Date lastTryDate) {
        this.lastTryDate = (lastTryDate == null ? null : new Date(lastTryDate.getTime()));
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

    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public Integer getSystemId() {
        return systemId;
    }

    public void setSystemId(Integer systemId) {
        this.systemId = systemId;
    }
}
