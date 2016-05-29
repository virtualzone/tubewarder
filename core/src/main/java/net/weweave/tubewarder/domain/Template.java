package net.weweave.tubewarder.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Template extends AbstractPersistentObject {
    @Column(unique = true)
    private String name;
    @OneToMany(mappedBy = "template", fetch = FetchType.EAGER)
    private List<ChannelTemplate> channelTemplates;
    @ManyToOne
    private UserGroup userGroup;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChannelTemplate> getChannelTemplates() {
        return channelTemplates;
    }

    public void setChannelTemplates(List<ChannelTemplate> channelTemplates) {
        this.channelTemplates = channelTemplates;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }
}
