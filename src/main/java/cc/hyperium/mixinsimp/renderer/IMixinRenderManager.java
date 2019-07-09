package cc.hyperium.mixinsimp.renderer;

import net.minecraft.client.renderer.entity.RenderPlayer;

import java.util.Map;

public interface IMixinRenderManager {
    double getPosX();

    double getPosY();

    double getPosZ();

    Map<String, RenderPlayer> getSkinMap();
}
