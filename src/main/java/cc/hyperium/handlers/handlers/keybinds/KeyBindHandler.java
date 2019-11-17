/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.handlers.handlers.keybinds;

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.client.GameShutDownEvent;
import cc.hyperium.event.interact.KeyPressEvent;
import cc.hyperium.event.interact.KeyReleaseEvent;
import cc.hyperium.event.interact.MouseButtonEvent;
import cc.hyperium.handlers.handlers.keybinds.keybinds.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.Map;

public class KeyBindHandler {

    private static final Map<Integer, Integer> mouseBinds = new HashMap<>();

    public final HyperiumBind debug = new HyperiumBind("DEBUG", Keyboard.KEY_J) {
        @Override
        public void onPress() {
            Hyperium.LOGGER.debug("debug keybind pressed");
        }

        @Override
        public void onRelease() {
            Hyperium.LOGGER.debug("debug keybind released");
        }
    };

    private final KeyBindConfig keyBindConfig;
    // Case insensitive treemap
    private final Map<String, HyperiumBind> keybinds = new HashMap<>();

    public KeyBindHandler() {
        keyBindConfig = new KeyBindConfig(this, Hyperium.folder);

        registerKeyBinding(debug);
        registerKeyBinding(new FriendsKeybind());
        registerKeyBinding(new NamesKeybind());
        registerKeyBinding(new GuiKeybind());
        registerKeyBinding(new DabKeybind());
        registerKeyBinding(new FlipKeybind());
        registerKeyBinding(new ViewStatsKeybind());
        registerKeyBinding(new FlossKeybind());
        registerKeyBinding(new ToggleSprintKeybind());
        registerKeyBinding(new TogglePerspectiveKeybind());
        registerKeyBinding(new TwerkDanceKeybind());
        registerKeyBinding(new TPoseKeybind());
        registerKeyBinding(new GuiDanceKeybind());
        registerKeyBinding(new UploadScreenshotKeybind());
        registerKeyBinding(new RearCamKeybind());
        registerKeyBinding(new HideLeatherKeybind());

        // Populate mouse bind list in accordance with Minecraft's values.
        int bound = keybinds.size();
        for (int i = 0; i < bound; i++) {
            mouseBinds.put(i, -100 + i);
        }
        keyBindConfig.load();
    }

    @InvokeEvent
    public void onKeyPress(KeyPressEvent event) {
        if (Minecraft.getMinecraft().inGameHasFocus && Minecraft.getMinecraft().currentScreen == null) {
            for (HyperiumBind bind : keybinds.values()) {
                if (!bind.isConflicted()) {
                    if (event.getKey() == bind.getKeyCode()) {
                        bind.onPress();
                        bind.setWasPressed(true);
                    }
                }
            }
        }
    }

    @InvokeEvent
    public void onKeyRelease(KeyReleaseEvent event) {
        if (Minecraft.getMinecraft().inGameHasFocus && Minecraft.getMinecraft().currentScreen == null) {
            for (HyperiumBind bind : keybinds.values()) {
                if (!bind.isConflicted()) {
                    if (event.getKey() == bind.getKeyCode()) {
                        bind.onRelease();
                        bind.setWasPressed(false);
                    }
                }
            }
        }
    }

    @InvokeEvent
    public void onMouseButton(MouseButtonEvent event) {
        // Dismisses mouse movement input.
        if (event.getValue() >= 0) {
            if (Minecraft.getMinecraft().inGameHasFocus && Minecraft.getMinecraft().currentScreen == null) {
                // Gets Minecraft value of the mouse value and checks to see if it matches a keybind.
                for (HyperiumBind bind : keybinds.values()) {
                    if (!bind.isConflicted()) {
                        if (mouseBinds.get(event.getValue()) == bind.getKeyCode()) {
                            if (event.getState()) {
                                bind.onPress();
                                bind.setWasPressed(true);
                            } else {
                                bind.onRelease();
                                bind.setWasPressed(false);
                            }
                        }
                    }
                }
            }
        }
    }

    @InvokeEvent
    public void onGameShutdown(GameShutDownEvent event) {
        keyBindConfig.save();
    }

    /**
     * Grabs a binding from the registered keybindings list, this is case-insensitive and
     * any key/name may be provided without fear of causing issues
     *
     * @param name the name or id of the keybinding
     * @return a keybinding instance or null if nothing was found
     */
    public HyperiumBind getBinding(String name) {
        return keybinds.getOrDefault(name, null);
    }

    /**
     * Registers a Hyperium KeyBinding here & in the game code so it shows up in the
     * controls menu, allowing the user to modify the keybind
     *
     * @param bind the hyperium key we wish to register
     */
    public void registerKeyBinding(HyperiumBind bind) {
        keybinds.put(bind.getRealDescription(), bind);
        keyBindConfig.attemptKeyBindLoad(bind);
    }

    /**
     * Removes a keybind from the registry. Unbinds this key and removes it
     *
     * @param bind the hyperium key we want to remove
     */
    public void unregisterKeyBinding(HyperiumBind bind) {
        if (bind.wasPressed()) {
            bind.onRelease();
            bind.setWasPressed(false);
        }

        // If bind is being held.
        if (bind.wasPressed()) {
            bind.onRelease();
            return;
        }

        keybinds.remove(bind.getRealDescription());
    }

    /**
     * Getter for the amazing KeyBind config
     *
     * @return the keybind config
     */
    public KeyBindConfig getKeyBindConfig() {
        return keyBindConfig;
    }

    /**
     * Getter for the all the registered key bindings, this is package
     * private to allow for saving and loading
     *
     * @return the keybinds
     */
    public Map<String, HyperiumBind> getKeybinds() {
        return keybinds;
    }

    public void releaseAllKeybinds() {
        if (!keybinds.isEmpty()) {
            for (Map.Entry<String, HyperiumBind> map : keybinds.entrySet()) {
                HyperiumBind bind = map.getValue();
                if (bind.wasPressed()) {
                    bind.onRelease();
                    bind.setWasPressed(false);
                    return;
                }

                // If bind is being held.
                if (bind.wasPressed()) {
                    bind.onRelease();
                    return;
                }
            }
        }
    }
}
