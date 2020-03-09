package cc.hyperium.debug;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.render.RenderHUDEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;

public class HyperiumDebug {

    /**
     * Usable via adding -DhyperiumDebug=true to vm arguments.
     */
    private final boolean debugHyperiumArgument = Boolean.parseBoolean(System.getProperty("hyperiumDebug", "false"));
    private final Minecraft mc = Minecraft.getMinecraft();

    @InvokeEvent
    public void renderDebugOverlay(RenderHUDEvent event) {
        if (!debugHyperiumArgument) {
            EventBus.INSTANCE.unregister(this);
            return;
        }

        if (mc.gameSettings.showDebugInfo) return;

        int mbDiv = 1048576;
        long maxMemory = Runtime.getRuntime().maxMemory() / mbDiv;
        long totalMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / mbDiv;
        mc.fontRendererObj.drawStringWithShadow("fps: " + Minecraft.getDebugFPS(), 3, 3, -1);
        mc.fontRendererObj.drawStringWithShadow("memory usage: " + totalMemory + " / " + maxMemory + "MB", 3, 13, -1);
        mc.fontRendererObj.drawStringWithShadow("supported opengl ver: " + GL11.glGetString(GL11.GL_VERSION), 3, 23, -1);
        mc.fontRendererObj.drawStringWithShadow("cpu: " + OpenGlHelper.getCpu(), 3, 33, -1);
        mc.fontRendererObj.drawStringWithShadow("gpu: " + GL11.glGetString(GL11.GL_RENDERER), 3, 43, -1);
        mc.fontRendererObj.drawStringWithShadow("operating system: " + System.getProperty("os.name"), 3, 53, -1);
        mc.fontRendererObj.drawStringWithShadow("architecture type: " + System.getProperty("os.arch"), 3, 63, -1);
    }
}
