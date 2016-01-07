package net.weweave.tubewarder.domain;

import javax.persistence.*;

@Entity
public class Channel extends AbstractPersistentObject {
    @Column(unique = true)
    private String name;
    @Enumerated(EnumType.STRING)
    private OutputHandler outputHandler;
    @ManyToOne
    private AbstractOutputHandlerConfiguration config;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OutputHandler getOutputHandler() {
        return outputHandler;
    }

    public void setOutputHandler(OutputHandler outputHandler) {
        this.outputHandler = outputHandler;
    }

    public AbstractOutputHandlerConfiguration getConfig() {
        return config;
    }

    public void setConfig(AbstractOutputHandlerConfiguration config) {
        this.config = config;
    }
}
