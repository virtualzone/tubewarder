package tubewarder.domain;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Template extends AbstractPersistentObject {
    private String name;
    @OneToMany(mappedBy = "template")
    private List<ChannelTemplate> channelTemplates;

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
}
