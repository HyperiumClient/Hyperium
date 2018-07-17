package cc.hyperium.addons.customcrosshair.mixins;

import cc.hyperium.addons.customcrosshair.crosshair.Crosshair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiIngame.class)
public class MixinGuiIngame {
    @Shadow
    @Final
    private Minecraft mc;

    @Overwrite
    protected boolean showCrosshair() {
        if (!Crosshair.showVanillaCrosshair) {
            return false;
        }
        if (this.mc.gameSettings.showDebugInfo && !this.mc.thePlayer.hasReducedDebug() && !this.mc.gameSettings.reducedDebugInfo)
        {
            return false;
        }
        else if (this.mc.playerController.isSpectator())
        {
            if (this.mc.pointedEntity != null)
            {
                return true;
            }
            else
            {
                if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                {
                    BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();

                    if (this.mc.theWorld.getTileEntity(blockpos) instanceof IInventory)
                    {
                        return true;
                    }
                }

                return false;
            }
        }
        else
        {
            return true;
        }
    }
}
