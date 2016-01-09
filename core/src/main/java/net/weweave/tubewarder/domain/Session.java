package net.weweave.tubewarder.domain;

import javax.persistence.*;
import java.io.Serializable;
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
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public Date getLastActionDate() {
        return lastActionDate;
    }

    public void setLastActionDate(Date lastActionDate) {
        this.lastActionDate = lastActionDate;
    }
}
