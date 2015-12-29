package tubewarder.service.request;

import tubewarder.service.model.AbstractRestModel;

public abstract class AbstractSetObjectRestRequest<T extends AbstractRestModel> extends AbstractRestRequest {
    public T object;
}
