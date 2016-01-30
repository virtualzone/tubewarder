package net.weweave.tubewarder.service.model;

import net.weweave.tubewarder.outputhandler.api.IOutputHandler;
import net.weweave.tubewarder.outputhandler.api.OutputHandler;
import net.weweave.tubewarder.outputhandler.api.configoption.OutputHandlerConfigOption;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class OutputHandlerModel {
    public String id;
    public String name;
    public List<OutputHandlerConfigOptionModel> configOptions = new ArrayList<>();

    public static OutputHandlerModel factory(IOutputHandler handler) {
        OutputHandlerModel model = new OutputHandlerModel();
        OutputHandler annotation = handler.getClass().getAnnotation(OutputHandler.class);
        model.id = annotation.id();
        model.name = annotation.name();
        List<OutputHandlerConfigOption> options = handler.getConfigOptions();
        for (OutputHandlerConfigOption configOption : options) {
            model.configOptions.add(OutputHandlerConfigOptionModel.factory(configOption));
        }
        return model;
    }
}
