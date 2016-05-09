package net.weweave.tubewarder.client;

/**
 * An abstract class for sending messages to a Tubewarder server.
 * The concrete implementations define the used protocol and libs.
 */
public abstract class TubewarderClient {
    private final String uri;

    /**
     * @param uri The base uri to the Tubewarder server (i.e. https://localhost/)
     */
    public TubewarderClient(String uri) {
        this.uri = (uri.endsWith("/") ? uri : uri + "/");
    }

    /**
     * Sends a concrete message
     * @param request
     * @return
     */
    public abstract SendResponse send(SendRequest request);

    public String getUri() {
        return uri;
    }
}
