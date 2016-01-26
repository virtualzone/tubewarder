package net.weweave.tubewarder.service.model;

import net.weweave.tubewarder.outputhandler.OutputHandler;
import net.weweave.tubewarder.outputhandler.config.OutputHandlerConfigOption;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class OutputHandlerModel {
    public String id;
    public String name;
    public List<OutputHandlerConfigOptionModel> configOptions = new ArrayList<>();

    public static OutputHandlerModel factory(OutputHandler handler) {
        OutputHandlerModel model = new OutputHandlerModel();
        model.id = handler.getId();
        model.name = handler.getName();
        for (OutputHandlerConfigOption configOption : handler.getConfigOptions()) {
            model.configOptions.add(OutputHandlerConfigOptionModel.factory(configOption));
        }
        return model;
    }
}
