/*
 *  Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  Hyperium Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.handlers.handlers.keybinds;

import cc.hyperium.Hyperium;
import cc.hyperium.event.GameShutDownEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.KeypressEvent;
import cc.hyperium.gui.NameHistoryGui;
import cc.hyperium.gui.integrations.HypixelFriendsGui;
import java.util.TreeMap;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;

public class KeyBindHandler {
    
    private final KeyBindConfig keyBindConfig;
    
    // Case insensitive treemap
    private final TreeMap<String, HyperiumBind> keybinds = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    
    public HyperiumBind debug = new HyperiumBind("friends", Keyboard.KEY_L) {
        @Override
        public void onPress() {
            Minecraft.getMinecraft().displayGuiScreen(new HypixelFriendsGui());
        }
    };
    
    public static HyperiumBind nameHistory = new HyperiumBind("nameHistory", Keyboard.KEY_N) {
        @Override
        public void onPress() {
            new NameHistoryGui().show();
        }
    };

    public KeyBindHandler() {
        this.keyBindConfig = new KeyBindConfig(this, Hyperium.folder);
        
        this.keyBindConfig.load();
        
        registerKeyBinding(debug);
        registerKeyBinding(nameHistory);
    }

    @InvokeEvent
    public void onKeyPress(KeypressEvent event) {
        if (Minecraft.getMinecraft().inGameHasFocus && Minecraft.getMinecraft().currentScreen == null) {
            for (HyperiumBind bind : this.keybinds.values()) {
                if (event.getKey() == bind.getKeyCode()) {
                    bind.onPress();
                    bind.setWasPressed(true);
                }
                
                if (bind.wasPressed() && !bind.isKeyDown()) {
                    bind.onRelease();
                    bind.setWasPressed(false);
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
     * controls menu, allowing the user to modify the keyblind
     *
     * @param bind the hyperium key we wish to register
     */
    public void registerKeyBinding(HyperiumBind bind) {
        this.keybinds.put(bind.getRealDescription(), bind);
        
        this.keyBindConfig.attemptKeyBindLoad(bind);
        
        Minecraft.getMinecraft().gameSettings.keyBindings = ArrayUtils
            .add(Minecraft.getMinecraft().gameSettings.keyBindings, bind);
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
    protected TreeMap<String, HyperiumBind> getKeybinds() {
        return this.keybinds;
    }
}
