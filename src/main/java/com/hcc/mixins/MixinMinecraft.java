/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
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

package com.hcc.mixins;

import com.hcc.HCC;
import com.hcc.event.*;
import com.hcc.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.Timer;
import net.minecraft.util.Util;
import net.minecraft.world.WorldSettings;
import org.apache.commons.io.IOUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.InputStream;
import java.nio.ByteBuffer;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow
    @Final
    private DefaultResourcePack mcDefaultResourcePack;
    
    @Shadow private static Minecraft theMinecraft;
    
    @Shadow public GuiScreen currentScreen;
    
    @Shadow public WorldClient theWorld;
    
    @Shadow public EntityPlayerSP thePlayer;
    
    @Shadow public GameSettings gameSettings;
    
    @Shadow public GuiIngame ingameGUI;
    
    @Shadow private SoundHandler mcSoundHandler;
    
    @Shadow public boolean skipRenderWorld;
    
    @Accessor
    public abstract Timer getTimer();

    /**
     * Invoked once the game has be launched
     *
     * @param ci {@see org.spongepowered.asm.mixin.injection.callback.CallbackInfo}
     */
    @Inject(method = "startGame", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        EventBus.INSTANCE.register(HCC.INSTANCE);
        HCC.INSTANCE.registerAddons();
        EventBus.INSTANCE.post(new InitializationEvent());
    }


    /**
     * Invoked every tick (every 50milliseconds)
     *
     * @param ci {@see org.spongepowered.asm.mixin.injection.callback.CallbackInfo}
     */
    @Inject(method = "runTick", at = @At("RETURN"))
    private void runTick(CallbackInfo ci) {
        EventBus.INSTANCE.post(new TickEvent());
    }

    /**
     * Invoked once the player has pressed a key
     *
     * @param ci {@see org.spongepowered.asm.mixin.injection.callback.CallbackInfo}
     */
    @Inject(method = "dispatchKeypresses", at = @At(value = "INVOKE_ASSIGN", target = "Lorg/lwjgl/input/Keyboard;getEventKeyState()Z"))
    private void runTickKeyboard(CallbackInfo ci) {
        EventBus.INSTANCE.post(new KeypressEvent(Keyboard.getEventKey(), Keyboard.isRepeatEvent()));
    }

    /**
     * Invoked once the player has pressed mouse button 1
     *
     * @param ci {@see org.spongepowered.asm.mixin.injection.callback.CallbackInfo}
     */
    @Inject(method = "clickMouse", at = @At("RETURN"))
    private void clickMouse(CallbackInfo ci) {
        EventBus.INSTANCE.post(new LeftMouseClickEvent());
    }

    /**
     * Invoked once the player has pressed mouse button 1
     *
     * @param ci {@see org.spongepowered.asm.mixin.injection.callback.CallbackInfo}
     */
    @Inject(method = "rightClickMouse", at = @At("RETURN"))
    private void rightClickMouse(CallbackInfo ci) {
        EventBus.INSTANCE.post(new RightMouseClickEvent());
    }

    /**
     * Invoked once the player has joined a singleplayer world
     *
     * @param ci {@see org.spongepowered.asm.mixin.injection.callback.CallbackInfo}
     */
    @Inject(method = "launchIntegratedServer", at = @At("HEAD"))
    private void launchIntegratedServer(String folderName, String worldName, WorldSettings worldSettingsIn, CallbackInfo ci) {
        EventBus.INSTANCE.post(new SingleplayerJoinEvent());
    }

    /**
     * Fixes bug MC-68754 and MC-111254
     *
     * @param ci
     */
    @Inject(method = "toggleFullscreen", at = @At(value = "JUMP", target = "Lnet/minecraft/client/Minecraft;toggleFullscreen()V", shift = At.Shift.AFTER))
    private void toggleFullScreen(CallbackInfo ci) {
        Display.setResizable(false);
        Display.setResizable(true);
    }

    /**
     * Sets Minecraft Icon
     *
     * @author Cubxity
     */
    @Overwrite
    private void setWindowIcon() {
        if (Util.getOSType() != Util.EnumOS.OSX) {
            InputStream inputstream = null;
            InputStream inputstream1 = null;
            try {
                inputstream = Minecraft.class.getResourceAsStream("/assets/hcc/icons/icon-16x.png");
                inputstream1 = Minecraft.class.getResourceAsStream("/assets/hcc/icons/icon-32x.png");

                if (inputstream != null && inputstream1 != null) {
                    Display.setIcon(new ByteBuffer[]{Utils.INSTANCE.readImageToBuffer(inputstream),
                            Utils.INSTANCE.readImageToBuffer(inputstream1)});
                }
            } catch (Exception ex) {
                HCC.LOGGER.error("Couldn't set Windows Icon", ex);
            } finally {
                IOUtils.closeQuietly(inputstream);
                IOUtils.closeQuietly(inputstream1);
            }
        }
    }
    
    /**
     * A change to gui display so a "GuiOpenEvent" can be called to set the screen
     *
     * @author boomboompower
     */
    @Overwrite
    public void displayGuiScreen(GuiScreen guiScreenIn) {
        if (this.currentScreen != null) {
            this.currentScreen.onGuiClosed();
        }
    
        if (guiScreenIn == null && this.theWorld == null)
        {
            guiScreenIn = new GuiMainMenu();
        }
        else if (guiScreenIn == null && this.thePlayer.getHealth() <= 0.0F)
        {
            guiScreenIn = new GuiGameOver();
        }
    
        GuiScreen old = this.currentScreen;
        GuiOpenEvent event = new GuiOpenEvent(guiScreenIn);
    
        EventBus.INSTANCE.post(event);
        
        if (event.isCancelled()) return;
    
        guiScreenIn = event.getGui();
        if (old != null && guiScreenIn != old) {
            old.onGuiClosed();
        }
    
        if (guiScreenIn instanceof GuiMainMenu) {
            this.gameSettings.showDebugInfo = false;
            this.ingameGUI.getChatGUI().clearChatMessages();
        }
    
        this.currentScreen = guiScreenIn;
    
        if (guiScreenIn != null) {
            this.setIngameNotInFocus();
            ScaledResolution scaledresolution = new ScaledResolution(theMinecraft);
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            guiScreenIn.setWorldAndResolution(theMinecraft, i, j);
            this.skipRenderWorld = false;
        } else {
            this.mcSoundHandler.resumeSounds();
            this.setIngameFocus();
        }
    }
    
    @Shadow public abstract void setIngameNotInFocus();
    
    @Shadow public abstract void setIngameFocus();
}