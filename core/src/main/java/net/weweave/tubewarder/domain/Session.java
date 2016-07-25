package net.weweave.tubewarder.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Session extends AbstractPersistentObject {
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    private Date loginDate;
    private Date lastActionDate;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getLoginDate() {
        return (loginDate == null ? null : new Date(loginDate.getTime()));
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = (loginDate == null ? null : new Date(loginDate.getTime()));
    }

    public Date getLastActionDate() {
        return (lastActionDate == null ? null : new Date(lastActionDate.getTime()));
    }

    public void setLastActionDate(Date lastActionDate) {
        this.lastActionDate = (lastActionDate == null ? null : new Date(lastActionDate.getTime()));
    }
}
