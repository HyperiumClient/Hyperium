package cc.hyperium.handlers.handlers.cloud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;

public interface IRenderHandler {

  void render(float partialTicks, WorldClient worldClient, Minecraft mc);
}
