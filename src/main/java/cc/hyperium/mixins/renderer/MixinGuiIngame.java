/*
 *  Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  Hyperium Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mixins.renderer;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.RenderHUDEvent;
import cc.hyperium.mods.chromahud.displayitems.hyperium.ScoreboardDisplay;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.ScoreObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

;

@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame {

    @Shadow
    public abstract FontRenderer getFontRenderer();

    @Inject(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))

    private void renderGameOverlay(float partialTicks, CallbackInfo ci) {
        EventBus.INSTANCE.post(new RenderHUDEvent(partialTicks));
    }

    /**
     * @param p_180475_1_
     * @param p_180475_2_
     * @author Kevin Brewster
     */
    @Overwrite
    private void renderScoreboard(ScoreObjective p_180475_1_, ScaledResolution p_180475_2_) {
        ScoreboardDisplay.p_180475_1_ = p_180475_1_;
        ScoreboardDisplay.p_180475_2_ = p_180475_2_;
    }
}
