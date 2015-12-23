package eio.domain;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class AbstractPersistentObject implements IdObject, ExposableId, Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true)
    private String exposableId;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getExposableId() {
        return exposableId;
    }

    @Override
    public void setExposableId(String exposableId) {
        this.exposableId = exposableId;
    }

    @Override
    public int hashCode() {
        if (this.getId() != null) {
            return this.getId().hashCode();
        } else {
            return super.hashCode();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || !(obj instanceof AbstractPersistentObject)) {
            return false;
        }

        AbstractPersistentObject other = (AbstractPersistentObject)obj;

        if (this.getId() == null) {
            return false;
        }

        return this.getId().equals(other.getId());
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[id="+this.getId()+"]";
    }
}
