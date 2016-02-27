package net.weweave.tubewarder.service.response;

import net.weweave.tubewarder.service.model.LogModel;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class GetLogResponse extends AbstractResponse {
    public List<LogModel> logs = new ArrayList<>();
}
