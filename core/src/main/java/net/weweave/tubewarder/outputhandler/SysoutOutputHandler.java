package net.weweave.tubewarder.outputhandler;

import net.weweave.tubewarder.outputhandler.api.*;
import net.weweave.tubewarder.outputhandler.api.configoption.OutputHandlerConfigOption;
import net.weweave.tubewarder.outputhandler.api.configoption.StringConfigOption;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@OutputHandler(id="SYSOUT", name="Console")
public class SysoutOutputHandler implements IOutputHandler {
    private static final Logger LOG = Logger.getLogger(SysoutOutputHandler.class.getName());

    @Override
    public void process(Config config, Address sender, Address recipient, String subject, String content, List<Attachment> attachments) {
        String prefix = (String)config.getOrDefault("prefix", "");
        String suffix = (String)config.getOrDefault("suffix", "");
        LOG.info(prefix + subject + " /// " + content + suffix);
    }

    @Override
    public List<OutputHandlerConfigOption> getConfigOptions() {
        List<OutputHandlerConfigOption> options = new ArrayList<>();
        options.add(new StringConfigOption("prefix", "Prefix", false, ""));
        options.add(new StringConfigOption("suffix", "Suffix", false, ""));
        return options;
    }

    @Override
    public void checkConfig(Config config) throws InvalidConfigException {

    }

    @Override
    public void checkRecipientAddress(Address address) throws InvalidAddessException {

    }
}
