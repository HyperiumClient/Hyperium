package cc.hyperium.mods.motionblur;

import java.lang.reflect.Method;

public class Reflection {

    public static Method getMethod(Class<?> clazz, String[] methodNames, Class[] parameters) {
        for (String name : methodNames) {
            try {
                Method m = clazz.getDeclaredMethod(name, parameters);
                if (m != null) {
                    m.setAccessible(true);
                    return m;
                }
            } catch (NoSuchMethodException e) {

            }
        }
        return null;
    }

}
