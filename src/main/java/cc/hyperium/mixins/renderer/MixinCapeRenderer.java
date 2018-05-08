package cc.hyperium.mixins.renderer;

import cc.hyperium.Hyperium;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LayerCape.class)
public class MixinCapeRenderer  {
    @Shadow @Final private RenderPlayer playerRenderer;

    @Overwrite
    public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {

        Hyperium.INSTANCE.getHandlers().getCapeHandler().doRenderLayer(entitylivingbaseIn,p_177141_2_,p_177141_3_,partialTicks,p_177141_5_,p_177141_6_,p_177141_7_,scale,playerRenderer);
    }


}
