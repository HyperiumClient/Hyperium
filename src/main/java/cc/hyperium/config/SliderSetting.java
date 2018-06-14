package cc.hyperium.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SliderSetting {

    float min();

    float max();

    String name();

    Category category() default Category.GENERAL;
}
