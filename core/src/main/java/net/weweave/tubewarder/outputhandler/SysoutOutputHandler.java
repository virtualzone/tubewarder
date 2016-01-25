package net.weweave.tubewarder.outputhandler;

import net.weweave.tubewarder.outputhandler.config.OutputHandlerConfigOption;
import net.weweave.tubewarder.outputhandler.config.StringConfigOption;
import net.weweave.tubewarder.service.model.AttachmentModel;
import net.weweave.tubewarder.util.Address;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SysoutOutputHandler extends OutputHandler {
    public static final String ID = "SYSOUT";

    public SysoutOutputHandler(Map<String, Object> config) {
        super(config);
    }

    @Override
    public String getId() {
        return "sysout";
    }

    @Override
    public String getName() {
        return "Console";
    }

    @Override
    public void process(Address sender, Address recipient, String subject, String content, List<AttachmentModel> attachments) {
        Map<String, Object> config = getConfig();
        String prefix = (String)config.getOrDefault("prefix", "");
        String suffix = (String)config.getOrDefault("suffix", "");
        System.out.println(prefix + subject + " /// " + content + suffix);
    }

    @Override
    public List<OutputHandlerConfigOption> getConfigOptions() {
        List<OutputHandlerConfigOption> options = new ArrayList<>();
        options.add(new StringConfigOption("prefix", "Prefix", false, ""));
        options.add(new StringConfigOption("suffix", "Suffix", false, ""));
        return options;
    }
}
