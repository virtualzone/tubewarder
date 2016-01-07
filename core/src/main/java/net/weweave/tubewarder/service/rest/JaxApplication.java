package net.weweave.tubewarder.service.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

@ApplicationPath("/rs")
public class JaxApplication extends Application {
    public static final String APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON + "; charset=UTF-8";
}
