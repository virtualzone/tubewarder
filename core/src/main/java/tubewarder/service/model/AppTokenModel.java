package tubewarder.service.model;

import tubewarder.domain.AppToken;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AppTokenModel extends AbstractRestModel {
    public String name;

    public static AppTokenModel factory(AppToken appToken) {
        AppTokenModel model = new AppTokenModel();
        model.id = appToken.getExposableId();
        model.name = appToken.getName();
        return model;
    }
}
