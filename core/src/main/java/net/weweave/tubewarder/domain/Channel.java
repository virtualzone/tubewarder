package net.weweave.tubewarder.domain;

import javax.persistence.*;

@Entity
public class Channel extends AbstractPersistentObject {
    @Column(unique = true)
    private String name;
    private String rewriteRecipientName;
    private String rewriteRecipientAddress;
    private String rewriteSubject;
    @Column(length = 100000)
    private String rewriteContent;
    @Column(length = 100000)
    private String configJson;
    @ManyToOne
    private UserGroup userGroup;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRewriteRecipientName() {
        return rewriteRecipientName;
    }

    public void setRewriteRecipientName(String rewriteRecipientName) {
        this.rewriteRecipientName = rewriteRecipientName;
    }

    public String getRewriteRecipientAddress() {
        return rewriteRecipientAddress;
    }

    public void setRewriteRecipientAddress(String rewriteRecipientAddress) {
        this.rewriteRecipientAddress = rewriteRecipientAddress;
    }

    public String getRewriteSubject() {
        return rewriteSubject;
    }

    public void setRewriteSubject(String rewriteSubject) {
        this.rewriteSubject = rewriteSubject;
    }

    public String getRewriteContent() {
        return rewriteContent;
    }

    public void setRewriteContent(String rewriteContent) {
        this.rewriteContent = rewriteContent;
    }

    public String getConfigJson() {
        return configJson;
    }

    public void setConfigJson(String configJson) {
        this.configJson = configJson;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }
}
