package cc.hyperium.mixinsimp.packet;

import net.minecraft.client.network.NetHandlerPlayClient;

public class HyperiumNetHandlerPlayClient {

    private NetHandlerPlayClient parent;

    public HyperiumNetHandlerPlayClient(NetHandlerPlayClient parent) {
        this.parent = parent;
    }
}
