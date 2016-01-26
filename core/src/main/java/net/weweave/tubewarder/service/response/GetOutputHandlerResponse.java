package net.weweave.tubewarder.service.response;

import net.weweave.tubewarder.service.model.OutputHandlerModel;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class GetOutputHandlerResponse extends AbstractResponse {
    public List<OutputHandlerModel> outputHandlers = new ArrayList<>();
}
