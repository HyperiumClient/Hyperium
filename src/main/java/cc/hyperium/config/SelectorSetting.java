package cc.hyperium.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface SelectorSetting {

    String name();

    Category category() default Category.GENERAL;

    String[] items();

    boolean enabled() default true;

    boolean mods() default false;
}

