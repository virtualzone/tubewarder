package net.weweave.tubewarder.service.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@XmlRootElement
public class RestSendModel extends SendModel {
    public Map<String, Object> model;

    @Override
    public JsonNode getModelAsJsonNode() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        if (this.model == null) {
            return mapper.convertValue(new HashMap<>(), JsonNode.class);
        } else {
            return mapper.convertValue(this.model, JsonNode.class);
        }
    }
}
