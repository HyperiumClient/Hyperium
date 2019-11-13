package cc.hyperium.mixins.server;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.awt.image.BufferedImage;
import java.io.File;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    @Inject(method = "addFaviconToStatusResponse", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ServerStatusResponse;setFavicon(Ljava/lang/String;)V",
        shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void releaseByteBuffer(ServerStatusResponse response, CallbackInfo ci, File serverIcon, ByteBuf buffer, BufferedImage image, ByteBuf bytebuf1) {
        bytebuf1.release();
    }
}
