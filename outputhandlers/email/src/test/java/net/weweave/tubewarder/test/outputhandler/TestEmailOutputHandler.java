package net.weweave.tubewarder.test.outputhandler;

import net.weweave.tubewarder.outputhandler.EmailOutputHandler;
import net.weweave.tubewarder.outputhandler.api.Address;
import net.weweave.tubewarder.outputhandler.api.Config;
import net.weweave.tubewarder.outputhandler.api.InvalidAddessException;
import org.junit.Test;

public class TestEmailOutputHandler {
    @Test
    public void testCheckConfigSuccess() throws Exception {
        Config config = getConfig();
        EmailOutputHandler handler = new EmailOutputHandler();
        handler.checkConfig(config);
    }

    @Test
    public void testCheckRecipientSuccess() throws Exception {
        Address recipient = new Address("John Doe", "no-reply@weweave.net");
        EmailOutputHandler handler = new EmailOutputHandler();
        handler.checkRecipientAddress(recipient);
    }

    @Test(expected=InvalidAddessException.class)
    public void testCheckRecipientNumberPrefix() throws Exception {
        Address recipient = new Address("John Doe", "+49123456");
        EmailOutputHandler handler = new EmailOutputHandler();
        handler.checkRecipientAddress(recipient);
    }

    @Test(expected=InvalidAddessException.class)
    public void testCheckRecipientNumber() throws Exception {
        Address recipient = new Address("John Doe", "0123456789");
        EmailOutputHandler handler = new EmailOutputHandler();
        handler.checkRecipientAddress(recipient);
    }

    @Test(expected=InvalidAddessException.class)
    public void testCheckRecipientString() throws Exception {
        Address recipient = new Address("John Doe", "thisisnomailaddress");
        EmailOutputHandler handler = new EmailOutputHandler();
        handler.checkRecipientAddress(recipient);
    }

    private Config getConfig() {
        Config config = new Config();
        config.put("id", "EMAIL");
        config.put("smtpServer", "localhost");
        config.put("port", 25);
        config.put("auth", false);
        config.put("security", "NONE");
        config.put("contentType", "text/plain");
        config.put("simulate", true);
        return config;
    }
}
