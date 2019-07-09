package cc.hyperium.mixins.client.network;

import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NetworkPlayerInfo.class)
public interface IMixinNetworkPlayerInfo {

    @Accessor void setPlayerTexturesLoaded(boolean playerTexturesLoaded);
    @Accessor void setLocationSkin(ResourceLocation locationSkin);
    @Accessor void setLocationCape(ResourceLocation locationCape);

}
