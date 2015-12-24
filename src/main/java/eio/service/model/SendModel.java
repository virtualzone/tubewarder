package eio.service.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

@XmlRootElement
public class SendModel {
    public String template;
    public String channel;
    public Map<String, Object> model;
}
