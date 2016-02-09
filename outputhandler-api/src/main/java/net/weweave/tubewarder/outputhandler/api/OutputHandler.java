package net.weweave.tubewarder.outputhandler.api;

import java.lang.annotation.*;

/**
 * Supplies additional information about an output handler. All output handler classes must be annotated with this.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OutputHandler {
    /**
     * @return A unique output handler id
     */
    String id();

    /**
     * @return A readable name for the output handler
     */
    String name();
}
