package cc.hyperium.mixins.client;

import cc.hyperium.mixinsimp.client.HyperiumClientBrand;
import net.minecraft.client.ClientBrandRetriever;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClientBrandRetriever.class)
public class MixinClientBrand {

    @Overwrite
    public static String getClientModName() {
        return HyperiumClientBrand.CLIENT_BRAND;
    }
}
