/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mixins.client.gui;

import cc.hyperium.mixinsimp.client.gui.HyperiumGuiPlayerTabOverlay;
import com.google.common.collect.Ordering;
import me.semx11.autotip.Autotip;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiPlayerTabOverlay.class)
public abstract class MixinGuiPlayerTabOverlay extends Gui {

    @Shadow @Final private static Ordering<NetworkPlayerInfo> field_175252_a;
    @Shadow @Final private Minecraft mc;
    @Shadow private IChatComponent header;
    @Shadow private IChatComponent footer;

    private HyperiumGuiPlayerTabOverlay hyperiumGuiPlayerTabOverlay = new HyperiumGuiPlayerTabOverlay((GuiPlayerTabOverlay) (Object) this);

    /**
     * @author boomboompower
     * @reason Numbered ping in tab
     */
    @Overwrite
    protected void drawPing(int p_175245_1_, int p_175245_2_, int yIn, NetworkPlayerInfo networkPlayerInfoIn) {
        hyperiumGuiPlayerTabOverlay.drawPing(p_175245_1_, p_175245_2_, yIn, networkPlayerInfoIn, zLevel, mc);
    }

    /**
     * @author Sk1er
     * @reason Friends first in tab
     */
    @Overwrite
    public void renderPlayerlist(int width, Scoreboard scoreboardIn, ScoreObjective scoreObjectiveIn) {
        hyperiumGuiPlayerTabOverlay.renderPlayerlist(width, scoreboardIn, scoreObjectiveIn, field_175252_a, header, footer, mc);
    }

    @Inject(method = "setHeader", at = @At("HEAD"))
    private void setHeader(IChatComponent headerIn, CallbackInfo ci) {
        // Update chat header for use in autotip.
        Autotip.tabHeader = headerIn;
    }
}
