package net.weweave.tubewarder.outputhandler.api;

import net.weweave.tubewarder.outputhandler.api.configoption.OutputHandlerConfigOption;

import java.util.List;

public interface IOutputHandler {
    void process(Config config, Address sender, Address recipient, String subject, String content, List<Attachment> attachments);

    List<OutputHandlerConfigOption> getConfigOptions();

    void checkConfig(Config config) throws InvalidConfigException;

    void checkRecipientAddress(Address address) throws InvalidAddessException;
}
