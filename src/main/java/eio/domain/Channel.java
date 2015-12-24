package eio.domain;

import javax.persistence.Entity;

@Entity
public class Channel extends AbstractPersistentObject {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
