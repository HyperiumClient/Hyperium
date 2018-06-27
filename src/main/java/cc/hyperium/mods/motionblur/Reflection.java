package cc.hyperium.mods.motionblur;

import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Field;
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
