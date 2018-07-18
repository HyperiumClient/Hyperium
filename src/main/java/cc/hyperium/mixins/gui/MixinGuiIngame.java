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

import cc.hyperium.mixinsimp.gui.HyperiumGuiIngame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.ScoreObjective;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

    /**
     * Disables the normal crosshair if custom crosshair is active.
     */
    @Inject(method = "showCrosshair",at=@At("HEAD"),cancellable = true)
    protected void showCrosshair(CallbackInfoReturnable<Boolean> ci) {
        hyperiumGuiIngame.showCrosshair(ci);
    }
}
