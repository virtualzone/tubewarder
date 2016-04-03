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
        return firstAlive;
    }

    public void setFirstAlive(Date firstAlive) {
        this.firstAlive = firstAlive;
    }

    public Date getLastAlive() {
        return lastAlive;
    }

    public void setLastAlive(Date lastAlive) {
        this.lastAlive = lastAlive;
    }
}
