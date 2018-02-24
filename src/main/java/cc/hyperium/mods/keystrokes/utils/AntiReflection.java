package cc.hyperium.mods.keystrokes.utils;

import sun.reflect.Reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Fyu's reflection class designed to hide methods and fields
 *      this prevents other mods or injections using or modifying any
 *      hidden methods or fields
 * <p>
 * Given to Sk1er by Fyu
 *
 * @author Fyu
 */
public class AntiReflection {

    /**
     * Creating a new instance is disabled.
     */
    private AntiReflection() {
    }

    /**
     * Filters multiple fields at once
     *
     * @param clazzes the classes we wish to filter
     */
    public static void filterMultipleClasses(Class<?>... clazzes) {
        for (Class<?> clazz : clazzes) {
            if (clazz != null) {
                filterClassMembers(clazz);
            }
        }
    }

    /**
     * Filters a class and hides all fields and methods with a hidden @annotation
     *
     * @param clazz class to filter and hide
     */
    public static void filterClassMembers(Class<?> clazz) {
        List<Field> hiddenFields = new ArrayList<>();
        List<Method> hiddenMethods = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(HiddenField.class) != null) {
                hiddenFields.add(field);
            }
        }

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getAnnotation(HiddenMethod.class) != null) {
                hiddenMethods.add(method);
            }
        }

        if (!hiddenFields.isEmpty()) {
            Reflection.filterFields(clazz, hiddenFields.toArray(new Field[hiddenFields.size()]));
        }

        if (!hiddenMethods.isEmpty()) {
            Reflection.filterMethods(clazz, hiddenMethods.toArray(new Method[hiddenMethods.size()]));
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface HiddenField {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface HiddenMethod {
    }

}