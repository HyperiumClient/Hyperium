package cc.hyperium.mixinsimp.renderer.gui;

import net.minecraft.client.multiplayer.ServerData;

public interface IMixinGuiMultiplayer {

    void makeDirectConnect();

    void setIp(ServerData ip);
}
