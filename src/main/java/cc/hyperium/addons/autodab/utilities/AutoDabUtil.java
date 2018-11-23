package cc.hyperium.addons.autodab.utilities;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * Class which contains a couple of utilities for the AutoDab addon
 */
public class AutoDabUtil {

    /**
     * The triggers of game wins
     */
    private static List<String> triggers;
    private static List<String> replaceTriggers;

    /**
     * Checks whether the message is an indicator to a victory
     *
     * @param message Message to check
     * @return {@code true} if the message indicates to a win, or {@code false} if
     * otherwise.
     */
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

    /**
     * Retrieves a method with the given name and parameters from class. It will return the first valid
     * method from the String array. Useful for obfuscated and non-obfuscated environments support.
     *
     * @param clazz       Class to lookup the methods from
     * @param methodNames Possible names of the method
     * @param parameters  Parameters of the given method
     * @return The first {@link Method} which matches the name
     */
    public static Method getMethod(final Class<?> clazz, final String[] methodNames, final Class[] parameters) {
        for (final String name : methodNames) {
            try {
                final Method m = clazz.getDeclaredMethod(name, (Class<?>[]) parameters);
                if (m != null) {
                    m.setAccessible(true);
                    return m;
                }
            } catch (NoSuchMethodException ignored) {
            }
        }
        return null;
    }

    static {
        AutoDabUtil.triggers = ImmutableList.of("1st Killer -", "1st Place -", "Winner:", "- Damage Dealt -", "Winning Team -", "1st -", "Winners:", "Winner:", "Winning Team:", "won the game!", "Top Seeker:", "1st Place:", "Last team standing!", "Winner #1 (", "Top Survivors", "Winners -", "Winner -");
        AutoDabUtil.replaceTriggers = Collections.singletonList("%s WINNER!");
    }
}
