package eio.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class ChannelTemplate extends AbstractPersistentObject {
    @ManyToOne
    private Template template;
    @ManyToOne
    private Channel channel;
    private String content;

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
