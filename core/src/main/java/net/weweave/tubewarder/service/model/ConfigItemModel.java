package net.weweave.tubewarder.service.model;

import net.weweave.tubewarder.domain.ConfigItem;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ConfigItemModel {
    public String key;
    public String type;
    public String value;
    public String label;

    public static ConfigItemModel factory(ConfigItem item) {
        ConfigItemModel model = new ConfigItemModel();
        model.key = item.getKey();
        model.type = item.getType().toString();
        model.value = item.getValue();
        model.label = item.getLabel();
        return model;
    }
}
