package net.weweave.tubewarder.outputhandler.api;

import net.weweave.tubewarder.outputhandler.api.configoption.OutputHandlerConfigOption;

import java.util.List;
import java.util.Map;

/**
 * Output handlers must implement the IOutputHandler interface.
 */
public interface IOutputHandler {
    /**
     * Does the actual output handling/processing (e.g. send an email, write a file, etc.).
     *
     * @param config The configuration for this output handler
     * @param sender The sender address
     * @param recipient The recipient address
     * @param subject The rendered subject to process (may not be applicable for the concrete output handler)
     * @param content The rendered content to process
     * @param attachments A list of attachments (may not be applicable for the concrete output handler)
     */
    void process(Config config,
                 Address sender,
                 Address recipient,
                 String subject,
                 String content,
                 List<Attachment> attachments) throws
            TemporaryProcessingException,
            PermanentProcessingException;

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
