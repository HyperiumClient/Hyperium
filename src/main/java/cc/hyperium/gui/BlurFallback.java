package cc.hyperium.gui;

import cc.hyperium.config.Settings;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import me.semx11.autotip.util.ReflectionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Fallback for when GUIs don't disable blurring themselves
 */
public class BlurFallback {

    public boolean isLoadedShader = false;
    /**
     * Last screen known to to be displayed to this object
     */
    private GuiScreen lastKnownScreen;
    private boolean initialBlur = false;
    private boolean loadedBlur = false;

    @InvokeEvent
    private void onTick(TickEvent event) {
            Minecraft mc = Minecraft.getMinecraft();
            // Clear shaders on disable.
            if (mc != null && mc.entityRenderer != null) {
                if (!loadedBlur) {
                    // Loads blur setting for first time.
                    loadedBlur = true;
                    initialBlur = Settings.BLUR_GUI;
                }
                if (!Settings.BLUR_GUI && Settings.BLUR_GUI != initialBlur) {
                    // Blur was just disabled.
                    mc.addScheduledTask(() -> mc.entityRenderer.stopUseShader());
                }

                initialBlur = Settings.BLUR_GUI;
            }

            if (mc != null && Settings.BLUR_GUI && mc.entityRenderer != null && mc.entityRenderer.isShaderActive()) {
                GuiScreen currentScreen = mc.currentScreen;

                if (lastKnownScreen != currentScreen) {
                    if (currentScreen == null) { // Disable shaders if screen just closed
                        mc.addScheduledTask(() ->
                                mc.entityRenderer.stopUseShader());
                    }

                    lastKnownScreen = mc.currentScreen;
                }
            }

            //Enable shader fallback
            if (mc != null && Settings.BLUR_GUI && mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat) && mc.entityRenderer != null && !mc.entityRenderer.isShaderActive()) {
                Method loadShaderMethod = null;
                try {
                    loadShaderMethod = ReflectionUtil.findDeclaredMethod(EntityRenderer.class, new String[]{"loadShader", "a"}, ResourceLocation.class);
                } catch (Exception ignored) {
                }
                if (loadShaderMethod != null) {
                    loadShaderMethod.setAccessible(true);
                    try {
                        loadShaderMethod.invoke(Minecraft.getMinecraft().entityRenderer, new ResourceLocation("shaders/hyperium_blur.json"));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
    }
}
