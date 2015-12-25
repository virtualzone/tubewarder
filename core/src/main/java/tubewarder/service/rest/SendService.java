package tubewarder.service.rest;

import tubewarder.service.common.SendServiceCommon;
import tubewarder.service.model.AbstractResponse;
import tubewarder.service.model.SendModel;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/send")
public class SendService {
    @Inject
    private SendServiceCommon sendServiceCommon;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public AbstractResponse action(SendModel sendModel) {
        return getSendServiceCommon().process(sendModel);
    }

    public SendServiceCommon getSendServiceCommon() {
        return sendServiceCommon;
    }

    public void setSendServiceCommon(SendServiceCommon sendServiceCommon) {
        this.sendServiceCommon = sendServiceCommon;
    }
}
