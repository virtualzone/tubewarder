package tubewarder.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@Entity
public class Channel extends AbstractPersistentObject {
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
