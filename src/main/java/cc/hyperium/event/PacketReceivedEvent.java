package cc.hyperium.event;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;

/**
 * Invoked once a packet is received by the client, right before it is processed
 */
public class PacketReceivedEvent extends Event {

    private final Packet<INetHandler> packet;

    public PacketReceivedEvent(Packet<INetHandler> packet) {
        this.packet = packet;
    }

    public Packet<INetHandler> getPacket() {
        return this.packet;
    }
}
