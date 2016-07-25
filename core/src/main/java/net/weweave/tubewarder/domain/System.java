package net.weweave.tubewarder.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Entity
public class System extends AbstractPersistentObject {
    @Column(unique = true)
    private Integer systemId;
    private Date firstAlive;
    private Date lastAlive;

    public Integer getSystemId() {
        return systemId;
    }

    public void setSystemId(Integer systemId) {
        this.systemId = systemId;
    }

    public Date getFirstAlive() {
        return (firstAlive == null ? null : new Date(lastAlive.getTime()));
    }

    public void setFirstAlive(Date firstAlive) {
        this.firstAlive = (firstAlive == null ? null : new Date(firstAlive.getTime()));
    }

    public Date getLastAlive() {
        return (lastAlive == null ? null : new Date(lastAlive.getTime()));
    }

    public void setLastAlive(Date lastAlive) {
        this.lastAlive = (lastAlive == null ? null : new Date(lastAlive.getTime()));
    }
}
