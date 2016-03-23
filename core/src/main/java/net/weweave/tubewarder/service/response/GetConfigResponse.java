package net.weweave.tubewarder.service.response;

import net.weweave.tubewarder.service.model.ConfigItemModel;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class GetConfigResponse extends AbstractResponse {
    public List<ConfigItemModel> items = new ArrayList<>();
}
