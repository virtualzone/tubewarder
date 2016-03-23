package net.weweave.tubewarder.service.request;

import net.weweave.tubewarder.service.model.ConfigItemModel;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class SetConfigRequest extends AbstractRestRequest {
    public List<ConfigItemModel> items = new ArrayList<>();
}
