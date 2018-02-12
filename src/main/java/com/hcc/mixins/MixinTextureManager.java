package com.hcc.mixins;

import com.hcc.utils.Utils;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureManager.class)
public class MixinTextureManager {

    @Inject(method = "onResourceManagerReload", at = @At("HEAD"))
    private void onResourceManagerReload(IResourceManager resourceManager, CallbackInfo ci) {
        Utils.INSTANCE.setCursor(new ResourceLocation("textures/cursor.png"));
    }
}
