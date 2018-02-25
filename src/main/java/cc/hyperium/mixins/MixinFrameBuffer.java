package cc.hyperium.mixins;

import net.minecraft.client.shader.Framebuffer;
import org.spongepowered.asm.mixin.Mixin;

import java.io.Serializable;

@Mixin(Framebuffer.class)
public class MixinFrameBuffer implements Serializable{ }
