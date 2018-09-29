package cc.hyperium.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Assign to a method to invoke an event
 * The first parameter of the method should be the event it is calling
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InvokeEvent {

    Priority priority() default Priority.NORMAL;
}
