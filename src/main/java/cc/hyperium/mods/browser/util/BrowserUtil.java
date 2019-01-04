package cc.hyperium.mods.browser.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;

public class BrowserUtil {

    private static final Map<Integer, Integer> keyModifiers = new HashMap<>();

    static {
        keyModifiers.put(Keyboard.KEY_LSHIFT, 1);
        keyModifiers.put(Keyboard.KEY_RSHIFT, 1);

        keyModifiers.put(Keyboard.KEY_LCONTROL, 2);
        keyModifiers.put(Keyboard.KEY_RCONTROL, 2);

        keyModifiers.put(Keyboard.KEY_LMENU, 3);
        keyModifiers.put(Keyboard.KEY_RMENU, 3);
    }

    public static int getModifierInt() {
        for (Entry<Integer, Integer> entry : keyModifiers.entrySet()) {
            if (Keyboard.isKeyDown(entry.getKey())) {
                return entry.getValue();
            }
        }
        return 0;
    }

}
