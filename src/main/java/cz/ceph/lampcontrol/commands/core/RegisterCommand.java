package cz.ceph.lampcontrol.commands.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by SiOnzee on 10.01.2017.
 */


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RegisterCommand {
    /**
     * Command execution name /value
     */
    String value();

    /**
     * Aliases to command execution name
     */
    String[] alias() default {};
}
