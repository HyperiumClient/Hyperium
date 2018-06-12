package cc.hyperium.mixins.client;

import net.minecraft.client.ClientBrandRetriever;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClientBrandRetriever.class)
public class MixinClientBrand {

    @Overwrite
    public static String getClientModName() {
        return "hyperium";
    }
}
