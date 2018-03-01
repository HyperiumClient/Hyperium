package cc.hyperium.handlers.handlers;

import cc.hyperium.Metadata;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.JoinHypixelEvent;
import cc.hyperium.utils.JsonHolder;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class HyperiumNetwork {


    @InvokeEvent
    public void joinHypixel(JoinHypixelEvent event) {
        Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C17PacketCustomPayload("hyperium", new PacketBuffer(Unpooled.buffer()).writeString(new JsonHolder()
                .put("id", Metadata.getModid())
                .put("optifine", Metadata.isUsingOptifine())
                .put("forge", Metadata.isUsingForge())
                .put("version", Metadata.getVersion()).toString())));
    }

}
