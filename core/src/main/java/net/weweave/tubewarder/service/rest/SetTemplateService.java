package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.dao.UserGroupDao;
import net.weweave.tubewarder.domain.Session;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.domain.UserGroup;
import net.weweave.tubewarder.exception.AuthRequiredException;
import net.weweave.tubewarder.exception.PermissionException;
import org.apache.commons.validator.GenericValidator;
import net.weweave.tubewarder.dao.TemplateDao;
import net.weweave.tubewarder.domain.Template;
import net.weweave.tubewarder.exception.InvalidInputParametersException;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.model.TemplateModel;
import net.weweave.tubewarder.service.request.SetTemplateRequest;
import net.weweave.tubewarder.service.response.SetObjectRestResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/template/set")
public class SetTemplateService extends AbstractSetObjectService<TemplateModel, Template> {
    @Inject
    private TemplateDao templateDao;

    @Inject
    private UserGroupDao userGroupDao;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public SetObjectRestResponse action(SetTemplateRequest request) {
        SetObjectRestResponse response = new SetObjectRestResponse();
        try {
            Session session = getSession(request.token);
            checkPermissions(session.getUser(), request.object);
            validateInputParameters(request.object);
            Template object = createUpdateObject(request.object);
            response.id = object.getExposableId();
        } catch (InvalidInputParametersException e) {
            addErrorsToResponse(response, e);
        } catch (ObjectNotFoundException e) {
            response.error = ErrorCode.OBJECT_LOOKUP_ERROR;
        } catch (PermissionException e) {
            response.error = ErrorCode.PERMISSION_DENIED;
        } catch (AuthRequiredException e) {
            response.error = ErrorCode.AUTH_REQUIRED;
        }
        return response;
    }

    private void checkPermissions(User user, TemplateModel model) throws PermissionException {
        if (user == null ||
                !user.getAllowTemplates()) {
            throw new PermissionException();
        }

        // Check if user is allowed to assign specified UserGroup
        try {
            UserGroup group = getUserGroupDao().get(model.group.id);
            if (!getUserGroupDao().isUserMemberOfGroup(user, group)) {
                throw new PermissionException();
            }
        } catch (ObjectNotFoundException e) {
            throw new PermissionException();
        }
    }

    @Override
    protected void validateInputParameters(TemplateModel model) throws InvalidInputParametersException {
        if (GenericValidator.isBlankOrNull(model.name)) {
            throw new InvalidInputParametersException("name", ErrorCode.FIELD_REQUIRED);
        }
        if (model.group == null) {
            throw new InvalidInputParametersException("group", ErrorCode.FIELD_REQUIRED);
        }
        if (GenericValidator.isBlankOrNull(model.group.id)) {
            throw new InvalidInputParametersException("group.id", ErrorCode.FIELD_REQUIRED);
        }

        // Check if object is to be created, but name already exists
        if (GenericValidator.isBlankOrNull(model.id)) {
            try {
                getObjectDao().getByName(model.name);
                throw new InvalidInputParametersException("name", ErrorCode.FIELD_NAME_ALREADY_EXISTS);
            } catch (ObjectNotFoundException e) {
                // This is okay
            }
        }

        // Check if object is to be updated, but (new) name already exists
        if (!GenericValidator.isBlankOrNull(model.id)) {
            try {
                Template template = getObjectDao().getByName(model.name);
                // Match found - okay if it's the object to be updated itself
                if (!model.id.equals(template.getExposableId())) {
                    throw new InvalidInputParametersException("name", ErrorCode.FIELD_NAME_ALREADY_EXISTS);
                }
            } catch (ObjectNotFoundException e) {
                // This is okay
            }
        }
    }

    @Override
    protected Template createObject(TemplateModel model) throws ObjectNotFoundException {
        Template object = new Template();
        getObjectDao().store(object);
        return object;
    }

    @Override
    protected void updateObject(Template object, TemplateModel model) throws ObjectNotFoundException {
        UserGroup group = getUserGroupDao().get(model.group.id);

        object.setName(model.name.trim());
        object.setUserGroup(group);
        getObjectDao().update(object);
    }

    @Override
    public TemplateDao getObjectDao() {
        return templateDao;
    }

    public UserGroupDao getUserGroupDao() {
        return userGroupDao;
    }

    public void setUserGroupDao(UserGroupDao userGroupDao) {
        this.userGroupDao = userGroupDao;
    }
}
