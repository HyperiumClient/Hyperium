package cc.hyperium.internal.addons;

import cc.hyperium.gui.main.HyperiumOverlay;
import cc.hyperium.internal.addons.misc.AddonLoadException;

import java.util.HashSet;
import java.util.Set;

public class OverlayChecker {
    /*
    in java because im bad at kotlin
     */
    public static void checkOverlayField(String value) throws AddonLoadException {
        try {
            Class<?> originClass = Class.forName(value);
            Set<Class<?>> allClasses = getSuperClasses(originClass, false);
            boolean isOverlay = false;
            for (Class<?> clazz : allClasses) {
                if (clazz.equals(HyperiumOverlay.class)) {
                    isOverlay = true;
                }
            }
            if (!isOverlay) {
                throw new AddonLoadException("overlay has to be an instance of HyperiumOverlay");
            }
        } catch (ClassNotFoundException e) {
            throw new AddonLoadException("Class " + value + " not found.");
        }
    }
    private static Set<Class<?>> getSuperClasses(Class<?> clazz, boolean inner) {
        Set<Class<?>> temp = new HashSet<>();
        if (!inner) {
            temp.add(clazz);
        }
        if (!clazz.getSuperclass().equals(Object.class)) {
            temp.addAll(getSuperClasses(clazz.getSuperclass(), true));
        }
        return temp;
    }
}
