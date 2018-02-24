package cc.hyperium.mods.keystrokes;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.mods.keystrokes.config.KeystrokesSettings;
import cc.hyperium.mods.keystrokes.render.KeystrokesRenderer;
import cc.hyperium.mods.keystrokes.screen.GuiScreenKeystrokes;
import cc.hyperium.mods.keystrokes.utils.AntiReflection;
import cc.hyperium.mods.sk1ercommon.Sk1erMod;
import net.minecraft.client.Minecraft;

import java.io.File;

public class KeystrokesMod {

    public static final String MOD_ID = "keystrokesmod";
    public static final String MOD_NAME = "KeystrokesMod";
    public static final String VERSION = "4.1.1";

    @AntiReflection.HiddenField
    private static KeystrokesSettings settings;

    @AntiReflection.HiddenField
    private static KeystrokesRenderer renderer;

    @AntiReflection.HiddenField
    private static boolean openGui;

    public KeystrokesMod() {
    }

    @AntiReflection.HiddenMethod
    public static KeystrokesSettings getSettings() {
        return settings;
    }

    @AntiReflection.HiddenMethod
    public static KeystrokesRenderer getRenderer() {
        return renderer;
    }

    @AntiReflection.HiddenMethod
    public static void openGui() {
        openGui = true;
    }

    @AntiReflection.HiddenMethod
    public static File getMainDir() {
        return new File(".", "mods" + File.separatorChar + KeystrokesMod.MOD_ID + File.separatorChar);
    }


    public void init() {
        settings = new KeystrokesSettings("keystrokes");
        settings.load();

        new Sk1erMod(MOD_ID, VERSION, (jsonHolder) -> {
            //ignored
        }).checkStatus();
        renderer = new KeystrokesRenderer();

        EventBus.INSTANCE.register(renderer);
        EventBus.INSTANCE.register(this);

        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandKeystrokes());

        AntiReflection.filterMultipleClasses(getClass(), settings.getClass(), renderer.getClass());
    }

    @InvokeEvent
    public void onClientTick(TickEvent event) {
        if (openGui) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiScreenKeystrokes());
            openGui = false;
        }
    }
}
