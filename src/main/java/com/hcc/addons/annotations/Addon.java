package com.hcc.addons.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Addon {

    /**
     * @return modid
     */
    String modid();

    /**
     * @return version
     */
    String version();

}
