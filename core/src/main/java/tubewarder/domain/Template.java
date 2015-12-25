package tubewarder.domain;

import javax.persistence.Entity;

@Entity
public class Template extends AbstractPersistentObject {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
