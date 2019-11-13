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

package cc.hyperium.mixinsimp.client;

import cc.hyperium.Hyperium;
import cc.hyperium.Metadata;
import cc.hyperium.SplashProgress;
import cc.hyperium.config.Settings;
import cc.hyperium.event.*;
import cc.hyperium.event.client.InitializationEvent;
import cc.hyperium.event.client.PreInitializationEvent;
import cc.hyperium.event.client.TickEvent;
import cc.hyperium.event.gui.GuiOpenEvent;
import cc.hyperium.event.interact.*;
import cc.hyperium.event.network.server.SingleplayerJoinEvent;
import cc.hyperium.event.render.RenderPlayerEvent;
import cc.hyperium.event.world.WorldChangeEvent;
import cc.hyperium.event.world.WorldUnloadEvent;
import cc.hyperium.gui.GuiHyperiumScreenMainMenu;
import cc.hyperium.handlers.HyperiumHandlers;
import cc.hyperium.internal.addons.AddonBootstrap;
import cc.hyperium.internal.addons.AddonMinecraftBootstrap;
import cc.hyperium.internal.addons.IAddon;
import cc.hyperium.utils.AddonWorkspaceResourcePack;
import cc.hyperium.utils.Utils;
import cc.hyperium.utils.mods.FPSLimiter;
import com.chattriggers.ctjs.CTJS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.crash.CrashReport;
import net.minecraft.init.Bootstrap;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.Timer;
import net.minecraft.util.Util;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HyperiumMinecraft {

    private Minecraft parent;

    public HyperiumMinecraft(Minecraft parent) {
        this.parent = parent;
    }

    public void preinit(List<IResourcePack> defaultResourcePacks, DefaultResourcePack mcDefaultResourcePack) {
        Hyperium.INSTANCE.setLaunchTime(System.currentTimeMillis());
        EventBus.INSTANCE.register(Hyperium.INSTANCE);

        defaultResourcePacks.add(mcDefaultResourcePack);
        AddonBootstrap.getAddonResourcePacks().stream().map(file -> file == null ? new AddonWorkspaceResourcePack() :
            new FileResourcePack(file)).forEach(defaultResourcePacks::add);

        AddonMinecraftBootstrap.init();
        CTJS.loadIntoJVM();
        EventBus.INSTANCE.post(new PreInitializationEvent());
    }

    public void loop(boolean inGameHasFocus, WorldClient theWorld, EntityPlayerSP thePlayer, RenderManager renderManager, Timer timer) {
        if (inGameHasFocus && theWorld != null) {
            HyperiumHandlers handlers = Hyperium.INSTANCE.getHandlers();
            RenderPlayerEvent event = new RenderPlayerEvent(thePlayer, renderManager, renderManager.viewerPosZ, renderManager.viewerPosY, renderManager.viewerPosZ,
                timer.renderPartialTicks);
            if (handlers != null && Settings.SHOW_PART_1ST_PERSON) {
                handlers.getParticleAuraHandler().renderPlayer(event);
            }
        }
    }

    public void startGame() {
        EventBus.INSTANCE.post(new InitializationEvent());
    }

    public void runTick(Profiler mcProfiler) {
        mcProfiler.startSection("hyperium_tick");
        EventBus.INSTANCE.post(new TickEvent());
        mcProfiler.endSection();
    }

    public void runTickKeyboard() {
        int key = Keyboard.getEventKey();
        boolean repeat = Keyboard.isRepeatEvent();
        boolean press = Keyboard.getEventKeyState();
        EventBus.INSTANCE.post(press ? new KeyPressEvent(key, repeat) : new KeyReleaseEvent(key, repeat));
    }

    public void clickMouse() {
        EventBus.INSTANCE.post(new LeftMouseClickEvent());
    }

    public void rightClickMouse() {
        EventBus.INSTANCE.post(new RightMouseClickEvent());
    }

    public void launchIntegratedServer() {
        EventBus.INSTANCE.post(new SingleplayerJoinEvent());
    }

    public void displayFix(CallbackInfo ci, boolean fullscreen, int displayWidth, int displayHeight)
        throws LWJGLException {
        Display.setFullscreen(false);
        if (fullscreen) {
            if (Settings.WINDOWED_FULLSCREEN) {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
            } else {
                Display.setFullscreen(true);
                DisplayMode displaymode = Display.getDisplayMode();
                parent.displayWidth = Math.max(1, displaymode.getWidth());
                parent.displayHeight = Math.max(1, displaymode.getHeight());
            }
        } else {
            if (Settings.WINDOWED_FULLSCREEN) {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
            } else {
                Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
            }
        }

        Display.setResizable(false);
        Display.setResizable(true);

        // effectively overwrites the method
        ci.cancel();
    }

    public void fullScreenFix(boolean fullscreen, int displayWidth, int displayHeight) throws LWJGLException {
        if (Settings.WINDOWED_FULLSCREEN) {
            if (fullscreen) {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
                Display.setDisplayMode(Display.getDesktopDisplayMode());
                Display.setLocation(0, 0);
                Display.setFullscreen(false);
            } else {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
                Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
            }
        } else {
            Display.setFullscreen(fullscreen);
            System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
        }

        Display.setResizable(false);
        Display.setResizable(true);
    }

    public void setWindowIcon() {
        if (Util.getOSType() != Util.EnumOS.OSX) {
            try (InputStream inputStream16x = Minecraft.class.getResourceAsStream("/assets/hyperium/icons/icon-16x.png");
                 InputStream inputStream32x = Minecraft.class.getResourceAsStream("/assets/hyperium/icons/icon-32x.png")) {
                ByteBuffer[] icons = new ByteBuffer[]{Utils.INSTANCE.readImageToBuffer(inputStream16x), Utils.INSTANCE.readImageToBuffer(inputStream32x)};
                Display.setIcon(icons);
            } catch (Exception e) {
                Hyperium.LOGGER.error("Couldn't set Windows Icon", e);
            }
        }
    }

    public void displayGuiScreen(GuiScreen guiScreenIn, GuiScreen currentScreen, WorldClient theWorld, EntityPlayerSP thePlayer, GameSettings gameSettings, GuiIngame ingameGUI) {
        if (currentScreen != null) {
            currentScreen.onGuiClosed();
        }

        if (guiScreenIn == null && theWorld == null) {
            guiScreenIn = new GuiHyperiumScreenMainMenu();
        } else if (guiScreenIn == null && thePlayer.getHealth() <= 0.0F) {
            guiScreenIn = new GuiGameOver();
        }

        GuiOpenEvent event = new GuiOpenEvent(guiScreenIn);
        EventBus.INSTANCE.post(event);

        if (event.isCancelled()) return;

        guiScreenIn = event.getGui();
        if (currentScreen != null && guiScreenIn != currentScreen) currentScreen.onGuiClosed();
        if (currentScreen != null) EventBus.INSTANCE.unregister(currentScreen);

        if (guiScreenIn instanceof GuiHyperiumScreenMainMenu) {
            gameSettings.showDebugInfo = false;

            if (!Settings.PERSISTENT_CHAT) {
                ingameGUI.getChatGUI().clearChatMessages();
            }
        }

        parent.currentScreen = guiScreenIn;

        if (guiScreenIn != null) {
            parent.setIngameNotInFocus();
            ScaledResolution scaledresolution = new ScaledResolution(parent);
            int scaledWidth = scaledresolution.getScaledWidth();
            int scaledHeight = scaledresolution.getScaledHeight();
            guiScreenIn.setWorldAndResolution(parent, scaledWidth, scaledHeight);
            parent.skipRenderWorld = false;
        } else {
            parent.getSoundHandler().resumeSounds();
            parent.setIngameFocus();
        }

        if (Hyperium.INSTANCE.getHandlers() != null) {
            Hyperium.INSTANCE.getHandlers().getKeybindHandler().releaseAllKeybinds();
        }
    }

    public void getLimitFramerate(CallbackInfoReturnable<Integer> ci) {
        if (FPSLimiter.shouldLimitFramerate()) ci.setReturnValue(FPSLimiter.getInstance().getFpsLimit());
    }

    public void onStartGame() {
        //ToDo Allow the usage of I18n formatting
        SplashProgress.setProgress(1, "Starting Game...");
    }

    public void onLoadDefaultResourcePack() {
        //ToDo Allow the usage of I18n formatting
        SplashProgress.setProgress(2, "Loading Resources...");
    }

    public void onCreateDisplay() {
        //ToDo Allow the usage of I18n formatting
        SplashProgress.setProgress(3, "Creating Display...");
    }

    public void onLoadTexture() {
        //ToDo Allow the usage of I18n formatting
        SplashProgress.setProgress(4, "Initializing Textures...");
    }

    public void loadWorld() {
        if (Minecraft.getMinecraft().theWorld != null) new WorldUnloadEvent().post();
        EventBus.INSTANCE.post(new WorldChangeEvent());
    }

    public void runTickMouseButton() {
        // Activates for EVERY mouse button.
        int i = Mouse.getEventButton();
        boolean state = Mouse.getEventButtonState();
        EventBus.INSTANCE.post(state ? new MouseButtonEvent(i, true) : new MouseButtonEvent(i, false));
    }

    public void displayCrashReport(CrashReport crashReportIn) {
        // Separate Hyperium crash reports.
        File crashReportDir;
        crashReportDir = new File(Minecraft.getMinecraft().mcDataDir, "crash-reports");

        File crashReportFile = new File(crashReportDir,
            "hyperium-crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date())
                + "-client-version-" + Metadata.getVersion().replace(" ", "-") + ".txt");

        crashReportIn.saveToFile(crashReportFile);
        Bootstrap.printToSYSOUT(crashReportIn.getCompleteReport());

        if (crashReportIn.getFile() != null) {
            Bootstrap.printToSYSOUT(
                "#@!@# Game crashed! Crash report saved to: #@!@# " + crashReportIn.getFile());
        } else if (crashReportIn.saveToFile(crashReportFile)) {
            Bootstrap.printToSYSOUT(
                "#@!@# Game crashed! Crash report saved to: #@!@# " + crashReportFile.getAbsolutePath());
        } else {
            Bootstrap.printToSYSOUT("#@?@# Game crashed! Crash report could not be saved. #@?@#");
        }
    }

    public void shutdown() {
        AddonMinecraftBootstrap.getLoadedAddons().forEach(IAddon::onClose);
    }
}
