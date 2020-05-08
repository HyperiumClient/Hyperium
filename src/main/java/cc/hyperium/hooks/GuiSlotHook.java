package cc.hyperium.hooks;

import cc.hyperium.config.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiSlotHook {

  private static final Minecraft mc = Minecraft.getMinecraft();

  public static void overlayBackground(int startY, int endY) {
    GlStateManager.color(1f, 1f, 1f, 1f);
    ScaledResolution resolution = new ScaledResolution(mc);
    String background = Settings.BACKGROUND;

    if (background.equals("CUSTOM")) {
      background = "1";
    }

    mc.getTextureManager().bindTexture(new ResourceLocation("hyperium", "textures/material/backgrounds/" + background + ".png"));
    int height = endY - startY;
    Gui.drawScaledCustomSizeModalRect(0,
        startY,
        0,
        startY,
        resolution.getScaledWidth(),
        height,
        resolution.getScaledWidth(),
        height,
        resolution.getScaledWidth(),
        resolution.getScaledHeight()
    );
  }
}
