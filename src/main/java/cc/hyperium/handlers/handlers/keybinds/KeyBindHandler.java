/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.handlers.handlers.keybinds;

import cc.hyperium.Hyperium;
import cc.hyperium.event.GameShutDownEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.KeypressEvent;
import cc.hyperium.event.KeyreleaseEvent;
import cc.hyperium.event.MouseButtonEvent;
import cc.hyperium.handlers.handlers.keybinds.keybinds.DabKeybind;
import cc.hyperium.handlers.handlers.keybinds.keybinds.FlipKeybind;
import cc.hyperium.handlers.handlers.keybinds.keybinds.FlossKeybind;
import cc.hyperium.handlers.handlers.keybinds.keybinds.FortniteDefaultDanceKeybind;
import cc.hyperium.handlers.handlers.keybinds.keybinds.FriendsKeybind;
import cc.hyperium.handlers.handlers.keybinds.keybinds.GuiDanceKeybind;
import cc.hyperium.handlers.handlers.keybinds.keybinds.GuiKeybind;
import cc.hyperium.handlers.handlers.keybinds.keybinds.NamesKeybind;
import cc.hyperium.handlers.handlers.keybinds.keybinds.RearCamKeybind;
import cc.hyperium.handlers.handlers.keybinds.keybinds.TPoseKeybind;
import cc.hyperium.handlers.handlers.keybinds.keybinds.TogglePerspectiveKeybind;
import cc.hyperium.handlers.handlers.keybinds.keybinds.ToggleSpotifyKeybind;
import cc.hyperium.handlers.handlers.keybinds.keybinds.ToggleSprintKeybind;
import cc.hyperium.handlers.handlers.keybinds.keybinds.TwerkDanceKeybind;
import cc.hyperium.handlers.handlers.keybinds.keybinds.UploadScreenshotKeybind;
import cc.hyperium.handlers.handlers.keybinds.keybinds.ViewStatsKeybind;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.Map;

public class KeyBindHandler {
    private static final Map<Integer, Integer> mouseBinds = new HashMap<>();
    public final HyperiumBind debug = new HyperiumBind("DEBUG", Keyboard.KEY_J) {
        @Override
        public void onPress() {

        }

        @Override
        public void onRelease() {
        }
    };
    private final KeyBindConfig keyBindConfig;
    // Case insensitive treemap
    private final Map<String, HyperiumBind> keybinds = new HashMap<>();
    private ToggleSprintKeybind toggleSprintKeybind;

    public KeyBindHandler() {
        this.keyBindConfig = new KeyBindConfig(this, Hyperium.folder);

        registerKeyBinding(debug);
        registerKeyBinding(new FriendsKeybind());
        registerKeyBinding(new NamesKeybind());
        registerKeyBinding(new GuiKeybind());
        registerKeyBinding(new DabKeybind());
        registerKeyBinding(new FlipKeybind());
        registerKeyBinding(new ViewStatsKeybind());
        registerKeyBinding(new FlossKeybind());
        registerKeyBinding(new ToggleSpotifyKeybind());
        registerKeyBinding(toggleSprintKeybind = new ToggleSprintKeybind());
        registerKeyBinding(new TogglePerspectiveKeybind());
        registerKeyBinding(new FortniteDefaultDanceKeybind());
        registerKeyBinding(new TwerkDanceKeybind());
        registerKeyBinding(new TPoseKeybind());
        registerKeyBinding(new GuiDanceKeybind());
        registerKeyBinding(new UploadScreenshotKeybind());
        registerKeyBinding(new RearCamKeybind());

        // Populate mouse bind list in accordance with Minecraft's values.
        for (int i = 0; i < 16; i++) {
            mouseBinds.put(i, -100 + i);
        }

        this.keyBindConfig.load();
    }

    @InvokeEvent
    public void onKeyPress(KeypressEvent event) {
        if (Minecraft.getMinecraft().inGameHasFocus && Minecraft.getMinecraft().currentScreen == null) {
            for (HyperiumBind bind : this.keybinds.values()) {
                if (bind.isConflicted()) {
                    continue;
                }
                if (event.getKey() == bind.getKeyCode()) {
                    bind.onPress();
                    bind.setWasPressed(true);
                }
            }
        }
    }

    @InvokeEvent
    public void onKeyRelease(KeyreleaseEvent event) {
        if (Minecraft.getMinecraft().inGameHasFocus && Minecraft.getMinecraft().currentScreen == null) {
            for (HyperiumBind bind : this.keybinds.values()) {
                if (bind.isConflicted()) {
                    continue;
                }
                if (event.getKey() == bind.getKeyCode()) {
                    bind.onRelease();
                    bind.setWasPressed(false);
                }
            }
        }
    }

    @InvokeEvent
    public void onMouseButton(MouseButtonEvent event) {
        // Dismisses mouse movement input.
        if (event.getValue() >= 0) {
            if (Minecraft.getMinecraft().inGameHasFocus && Minecraft.getMinecraft().currentScreen == null) {
                for (HyperiumBind bind : this.keybinds.values()) {
                    if (bind.isConflicted()) {
                        continue;
                    }
                    // Gets Minecraft value of the mouse value and checks to see if it matches a keybind.
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

    @InvokeEvent
    public void onGameShutdown(GameShutDownEvent event) {
        this.keyBindConfig.save();
    }

    /**
     * Grabs a binding from the registered keybindings list, this is case-insensitive and
     * any key/name may be provided without fear of causing issues
     *
     * @param name the name or id of the keybinding
     * @return a keybinding instance or null if nothing was found
     */
    public HyperiumBind getBinding(String name) {
        return this.keybinds.getOrDefault(name, null);
    }

    /**
     * Registers a Hyperium KeyBinding here & in the game code so it shows up in the
     * controls menu, allowing the user to modify the keybind
     *
     * @param bind the hyperium key we wish to register
     */
    public void registerKeyBinding(HyperiumBind bind) {
        this.keybinds.put(bind.getRealDescription(), bind);

        this.keyBindConfig.attemptKeyBindLoad(bind);
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

        this.keybinds.remove(bind.getRealDescription());
    }

    /**
     * Getter for the amazing KeyBind config
     *
     * @return the keybind config
     */
    public KeyBindConfig getKeyBindConfig() {
        return this.keyBindConfig;
    }

    /**
     * Getter for the all the registered key bindings, this is package
     * private to allow for saving and loading
     *
     * @return the keybinds
     */
    public Map<String, HyperiumBind> getKeybinds() {
        return this.keybinds;
    }

    public ToggleSprintKeybind getToggleSprintBind() {
        return toggleSprintKeybind;
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
