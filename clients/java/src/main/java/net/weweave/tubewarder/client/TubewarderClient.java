package net.weweave.tubewarder.client;

public abstract class TubewarderClient {
    private final String uri;

    public TubewarderClient(String uri) {
        this.uri = (uri.endsWith("/") ? uri : uri + "/");
    }

    public abstract SendResponse send(SendRequest request);

    public String getUri() {
        return uri;
    }
}
