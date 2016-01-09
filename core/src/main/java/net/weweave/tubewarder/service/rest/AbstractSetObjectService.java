package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.exception.InvalidInputParametersException;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.service.model.AbstractRestModel;
import net.weweave.tubewarder.dao.AbstractDao;
import org.apache.commons.validator.GenericValidator;

import java.io.Serializable;

public abstract class AbstractSetObjectService<I extends AbstractRestModel, O extends Serializable> extends AbstractService {
    protected abstract void validateInputParameters(I model) throws InvalidInputParametersException;
    protected abstract O createObject(I model) throws ObjectNotFoundException;
    protected abstract void updateObject(O output, I model) throws ObjectNotFoundException;
    protected abstract AbstractDao<O> getObjectDao();

    protected O createUpdateObject(I model) throws ObjectNotFoundException {
        O object;
        if (!GenericValidator.isBlankOrNull(model.id)) {
            object = getObjectDao().get(model.id);
        } else {
            object = createObject(model);
        }
        updateObject(object, model);
        return object;
    }
}
