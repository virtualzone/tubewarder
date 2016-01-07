package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.service.model.ChannelModel;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.response.GetChannelResponse;
import org.apache.commons.validator.GenericValidator;
import net.weweave.tubewarder.dao.ChannelDao;
import net.weweave.tubewarder.domain.Channel;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;

@RequestScoped
@Path("/channel/get")
public class GetChannelService {
    @Inject
    private ChannelDao channelDao;

    @GET
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public GetChannelResponse action(@QueryParam("id") @DefaultValue("") String id) {
        GetChannelResponse response = new GetChannelResponse();

        try {
            setResponseList(response, id);
        } catch (Exception e) {
            response.error = ErrorCode.INVALID_INPUT_PARAMETERS;
        }

        return response;
    }

    private void setResponseList(GetChannelResponse response, String id) throws ObjectNotFoundException {
        if (GenericValidator.isBlankOrNull(id)) {
            List<Channel> channels = getChannelDao().getAll();
            for (Channel channel : channels) {
                response.channels.add(ChannelModel.factory(channel));
            }
        } else {
            Channel channel = getChannelDao().get(id);
            response.channels.add(ChannelModel.factory(channel));
        }
    }

    public ChannelDao getChannelDao() {
        return channelDao;
    }

    public void setChannelDao(ChannelDao channelDao) {
        this.channelDao = channelDao;
    }
}