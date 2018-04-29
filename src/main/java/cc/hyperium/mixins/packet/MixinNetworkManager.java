package cc.hyperium.mixins.packet;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.PacketReceivedEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
class MixinNetworkManager {

    @Inject(method = "channelRead0", at = @At(value = "FIELD", target = "Lnet/minecraft/network/NetworkManager;packetListener:Lnet/minecraft/network/INetHandler;"))
    private void channelRead0(ChannelHandlerContext p_channelRead0_1_, Packet p_channelRead0_2_, CallbackInfo ci) {
        EventBus.INSTANCE.post(new PacketReceivedEvent(p_channelRead0_2_));
    }
}
