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

package cc.hyperium.mods.coalores;

import cc.hyperium.Hyperium;
import cc.hyperium.gui.settings.items.GeneralSetting;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;

import cc.hyperium.handlers.handlers.keybinds.KeyBindHandler;
import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

/**
 * The perspective mod handler
 */
public class PerspectiveModifierContainer {
    
    /**
     * This class requires massive improvements, this will be worked on and fixed in a later release
     */
    
    private final HyperiumBind perspective = new PerspectiveKeyBind();
    
    public PerspectiveModifierContainer() {
        KeyBindHandler keyBindHandler = Hyperium.INSTANCE.getHandlers().getKeybindHandler();
        
        if (keyBindHandler.getBinding("perspective") == null) {
            keyBindHandler.registerKeyBinding(this.perspective);
        }
    }
    
    private boolean enabled = false;

    public float modifiedYaw;
    public float modifiedPitch;

    public void onEnable() {
        Minecraft.getMinecraft().gameSettings.thirdPersonView = 2;
        //GeneralChatHandler.instance().sendMessage("Enabled 360 Degree Perspective.");

        this.modifiedYaw = Minecraft.getMinecraft().thePlayer.cameraYaw;
        this.modifiedPitch = Minecraft.getMinecraft().thePlayer.cameraPitch;
    }

    public void onDisable() {
        this.enabled = false;
        
        //((MixinKeyBinding) this.perspective).setPressed(false);
    
        Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;
        //GeneralChatHandler.instance().sendMessage("Disabled 360 Degree Perspective.");
    
        // Reset the states anyway
        this.modifiedYaw = Minecraft.getMinecraft().thePlayer.cameraYaw;
        this.modifiedPitch = Minecraft.getMinecraft().thePlayer.cameraPitch;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    class PerspectiveKeyBind extends HyperiumBind {
    
        public PerspectiveKeyBind() {
            super("perspective", Keyboard.KEY_P);
        }
    
        @Override
        public void onPress() {
            if (GeneralSetting.perspectiveHoldDownEnabled) {
                onEnable();
                setEnabled(true);
            } else {
                setEnabled(!isEnabled());
    
                if (!GeneralSetting.perspectiveHoldDownEnabled && !isEnabled()) {
                    onDisable();
                } else {
                    onEnable();
                }
            }
        }
    
        @Override
        public void onRelease() {
            if (GeneralSetting.perspectiveHoldDownEnabled) {
                onDisable();
            }
        }
    }
}
