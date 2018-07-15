package cc.hyperium.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * Created by Cubxity on 03/06/2018
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ToggleSetting {
    String name();

    boolean enabled() default true;

    Category category() default Category.GENERAL;

    boolean mods() default false;
}
