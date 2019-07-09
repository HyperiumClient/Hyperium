package cc.hyperium.mixinsimp.world;

import cc.hyperium.config.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class HyperiumChunk {

    public void getLightFor(EnumSkyBlock type, BlockPos pos, CallbackInfoReturnable<Integer> ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && Settings.FULLBRIGHT) {
            ci.setReturnValue(15);
        }
    }

    public void getLightSubtracted(BlockPos pos, int amount, CallbackInfoReturnable<Integer> ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && Settings.FULLBRIGHT) {
            ci.setReturnValue(15);
        }
    }
}
