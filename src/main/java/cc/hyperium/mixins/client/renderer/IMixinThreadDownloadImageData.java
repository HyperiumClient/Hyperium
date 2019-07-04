package cc.hyperium.mixins.client.renderer;

import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ThreadDownloadImageData.class)
public interface IMixinThreadDownloadImageData {

    @Accessor IImageBuffer getImageBuffer();

}
