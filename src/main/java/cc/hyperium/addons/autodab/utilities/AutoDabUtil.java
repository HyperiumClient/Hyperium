package cc.hyperium.addons.autodab.utilities;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;

public class AutoDabUtil {
    private static List<String> triggers;
    private static List<String> replaceTriggers;

    public static boolean hasWonGame(final String message) {
        if (!message.contains(Minecraft.getMinecraft().getSession().getUsername())) {
            return false;
        }
        for (final String trigger : AutoDabUtil.triggers) {
            if (message.contains(trigger)) {
                return true;
            }
        }
        for (final String trigger : AutoDabUtil.replaceTriggers) {
            if (message.contains(String.format(trigger, Minecraft.getMinecraft().getSession().getUsername()))) {
                return true;
            }
        }
        return message.contains("+") && message.contains("coins") && message.contains("Win");
    }

    public static Method getMethod(final Class<?> clazz, final String[] methodNames, final Class[] parameters) {
        for (final String name : methodNames) {
            try {
                final Method m = clazz.getDeclaredMethod(name, (Class<?>[])parameters);
                if (m != null) {
                    m.setAccessible(true);
                    return m;
                }
            }
            catch (NoSuchMethodException ex) {}
        }
        return null;
    }

    static {
        AutoDabUtil.triggers = Arrays.asList("1st Killer -", "1st Place -", "Winner:", "- Damage Dealt -", "Winning Team -", "1st -", "Winners:", "Winner:", "Winning Team:", "won the game!", "Top Seeker:", "1st Place:", "Last team standing!", "Winner #1 (", "Top Survivors", "Winners -", "Winner -");
        AutoDabUtil.replaceTriggers = Collections.singletonList("%s WINNER!");
    }
}
