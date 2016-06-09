package net.weweave.tubewarder.service.response;

import net.weweave.tubewarder.service.model.ErrorCode;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@XmlRootElement
public class AbstractResponse {
    public Integer error = ErrorCode.OK;
    public Map<String, ArrayList<Integer>> fieldErrors = new HashMap<>();

    public void addFieldError(String field, Integer error) {
        if (!fieldErrors.containsKey(field)) {
            fieldErrors.put(field, new ArrayList<>());
        }
        fieldErrors.get(field).add(error);
    }
}
