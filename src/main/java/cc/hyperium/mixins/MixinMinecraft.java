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

package cc.hyperium.mixins;

import cc.hyperium.Metadata;
import cc.hyperium.SplashProgress;
import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.RenderTickEvent;
import cc.hyperium.event.WorldLoadEvent;
import cc.hyperium.mixinsimp.HyperiumMinecraft;
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.crash.CrashReport;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.Timer;
import net.minecraft.world.WorldSettings;
import org.lwjgl.LWJGLException;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.util.List;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow
    public FontRenderer fontRendererObj;

    @Shadow
    @Final
    public Profiler mcProfiler;

    @Shadow
    public boolean inGameHasFocus;

    @Shadow
    public GuiAchievement guiAchievement;

    @Shadow
    public int displayHeight;

    @Shadow
    public int displayWidth;

    @Shadow
    public GuiScreen currentScreen;

    @Shadow
    public WorldClient theWorld;

    @Shadow
    public EntityPlayerSP thePlayer;

    @Shadow
    public GameSettings gameSettings;

    @Shadow
    public GuiIngame ingameGUI;

    @Shadow
    private boolean fullscreen;

    @Shadow
    @Final
    private DefaultResourcePack mcDefaultResourcePack;

    @Shadow
    @Final
    private List<IResourcePack> defaultResourcePacks;

    @Shadow
    private boolean enableGLErrorChecking;

    private HyperiumMinecraft hyperiumMinecraft = new HyperiumMinecraft((Minecraft) (Object) this);

    @Shadow
    private Timer timer;

    @Shadow
    private RenderManager renderManager;

    @Shadow
    long systemTime;

    @Final
    @Shadow
    public File mcDataDir;

    @Shadow
    private Framebuffer framebufferMc;

    protected MixinMinecraft() {
    }

    /**
     * Invoked once the game is launching
     *
     * @param ci {@see org.spongepowered.asm.mixin.injection.callback.CallbackInfo}
     */
    @Inject(method = "startGame", at = @At("HEAD"))
    private void preinit(CallbackInfo ci) {
        hyperiumMinecraft.preinit(ci, defaultResourcePacks, mcDefaultResourcePack, defaultResourcePacks);
    }

    @Inject(method = "runGameLoop", at = @At("HEAD"))
    public void loop(CallbackInfo info) {
        hyperiumMinecraft.loop(info, inGameHasFocus, theWorld, thePlayer, renderManager, timer);
    }

    /**
     * Invoked once the game has be launched
     *
     * @param ci {@see org.spongepowered.asm.mixin.injection.callback.CallbackInfo}
     */
    @Inject(method = "startGame", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        //Accessor not needed since its only set once
        enableGLErrorChecking = Metadata.isDevelopment();
        hyperiumMinecraft.startGame(ci);
    }

    /**
     * Invoked every tick (every 50milliseconds)
     *
     * @param ci {@see org.spongepowered.asm.mixin.injection.callback.CallbackInfo}
     */
    @Inject(method = "runTick", at = @At("RETURN"))
    private void runTick(CallbackInfo ci) {
        hyperiumMinecraft.runTick(ci, mcProfiler);
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    private void tick(CallbackInfo info) {
        hyperiumMinecraft.startTick(info, mcProfiler);
    }

    /**
     * Invoked once the player has pressed a key
     *
     * @param ci {@see org.spongepowered.asm.mixin.injection.callback.CallbackInfo}
     */
    @Inject(method = "dispatchKeypresses", at = @At(value = "INVOKE_ASSIGN", target = "Lorg/lwjgl/input/Keyboard;getEventKeyState()Z"))
    private void runTickKeyboard(CallbackInfo ci) {
        hyperiumMinecraft.runTickKeyboard(ci);
    }

    @Inject(
        method = "dispatchKeypresses",
        at = @At(
            value = "INVOKE",
            shift = At.Shift.BEFORE,
            target = "Lnet/minecraft/client/gui/GuiNewChat;printChatMessage(Lnet/minecraft/util/IChatComponent;)V",
            ordinal = 1
        ),
        cancellable = true
    )
    private void dispatchKeypresses(CallbackInfo ci) {
        IChatComponent chatComponent = ScreenShotHelper.saveScreenshot(this.mcDataDir, this.displayWidth, this.displayHeight, this.framebufferMc);

        if (chatComponent != null) {
            new TextComponent(chatComponent).chat();
        }

        ci.cancel();
    }

    /**
     * Invoked once the player has pressed mouse button 1
     *
     * @param ci {@see org.spongepowered.asm.mixin.injection.callback.CallbackInfo}
     */
    @Inject(method = "clickMouse", at = @At("RETURN"))
    private void clickMouse(CallbackInfo ci) {
        hyperiumMinecraft.clickMouse(ci);
    }

    /**
     * Invoked once the player has pressed mouse button 1
     *
     * @param ci {@see org.spongepowered.asm.mixin.injection.callback.CallbackInfo}
     */
    @Inject(method = "rightClickMouse", at = @At("RETURN"))
    private void rightClickMouse(CallbackInfo ci) {
        hyperiumMinecraft.rightClickMouse(ci);
    }

    /**
     * Invoked once the player has joined a singleplayer world
     *
     * @param ci {@see org.spongepowered.asm.mixin.injection.callback.CallbackInfo}
     */
    @Inject(method = "launchIntegratedServer", at = @At("HEAD"))
    private void launchIntegratedServer(String folderName, String worldName, WorldSettings worldSettingsIn, CallbackInfo ci) {
        hyperiumMinecraft.launchIntegratedServer(folderName, worldName, worldSettingsIn, ci);
    }

    /**
     * Fixes bug MC-68754 and MC-111254
     *
     * @param ci
     * @author Kevin Brewster
     */
    @Inject(method = "setInitialDisplayMode", at = @At(value = "HEAD"), cancellable = true)
    private void displayFix(CallbackInfo ci) throws LWJGLException {
        hyperiumMinecraft.displayFix(ci, fullscreen, displayWidth, displayHeight);
    }

    @Inject(method = "toggleFullscreen", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;setVSyncEnabled(Z)V", shift = At.Shift.AFTER))
    private void fullScreenFix(CallbackInfo ci) throws LWJGLException {
        hyperiumMinecraft.fullScreenFix(ci, fullscreen, displayWidth, displayHeight);
    }

    /**
     * @author Cubxity
     * @reason Set Minecraft icon to Hyperium icon
     */
    @Overwrite
    private void setWindowIcon() {
        hyperiumMinecraft.setWindowIcon();
    }

    /**
     * @author boomboompower
     * @reason GuiOpenEvent post
     */
    @Overwrite
    public void displayGuiScreen(GuiScreen guiScreenIn) {
        hyperiumMinecraft.displayGuiScreen(guiScreenIn, currentScreen, theWorld, thePlayer, gameSettings, ingameGUI);
    }

    @Inject(method = "getLimitFramerate", at = @At("HEAD"), cancellable = true)
    private void getLimitFramerate(CallbackInfoReturnable<Integer> ci) {
        hyperiumMinecraft.getLimitFramerate(ci);
    }

    @Shadow
    public abstract void shutdown();

    @Shadow
    public abstract void run();

    @Shadow
    public EffectRenderer effectRenderer;

    /**
     * @author Cubxity
     * @reason Change splash screen
     */
    @Overwrite
    private void drawSplashScreen(TextureManager tm) {
        SplashProgress.drawSplash(tm);
    }


    @Inject(method = "startGame", at = @At("HEAD"))
    private void onStartGame(CallbackInfo ci) {
        hyperiumMinecraft.onStartGame(ci);
    }

    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "java/util/List.add(Ljava/lang/Object;)Z", shift = At.Shift.BEFORE))
    private void onLoadDefaultResourcePack(CallbackInfo ci) {
        hyperiumMinecraft.onLoadDefaultResourcePack(ci);
    }

    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "net/minecraft/client/Minecraft.createDisplay()V", shift = At.Shift.BEFORE))
    private void onCreateDisplay(CallbackInfo ci) {
        hyperiumMinecraft.onCreateDisplay(ci);
    }

    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/OpenGlHelper.initializeTextures()V", shift = At.Shift.BEFORE))
    private void onLoadTexture(CallbackInfo ci) {
        hyperiumMinecraft.onLoadTexture(ci);
    }

    @Inject(method = "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;)V", at = @At("HEAD"))
    private void loadWorld(WorldClient worldClient, CallbackInfo ci) {
        hyperiumMinecraft.loadWorld(worldClient, ci);
    }

    @Inject(method = "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V", at = @At(value = "INVOKE", target = "Ljava/lang/System;gc()V"), cancellable = true)
    public void fixGarbageCollection(WorldClient worldClientIn, String loadingMessage, CallbackInfo info) {
        new WorldLoadEvent().post();
        if (!Settings.FAST_WORLD_LOADING)
            return;
        systemTime = 0;
        info.cancel();
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Mouse;getEventButton()I", ordinal = 0))
    private void runTickMouseButton(CallbackInfo ci) {
        hyperiumMinecraft.runTickMouseButton(ci);
    }

    /**
     * @author Mojang & Cubxity
     * @reason Hyperium's crash-report screen
     */
    @Overwrite
    public void displayCrashReport(CrashReport crashReportIn) {
        hyperiumMinecraft.displayCrashReport(crashReportIn);
    }


    @Inject(method = "shutdown", at = @At("HEAD"))
    private void shutdown(CallbackInfo ci) {
        hyperiumMinecraft.shutdown(ci);
    }

    @Inject(method = "runGameLoop", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;skipRenderWorld:Z", shift = At.Shift.AFTER))
    private void runGameLoop(CallbackInfo callbackInfo) {
        EventBus.INSTANCE.post(new RenderTickEvent());
    }
}
