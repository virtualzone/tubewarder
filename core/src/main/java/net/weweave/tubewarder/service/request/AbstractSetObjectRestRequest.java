package net.weweave.tubewarder.service.request;

import net.weweave.tubewarder.service.model.AbstractRestModel;

public abstract class AbstractSetObjectRestRequest<T extends AbstractRestModel> extends AbstractRestRequest {
    public T object;
}
