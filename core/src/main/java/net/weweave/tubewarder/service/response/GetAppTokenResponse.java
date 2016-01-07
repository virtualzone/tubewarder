package net.weweave.tubewarder.service.response;

import net.weweave.tubewarder.service.model.AppTokenModel;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class GetAppTokenResponse extends AbstractResponse {
    public List<AppTokenModel> tokens = new ArrayList<>();
}
