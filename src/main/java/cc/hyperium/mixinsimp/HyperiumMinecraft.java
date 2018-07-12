package cc.hyperium.mixinsimp;

import cc.hyperium.Hyperium;
import cc.hyperium.SplashProgress;
import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.GuiOpenEvent;
import cc.hyperium.event.InitializationEvent;
import cc.hyperium.event.KeypressEvent;
import cc.hyperium.event.KeyreleaseEvent;
import cc.hyperium.event.LeftMouseClickEvent;
import cc.hyperium.event.MouseButtonEvent;
import cc.hyperium.event.PreInitializationEvent;
import cc.hyperium.event.RenderPlayerEvent;
import cc.hyperium.event.RightMouseClickEvent;
import cc.hyperium.event.SingleplayerJoinEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.event.WorldChangeEvent;
import cc.hyperium.gui.CrashReportGUI;
import cc.hyperium.gui.HyperiumMainMenu;
import cc.hyperium.handlers.HyperiumHandlers;
import cc.hyperium.internal.addons.AddonBootstrap;
import cc.hyperium.internal.addons.AddonMinecraftBootstrap;
import cc.hyperium.internal.addons.IAddon;
import cc.hyperium.mixins.IMixinMinecraft;
import cc.hyperium.utils.AddonWorkspaceResourcePack;
import cc.hyperium.utils.Utils;
import cc.hyperium.utils.mods.FPSLimiter;
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
import net.minecraft.world.WorldSettings;
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

    public void preinit(CallbackInfo ci, List<IResourcePack> defaultResourcePacks, DefaultResourcePack mcDefaultResourcePack, List<IResourcePack> resourcePacks) {
        defaultResourcePacks.add(mcDefaultResourcePack);
        for (File file : AddonBootstrap.getAddonResourcePacks()) {
            defaultResourcePacks.add(file == null ? new AddonWorkspaceResourcePack() : new FileResourcePack(file));
        }
        AddonMinecraftBootstrap.init();
        EventBus.INSTANCE.post(new PreInitializationEvent());
    }

    public void loop(CallbackInfo info, boolean inGameHasFocus, WorldClient theWorld, EntityPlayerSP thePlayer, RenderManager renderManager, Timer timer) {
        if (inGameHasFocus && theWorld != null && Settings.SHOW_PART_1ST_PERSON) {
            HyperiumHandlers handlers = Hyperium.INSTANCE.getHandlers();
            if (handlers != null) {

                handlers.getParticleAuraHandler().renderPlayer(new RenderPlayerEvent(thePlayer, renderManager, 0, 0, 0, timer.renderPartialTicks));
            }
        }
    }

    public void startGame(CallbackInfo info) {
        EventBus.INSTANCE.register(Hyperium.INSTANCE);
        EventBus.INSTANCE.post(new InitializationEvent());
    }

    public void runTick(CallbackInfo ci, Profiler mcProfiler) {
        mcProfiler.startSection("hyperium_tick");
        EventBus.INSTANCE.post(new TickEvent());
        mcProfiler.endSection();
    }

    public void runTickKeyboard(CallbackInfo ci) {
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

    public void clickMouse(CallbackInfo ci) {
        EventBus.INSTANCE.post(new LeftMouseClickEvent());
    }

    public void rightClickMouse(CallbackInfo ci) {
        EventBus.INSTANCE.post(new RightMouseClickEvent());
    }

    public void launchIntegratedServer(String folderName, String worldName, WorldSettings worldSettingsIn, CallbackInfo ci) {
        EventBus.INSTANCE.post(new SingleplayerJoinEvent());
    }

    public void displayFix(CallbackInfo ci, boolean fullscreen, int displayWidth, int displayHeight) throws LWJGLException {
        Display.setFullscreen(false);
        if (fullscreen) {
            if (Settings.WINDOWED_FULLSCREEN) {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
            } else {
                Display.setFullscreen(true);
                DisplayMode displaymode = Display.getDisplayMode();
                ((IMixinMinecraft) parent).setDisplayWidth(Math.max(1, displaymode.getWidth()));
                ((IMixinMinecraft) parent).setDisplayHeight(Math.max(1, displaymode.getHeight()));
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

    public void fullScreenFix(CallbackInfo ci, boolean fullscreen, int displayWidth, int displayHeight) throws LWJGLException {
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
                ByteBuffer[] icons = new ByteBuffer[]{Utils.INSTANCE.readImageToBuffer(inputStream16x),
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
            guiScreenIn = new HyperiumMainMenu();
        } else if (guiScreenIn == null && thePlayer.getHealth() <= 0.0F) {
            guiScreenIn = new GuiGameOver();
        }

        GuiScreen old = currentScreen;

        GuiOpenEvent event = new GuiOpenEvent(guiScreenIn);

        EventBus.INSTANCE.post(event);

        if (event.isCancelled()) return;

        guiScreenIn = event.getGui();
        if (old != null && guiScreenIn != old) {
            old.onGuiClosed();
        }
        if (old != null)
            EventBus.INSTANCE.unregister(old);

        if (guiScreenIn instanceof HyperiumMainMenu) {
            gameSettings.showDebugInfo = false;
            if (!Settings.PERSISTENT_CHAT)
                ingameGUI.getChatGUI().clearChatMessages();
        }

        ((IMixinMinecraft) parent).setCurrentScreen(guiScreenIn);

        if (guiScreenIn != null) {
            parent.setIngameNotInFocus();
            ScaledResolution scaledresolution = new ScaledResolution(parent);
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            guiScreenIn.setWorldAndResolution(parent, i, j);
            parent.skipRenderWorld = false;
        } else {
            parent.getSoundHandler().resumeSounds();
            parent.setIngameFocus();
        }
    }

    public void getLimitFramerate(CallbackInfoReturnable<Integer> ci) {
        if (FPSLimiter.shouldLimitFramerate()) {
            ci.setReturnValue(FPSLimiter.getInstance().getFpsLimit());
        }
    }

    public void onStartGame(CallbackInfo ci) {
        SplashProgress.PROGRESS = 1;
        SplashProgress.CURRENT = "Starting game";
        SplashProgress.update();
    }

    public void onLoadDefaultResourcePack(CallbackInfo ci) {
        SplashProgress.PROGRESS = 2;
        SplashProgress.CURRENT = "Loading resource";
        SplashProgress.update();
    }

    public void onCreateDisplay(CallbackInfo ci) {
        SplashProgress.PROGRESS = 3;
        SplashProgress.CURRENT = "Creating display";
        SplashProgress.update();
    }

    public void onLoadTexture(CallbackInfo ci) {
        SplashProgress.PROGRESS = 4;
        SplashProgress.CURRENT = "Initializing textures";
        SplashProgress.update();
    }

    public void loadWorld(WorldClient worldClient, CallbackInfo ci) {
        EventBus.INSTANCE.post(new WorldChangeEvent());
    }

    public void runTickMouseButton(CallbackInfo ci) {
        // Actiavtes for EVERY mouse button.
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
        File file1 = new File(Minecraft.getMinecraft().mcDataDir, "crash-reports");
        File file2 = new File(file1, "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-client.txt");
        Bootstrap.printToSYSOUT(crashReportIn.getCompleteReport());

        try {
            Display.setFullscreen(false);
            Display.setDisplayMode(new DisplayMode(720, 480));
            Display.update();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        int x = CrashReportGUI.handle(crashReportIn);

        if (crashReportIn.getFile() != null)
            Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + crashReportIn.getFile());
        else if (crashReportIn.saveToFile(file2))
            Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + file2.getAbsolutePath());
        else
            Bootstrap.printToSYSOUT("#@?@# Game crashed! Crash report could not be saved. #@?@#");

        switch (x) {
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
                    StringBuilder cmd = new StringBuilder();
                    String[] command = System.getProperty("sun.java.command").split(" ");
                    cmd.append(System.getProperty("java.home")).append(File.separator).append("bin").append(File.separator).append("java ");
                    ManagementFactory.getRuntimeMXBean().getInputArguments().forEach(s -> {
                        if (!s.contains("-agentlib"))
                            cmd.append(s).append(" ");
                    });
                    if (command[0].endsWith(".jar"))
                        cmd.append("-jar ").append(new File(command[0]).getPath()).append(" ");
                    else
                        cmd.append("-cp \"").append(System.getProperty("java.class.path")).append("\" ").append(command[0]).append(" ");
                    for (int i = 1; i < command.length; i++)
                        cmd.append(command[i]).append(" ");
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
    public void shutdown(CallbackInfo ci) {
        AddonMinecraftBootstrap.getLoadedAddons().forEach(IAddon::onClose);
    }


}
