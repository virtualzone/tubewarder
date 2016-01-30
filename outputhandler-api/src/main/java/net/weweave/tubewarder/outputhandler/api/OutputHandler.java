package net.weweave.tubewarder.outputhandler.api;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OutputHandler {
    String id();
    String name();
}
