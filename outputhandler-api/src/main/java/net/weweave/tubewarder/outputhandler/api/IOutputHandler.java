package net.weweave.tubewarder.outputhandler.api;

import net.weweave.tubewarder.outputhandler.api.configoption.OutputHandlerConfigOption;

import java.util.List;

/**
 * Output handlers must implement the IOutputHandler interface.
 */
public interface IOutputHandler {
    /**
     * Does the actual output handling/processing (e.g. send an email, write a file, etc.).
     *
     * @param config The configuration for this output handler
     * @param item The item to be sent
     * @throws TemporaryProcessingException Thrown if a temporary exception occurred. The send queue scheduler should
     *                                      try to send the item again later.
     * @throws PermanentProcessingException Thrown if a permanent exception occurred. The send queue scheduler should
     *                                      give up.
     */
    void process(Config config, SendItem item) throws TemporaryProcessingException, PermanentProcessingException;

    /**
     * Returns a list of valid configuration options for this output handler.
     *
     * @return A list of config options
     */
    List<OutputHandlerConfigOption> getConfigOptions();

    /**
     * Checks if the output handler can work with the supplied configuration. If not, an InvalidConfigException is thown.
     *
     * @param config The configuration instance to check
     * @throws InvalidConfigException Thrown if the output handler cannot work with the supplied config
     */
    void checkConfig(Config config) throws InvalidConfigException;

    /**
     * Checks if the output handler can work with the supplied address (e.g. an sms output handler needs a different
     * address format than an email output handler).
     *
     * @param address The address instance to check.
     * @throws InvalidAddessException Thrown is the output handler cannot work with the supplied address
     */
    void checkRecipientAddress(Address address) throws InvalidAddessException;
}
