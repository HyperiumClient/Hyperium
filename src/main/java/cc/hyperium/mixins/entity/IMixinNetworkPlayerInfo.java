package cc.hyperium.mixins.entity;

import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NetworkPlayerInfo.class)
public interface IMixinNetworkPlayerInfo {
    @Invoker
    void callLoadPlayerTextures();

    @Accessor
    void setLocationCape(ResourceLocation locationCape);

    @Accessor
    void setLocationSkin(ResourceLocation locationSkin);

    @Accessor
    void setPlayerTexturesLoaded(boolean loaded);

}
