package com.hcc.addons.loader;

import com.hcc.addons.annotations.Instance;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Base for AddonLoader
 */
public class AddonLoaderStrategy {

    static void assignInstances(Object instance) throws IllegalAccessException {
        Class<?> clazz = instance.getClass();
        Field fields[] = clazz.getFields();
        for (Field field : fields) {

            Annotation annotation = field.getAnnotation(Instance.class);

            if (annotation != null) {
                field.setAccessible(true);
                field.set(instance, instance);
            }
        }
    }

    /**
     * Override this method in the AddonLoader
     * @param file file to load
     * @throws Exception when exception occurs
     */
    public void load(File file) throws Exception {
    }
}
