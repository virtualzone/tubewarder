package net.weweave.tubewarder.outputhandler;

import net.weweave.tubewarder.outputhandler.config.OutputHandlerConfigOption;
import net.weweave.tubewarder.util.Address;
import net.weweave.tubewarder.service.model.AttachmentModel;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class OutputHandler {
    private Map<String, Object> config;

    public OutputHandler(Map<String, Object> config) {
        this.config = config;
    }

    public abstract String getId();
    public abstract String getName();
    public abstract List<OutputHandlerConfigOption> getConfigOptions();
    public abstract void process(Address sender, Address recipient, String subject, String content, List<AttachmentModel> attachments);

    public Map<String, Object> getConfig() {
        return config;
    }
}
