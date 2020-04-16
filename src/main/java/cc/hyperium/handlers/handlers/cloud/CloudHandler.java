package cc.hyperium.handlers.handlers.cloud;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.client.TickEvent;
import net.minecraft.client.Minecraft;

public class CloudHandler {

  private final CloudRenderer cloudRenderer = new CloudRenderer();

  @InvokeEvent
  public void checkSettings(TickEvent event) {
    cloudRenderer.checkSettings();
  }

  public boolean renderClouds(int cloudTicks, float partialTicks) {
    IRenderHandler renderer = Minecraft.getMinecraft().theWorld.provider.getCloudRenderer();
    if (renderer != null) {
      renderer.render(partialTicks, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft());
      return true;
    }

    return cloudRenderer.render(cloudTicks, partialTicks);
  }

  public CloudRenderer getCloudRenderer() {
    return cloudRenderer;
  }
}
