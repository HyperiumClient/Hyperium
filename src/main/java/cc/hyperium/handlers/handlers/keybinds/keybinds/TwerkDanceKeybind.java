package cc.hyperium.handlers.handlers.keybinds.keybinds;

import cc.hyperium.Hyperium;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.packets.serverbound.ServerCrossDataPacket;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UUIDUtil;
import org.lwjgl.input.Keyboard;

public class TwerkDanceKeybind extends HyperiumBind {
    public TwerkDanceKeybind() {
        super("Twerk..", Keyboard.KEY_Y);
    }

    @Override
    public void onPress() {
        Hyperium.INSTANCE.getHandlers().getTwerkDance().getStates().put(UUIDUtil.getClientUUID(), System.currentTimeMillis());
        Hyperium.INSTANCE.getHandlers().getTwerkDance().startAnimation(UUIDUtil.getClientUUID());
        NettyClient client = NettyClient.getClient();
        if (client != null)
            client.write(ServerCrossDataPacket.build(new JsonHolder().put("type", "twerk_dance")));

    }
}
