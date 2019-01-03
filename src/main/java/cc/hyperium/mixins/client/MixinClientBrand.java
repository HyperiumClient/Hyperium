package cc.hyperium.mixins.client;

import cc.hyperium.mixinsimp.client.HyperiumClientBrand;
import net.minecraft.client.ClientBrandRetriever;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClientBrandRetriever.class)
public class MixinClientBrand {

    /**
     * @author Sk1er
     * @reason Set client brand
     */
    @Overwrite
    public static String getClientModName() {
        return HyperiumClientBrand.getClientModName();
    }
}
