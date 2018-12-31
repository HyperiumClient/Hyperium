package me.semx11.autotip.universal;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for Reflection.
 *
 * @author Semx11
 */
public class ReflectionUtil {

    private static Map<String, Class<?>> loadedClasses = new HashMap<>();
    private static Map<Class<?>, Map<Class<?>[], Constructor<?>>> loadedConstructors = new HashMap<>();
    private static Map<Class<?>, Map<String, Method>> loadedMethods = new HashMap<>();
    private static Map<Class<?>, Map<String, Field>> loadedFields = new HashMap<>();
    private static Map<Class<?>, Map<String, Enum<?>>> loadedEnums = new HashMap<>();

    public static Class<?> findClazz(String... classNames) {
        for (String className : classNames) {
            if (loadedClasses.containsKey(className)) {
                return loadedClasses.get(className);
            }
        }

        Exception err = null;
        for (String className : classNames) {
            try {
                Class clazz = Class.forName(className);
                loadedClasses.put(className, clazz);
                return clazz;
            } catch (ClassNotFoundException e) {
                err = e;
            }
        }
        throw new UnableToFindClassException(classNames, err);
    }

    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... params) {
        if (!loadedConstructors.containsKey(clazz)) {
            loadedConstructors.put(clazz, new HashMap<>());
        }

        Map<Class<?>[], Constructor<?>> clazzConstructors = loadedConstructors.get(clazz);

        if (clazzConstructors.containsKey(params)) {
            clazzConstructors.get(params);
        }

        Constructor<?> constructor;
        try {
            constructor = clazz.getConstructor(params);
            constructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new UnableToFindConstructorException(params, e);
        }

        clazzConstructors.put(params, constructor);
        loadedConstructors.put(clazz, clazzConstructors);
        return constructor;
    }

    public static Method findMethod(Class<?> clazz, String[] methodNames, Class<?>... params) {
        if (!loadedMethods.containsKey(clazz)) {
            loadedMethods.put(clazz, new HashMap<>());
        }

        Map<String, Method> clazzMethods = loadedMethods.get(clazz);

        Exception err = null;
        for (String methodName : methodNames) {
            if (clazzMethods.containsKey(methodName)) {
                return clazzMethods.get(methodName);
            }

            try {
                Method method = clazz.getMethod(methodName, params);
                method.setAccessible(true);

                clazzMethods.put(methodName, method);
                loadedMethods.put(clazz, clazzMethods);
                return method;
            } catch (NoSuchMethodException e) {
                err = e;
            }
        }
        throw new UnableToFindMethodException(methodNames, err);
    }

    public static Field findField(Class<?> clazz, String... fieldNames) {
        if (!loadedFields.containsKey(clazz)) {
            loadedFields.put(clazz, new HashMap<>());
        }

        Map<String, Field> clazzFields = loadedFields.get(clazz);

        Exception err = null;
        for (String fieldName : fieldNames) {
            if (clazzFields.containsKey(fieldName)) {
                return clazzFields.get(fieldName);
            }

            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);

                clazzFields.put(fieldName, field);
                loadedFields.put(clazz, clazzFields);
                return field;
            } catch (NoSuchFieldException e) {
                err = e;
            } catch (SecurityException e) {
                throw new UnableToAccessFieldException(fieldNames, e);
            }
        }
        throw new UnableToFindFieldException(fieldNames, err);
    }

    public static Enum<?> getEnum(Class<?> clazz, String enumName) {
        if (!loadedEnums.containsKey(clazz)) {
            loadedEnums.put(clazz, new HashMap<>());
        }

        Map<String, Enum<?>> clazzEnums = loadedEnums.get(clazz);

        if (clazzEnums.containsKey(enumName.toUpperCase())) {
            return clazzEnums.get(enumName.toUpperCase());
        }

        if (clazz.getEnumConstants().length == 0) {
            throw new UnableToFindEnumException(enumName);
        }

        Enum<?> theEnum = null;
        for (Object o : clazz.getEnumConstants()) {
            Enum<?> anEnum = (Enum<?>) o;
            clazzEnums.put(anEnum.name(), anEnum);
            if (anEnum.name().equalsIgnoreCase(enumName)) {
                theEnum = anEnum;
                break;
            }
        }
        if (theEnum == null) {
            throw new UnableToFindEnumException(enumName);
        }

        loadedEnums.put(clazz, clazzEnums);
        return theEnum;
    }

    public static class UnableToFindMethodException extends RuntimeException {

        private static final long serialVersionUID = 2646222778476346499L;

        UnableToFindMethodException(String[] methodNames, Exception e) {
            super("Could not find methods: " + String.join(", ", methodNames), e);
        }

    }

    public static class UnableToFindClassException extends RuntimeException {

        private static final long serialVersionUID = 3898634214210207487L;

        UnableToFindClassException(String[] classNames, Exception e) {
            super("Could not find classes: " + String.join(", ", classNames), e);
        }

    }

    public static class UnableToAccessFieldException extends RuntimeException {

        private static final long serialVersionUID = 3624431716334716913L;

        UnableToAccessFieldException(String[] fieldNames, Exception e) {
            super("Could not access fields: " + String.join(", ", fieldNames), e);
        }

    }

    public static class UnableToFindFieldException extends RuntimeException {

        private static final long serialVersionUID = 6746871885265039462L;

        UnableToFindFieldException(String[] fieldNames, Exception e) {
            super("Could not find fields: " + String.join(", ", fieldNames), e);
        }

    }

    public static class UnableToFindEnumException extends RuntimeException {

        private static final long serialVersionUID = -6759637615386333754L;

        UnableToFindEnumException(String enumName) {
            super("Could not find enum: " + enumName);
        }

    }

    public static class UnableToFindConstructorException extends RuntimeException {

        private static final long serialVersionUID = 601263623563010837L;

        UnableToFindConstructorException(Class<?>[] params, Exception e) {
            super("Could not find params: " + String.join(", ", Arrays.stream(params)
                    .map(Class::getSimpleName)
                    .collect(Collectors.toList())), e);
        }

    }

}
