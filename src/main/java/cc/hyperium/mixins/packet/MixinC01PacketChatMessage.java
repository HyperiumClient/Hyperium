package cc.hyperium.mixins.packet;

import net.minecraft.network.play.client.C01PacketChatMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(C01PacketChatMessage.class)
public interface MixinC01PacketChatMessage {

    @Accessor
    void setMessage(String message);

}
