package eio.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/test")
public class TestService {
    @GET
    public String action() {
        return "Hallo Test!";
    }
}
