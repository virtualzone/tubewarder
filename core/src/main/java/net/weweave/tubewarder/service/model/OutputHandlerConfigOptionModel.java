package net.weweave.tubewarder.service.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import net.weweave.tubewarder.outputhandler.config.OutputHandlerConfigOption;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

@XmlRootElement
public class OutputHandlerConfigOptionModel {
    public String type;
    public String id;
    public String label;
    public boolean required;
    protected Map<String,Object> other = new HashMap<>();

    @JsonAnyGetter
    public Map<String,Object> any() {
        return other;
    }

    public static OutputHandlerConfigOptionModel factory(OutputHandlerConfigOption option) {
        OutputHandlerConfigOptionModel model = new OutputHandlerConfigOptionModel();
        model.type = option.getType();
        model.id = option.getId();
        model.label = option.getLabel();
        model.required = option.isRequired();
        model.other = option.getAdditionalParameters();
        return model;
    }
}
