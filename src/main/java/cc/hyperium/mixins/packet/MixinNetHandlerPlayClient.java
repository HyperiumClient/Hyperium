package cc.hyperium.mixins.packet;

import cc.hyperium.Hyperium;
import cc.hyperium.mods.timechanger.TimeChanger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerPlayClient {
    
    @Shadow
    private Minecraft gameController;
    
    private TimeChanger timeChanger = (TimeChanger) Hyperium.INSTANCE.getModIntegration().getTimeChanger();
    
    /**
     * For TimeChanger, changes the way time packets are handled
     *
     * @author boomboompower
     */
    @Overwrite
    public void handleTimeUpdate(S03PacketTimeUpdate packet) {
        switch (this.timeChanger.getTimeType()) {
            case DAY:
                handleActualPacket(new S03PacketTimeUpdate(packet.getWorldTime(), -6000L, true));
                break;
            case SUNSET:
                handleActualPacket(new S03PacketTimeUpdate(packet.getWorldTime(), -22880L, true));
                break;
            case NIGHT:
                handleActualPacket(new S03PacketTimeUpdate(packet.getWorldTime(), -18000L, true));
                break;
            case VANILLA:
                handleActualPacket(packet);
                break;
        }
    }
    
    /**
     * The actual logic of the packet, may be spoofed.
     *
     * @param packetIn the packet
     */
    private void handleActualPacket(S03PacketTimeUpdate packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn,
            (INetHandlerPlayClient) getNetworkManager().getNetHandler(), this.gameController);
        this.gameController.theWorld.setTotalWorldTime(packetIn.getTotalWorldTime());
        this.gameController.theWorld.setWorldTime(packetIn.getWorldTime());
    }
    
    @Shadow
    public abstract NetworkManager getNetworkManager();
}
