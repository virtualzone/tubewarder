package net.weweave.tubewarder.outputhandler;

import net.weweave.tubewarder.outputhandler.api.*;
import net.weweave.tubewarder.outputhandler.api.configoption.OutputHandlerConfigOption;

import java.util.ArrayList;
import java.util.List;

@OutputHandler(id="NULL", name="Null (no action)")
public class NullOutputHandler implements IOutputHandler {
    @Override
    public void process(Config config, SendItem item) {
        // Do nothing
    }

    @Override
    public List<OutputHandlerConfigOption> getConfigOptions() {
        return new ArrayList<>();
    }

    @Override
    public void checkConfig(Config config) throws InvalidConfigException {
        // Do nothing
    }

    @Override
    public void checkRecipientAddress(Address address) throws InvalidAddessException {
        // Do nothing
    }
}
