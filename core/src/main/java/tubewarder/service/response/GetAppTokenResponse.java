package tubewarder.service.response;

import tubewarder.service.model.AppTokenModel;
import tubewarder.service.model.ChannelModel;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class GetAppTokenResponse extends AbstractResponse {
    public List<AppTokenModel> tokens = new ArrayList<>();
}
