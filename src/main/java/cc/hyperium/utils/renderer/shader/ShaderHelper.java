package cc.hyperium.utils.renderer.shader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.ResourceLocation;

public class ShaderHelper {

  private EntityRenderer parent;
  public static ShaderHelper INSTANCE;

  public ShaderHelper(EntityRenderer parent) {
    this.parent = parent;
    INSTANCE = this;
  }

  public void loadShader(ResourceLocation resourceLocation) {
    if (resourceLocation.equals(new ResourceLocation("shaders/hyperium_blur.json")) && Minecraft.getMinecraft().currentScreen == null) {
      // If a gui is closed and we are asked
      // to blur, cancel it.
      return;
    }

    parent.loadShader(resourceLocation);
  }

  public void enableBlurShader() {
    loadShader(new ResourceLocation("shaders/hyperium_blur.json"));
  }

  public void disableBlurShader() {
    Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().entityRenderer.stopUseShader());
  }
}
