package net.weweave.tubewarder.test.outputhandler;


import net.weweave.tubewarder.outputhandler.WebserviceOutputHandler;
import net.weweave.tubewarder.outputhandler.api.Config;
import net.weweave.tubewarder.outputhandler.api.InvalidConfigException;
import org.junit.Test;

public class TestWebserviceOutputHandler {
    @Test
    public void testCheckConfigSuccess() throws Exception {
        Config config = getConfig();
        WebserviceOutputHandler handler = new WebserviceOutputHandler();
        handler.checkConfig(config);
    }

    @Test(expected = InvalidConfigException.class)
    public void testCheckConfigEmptyUrl() throws Exception {
        Config config = getConfig();
        config.put("url", "");
        WebserviceOutputHandler handler = new WebserviceOutputHandler();
        handler.checkConfig(config);
    }

    @Test(expected = InvalidConfigException.class)
    public void testCheckConfigInvalidUrl() throws Exception {
        Config config = getConfig();
        config.put("url", "missing.protocol/some/url.php?recipient=${recipientAddress}&param2=test");
        WebserviceOutputHandler handler = new WebserviceOutputHandler();
        handler.checkConfig(config);
    }

    @Test
    public void testCheckConfigIpUrl() throws Exception {
        Config config = getConfig();
        config.put("url", "https://127.0.0.1/some/url.php?recipient=${recipientAddress}&param2=test");
        WebserviceOutputHandler handler = new WebserviceOutputHandler();
        handler.checkConfig(config);
    }

    @Test
    public void testCheckConfigNoPath() throws Exception {
        Config config = getConfig();
        config.put("url", "https://127.0.0.1/?recipient=${recipientAddress}&param2=test");
        WebserviceOutputHandler handler = new WebserviceOutputHandler();
        handler.checkConfig(config);
    }

    @Test(expected = InvalidConfigException.class)
    public void testCheckConfigInvalidMethod() throws Exception {
        Config config = getConfig();
        config.put("method", "PATCH");
        WebserviceOutputHandler handler = new WebserviceOutputHandler();
        handler.checkConfig(config);
    }

    @Test(expected = InvalidConfigException.class)
    public void testCheckConfigInvalidAuth() throws Exception {
        Config config = getConfig();
        config.put("authType", "SOMETHING");
        WebserviceOutputHandler handler = new WebserviceOutputHandler();
        handler.checkConfig(config);
    }

    @Test
    public void testCheckConfigBasicAuth() throws Exception {
        Config config = getConfig();
        config.put("authType", "BASIC");
        config.put("username", "testUser");
        config.put("password", "testPass");
        WebserviceOutputHandler handler = new WebserviceOutputHandler();
        handler.checkConfig(config);
    }

    @Test
    public void testCheckConfigMethodPost() throws Exception {
        Config config = getConfig();
        config.put("method", "POST");
        WebserviceOutputHandler handler = new WebserviceOutputHandler();
        handler.checkConfig(config);
    }

    @Test
    public void testCheckConfigMethodGet() throws Exception {
        Config config = getConfig();
        config.put("method", "GET");
        WebserviceOutputHandler handler = new WebserviceOutputHandler();
        handler.checkConfig(config);
    }

    private Config getConfig() {
        Config config = new Config();
        config.put("id", "WEBSERVICE");
        config.put("url", "http://localhost/some/url.php?recipient=${recipientAddress}&param2=test");
        config.put("method", "POST");
        config.put("authType", "NONE");
        config.put("contentType", "text/plain");
        config.put("payload", "Line 1\nLine 2");
        return config;
    }
}
