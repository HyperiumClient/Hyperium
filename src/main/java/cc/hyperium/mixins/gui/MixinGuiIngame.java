/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mixins.gui;

import cc.hyperium.addons.customcrosshair.crosshair.Crosshair;
import cc.hyperium.mixinsimp.gui.HyperiumGuiIngame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.inventory.IInventory;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame extends Gui {

    @Shadow
    @Final
    private Minecraft mc;
    private HyperiumGuiIngame hyperiumGuiIngame = new HyperiumGuiIngame((GuiIngame) (Object) this);

    @Shadow
    public abstract FontRenderer getFontRenderer();

    @Inject(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))
    private void renderGameOverlay(float partialTicks, CallbackInfo ci) {
        hyperiumGuiIngame.renderGameOverlay(partialTicks, ci);
    }

    @Inject(method = "renderSelectedItem", at = @At(value = "RETURN", target = "Lnet/minecraft/client/renderer/GlStateManager;popMatrix()V"))
    private void onRenderSelectedItem(ScaledResolution p_181551_1_, CallbackInfo ci) {
        hyperiumGuiIngame.renderSelectedItem(p_181551_1_);
    }

    @Overwrite
    private void renderScoreboard(ScoreObjective objective, ScaledResolution resolution) {
        hyperiumGuiIngame.renderScoreboard(objective, resolution);
    }

    /**
     * Add toggle for boss bar texture
     *
     * @author boomboompower
     * @reason cool things
     */
    @Overwrite
    private void renderBossHealth() {
        hyperiumGuiIngame.renderBossHealth();
    }
    
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
