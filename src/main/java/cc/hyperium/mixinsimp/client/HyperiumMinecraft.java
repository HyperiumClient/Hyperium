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
import cc.hyperium.gui.CrashReportGUI;
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
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
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
        EventBus.INSTANCE.register(Hyperium.INSTANCE);

        defaultResourcePacks.add(mcDefaultResourcePack);
        for (File file : AddonBootstrap.getAddonResourcePacks()) {
            defaultResourcePacks.add(file == null ? new AddonWorkspaceResourcePack() : new FileResourcePack(file));
        }

        AddonMinecraftBootstrap.init();
        CTJS.loadIntoJVM();
        EventBus.INSTANCE.post(new PreInitializationEvent());
    }

    public void loop(boolean inGameHasFocus, WorldClient theWorld, EntityPlayerSP thePlayer, RenderManager renderManager, Timer timer) {
        if (inGameHasFocus && theWorld != null) {
            HyperiumHandlers handlers = Hyperium.INSTANCE.getHandlers();
            RenderPlayerEvent event = new RenderPlayerEvent(thePlayer, renderManager, renderManager.viewerPosZ, renderManager.viewerPosY, renderManager.viewerPosZ,
                timer.renderPartialTicks);
            if (handlers != null) {
                if (Settings.SHOW_PART_1ST_PERSON)
                    handlers.getParticleAuraHandler().renderPlayer(
                        event);
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

        if (press) {
            // Key has been pressed.
            EventBus.INSTANCE.post(new KeypressEvent(key, repeat));
        } else {
            // Key has been released.
            EventBus.INSTANCE.post(new KeyreleaseEvent(key, repeat));
        }
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
            try (InputStream inputStream16x = Minecraft.class
                .getResourceAsStream("/assets/hyperium/icons/icon-16x.png");
                 InputStream inputStream32x = Minecraft.class
                     .getResourceAsStream("/assets/hyperium/icons/icon-32x.png")) {
                ByteBuffer[] icons = new ByteBuffer[]{
                    Utils.INSTANCE.readImageToBuffer(inputStream16x),
                    Utils.INSTANCE.readImageToBuffer(inputStream32x)};
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

        if (event.isCancelled()) {
            return;
        }

        guiScreenIn = event.getGui();
        if (currentScreen != null && guiScreenIn != currentScreen) {
            currentScreen.onGuiClosed();
        }
        if (currentScreen != null) {
            EventBus.INSTANCE.unregister(currentScreen);
        }

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
        if (FPSLimiter.shouldLimitFramerate()) {
            ci.setReturnValue(FPSLimiter.getInstance().getFpsLimit());
        }
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
        if (Minecraft.getMinecraft().theWorld != null) {
            new WorldUnloadEvent().post();
        }

        EventBus.INSTANCE.post(new WorldChangeEvent());
    }

    public void runTickMouseButton() {
        // Activates for EVERY mouse button.
        int i = Mouse.getEventButton();
        boolean state = Mouse.getEventButtonState();
        if (state) {
            // Mouse clicked.
            EventBus.INSTANCE.post(new MouseButtonEvent(i, true));
        } else {
            // Mouse released.
            EventBus.INSTANCE.post(new MouseButtonEvent(i, false));
        }
    }

    public void displayCrashReport(CrashReport crashReportIn) {
        // Separate Hyperium crash reports.
        File crashReportDir;
        crashReportDir = new File(Minecraft.getMinecraft().mcDataDir, "crash-reports");

        File crashReportFile = new File(crashReportDir,
            "hyperium-crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date())
                + "-client-version" + Metadata.getVersion() + ".txt");

        crashReportIn.saveToFile(crashReportFile);
        Bootstrap.printToSYSOUT(crashReportIn.getCompleteReport());

        try {
            Display.setFullscreen(false);
            Display.setDisplayMode(new DisplayMode(720, 480));
            Display.update();
        } catch (LWJGLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Display not created yet. This is going to cause issues.");
        }

        // Intercept the crash with Hyperium crash report GUI.
        int crashAction = CrashReportGUI.handle(crashReportIn);

        if (crashReportIn.getFile() != null) {
            Bootstrap.printToSYSOUT(
                "#@!@# Game crashed! Crash report saved to: #@!@# " + crashReportIn.getFile());
        } else if (crashReportIn.saveToFile(crashReportFile)) {
            Bootstrap.printToSYSOUT(
                "#@!@# Game crashed! Crash report saved to: #@!@# " + crashReportFile.getAbsolutePath());
        } else {
            Bootstrap.printToSYSOUT("#@?@# Game crashed! Crash report could not be saved. #@?@#");
        }

        switch (crashAction) {
            case 0:
                System.exit(-1);
                break;
            case 1:
                try {
                    parent.shutdown();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.exit(-1); // if minecraft could not exit normally
                }
                break;
            case 2:
                try {
                    // Restart the client using the command line.
                    StringBuilder cmd = new StringBuilder();
                    String[] command = System.getProperty("sun.java.command").split(" ");
                    cmd.append(System.getProperty("java.home")).append(File.separator).append("bin")
                        .append(File.separator).append("java ");
                    ManagementFactory.getRuntimeMXBean().getInputArguments().forEach(s -> {
                        if (!s.contains("-agentlib")) {
                            cmd.append(s).append(" ");
                        }
                    });
                    if (command[0].endsWith(".jar")) {
                        cmd.append("-jar ").append(new File(command[0]).getPath()).append(" ");
                    } else {
                        cmd.append("-cp \"").append(System.getProperty("java.class.path"))
                            .append("\" ").append(command[0]).append(" ");
                    }
                    for (int i = 1; i < command.length; i++) {
                        cmd.append(command[i]).append(" ");
                    }
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        try {
                            System.out.println("## RESTARTING MINECRAFT ##");
                            System.out.println("cmd=" + cmd.toString());
                            Runtime.getRuntime().exec(cmd.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("## FAILED TO RESTART MINECRAFT ##");
                        }
                    }));
                    parent.shutdown();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("## FAILED TO RESTART MINECRAFT ##");
                }
                break;
        }
    }

    public void shutdown() {
        AddonMinecraftBootstrap.getLoadedAddons().forEach(IAddon::onClose);
    }
}
