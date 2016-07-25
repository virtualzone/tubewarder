package net.weweave.tubewarder.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UserGroup extends AbstractPersistentObject {
    @Column(unique = true)
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> members = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }
}
