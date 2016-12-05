package net.weweave.tubewarder.service.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.validator.GenericValidator;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.util.HashMap;

@XmlRootElement
public class SoapSendModel extends SendModel {
    public String modelJson;

    @Override
    public JsonNode getModelAsJsonNode() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        if (GenericValidator.isBlankOrNull(this.modelJson)) {
            return mapper.convertValue(new HashMap<>(), JsonNode.class);
        } else {
            return mapper.readValue(this.modelJson, JsonNode.class);
        }
    }
}
