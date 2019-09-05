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

package cc.hyperium.mixinsimp.client.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.bossbar.config.BossbarConfig;
import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.RenderHUDEvent;
import cc.hyperium.event.RenderSelectedItemEvent;
import cc.hyperium.mods.chromahud.displayitems.hyperium.ScoreboardDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.scoreboard.ScoreObjective;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class HyperiumGuiIngame {
    public static boolean renderScoreboard = true;

    private GuiIngame parent;

    public HyperiumGuiIngame(GuiIngame parent) {
        this.parent = parent;
    }

    public void renderSelectedItem(ScaledResolution sr) {
        EventBus.INSTANCE.post(new RenderSelectedItemEvent(sr));
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void renderGameOverlay(float part) {
        Minecraft.getMinecraft().mcProfiler.startSection("hyperium_overlay");
        EventBus.INSTANCE.post(new RenderHUDEvent(part));
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().mcProfiler.endSection();

    }

    public void renderScoreboard(ScoreObjective objective, ScaledResolution resolution) {
        //For *extra* scoreboards
        ScoreboardDisplay.p_180475_1_ = objective;
        ScoreboardDisplay.p_180475_2_ = resolution;

        if (renderScoreboard) {
            Hyperium.INSTANCE.getHandlers().getScoreboardRenderer().render(objective, resolution);
        }
    }

    public void renderBossHealth() {
        if (BossStatus.bossName != null && BossStatus.statusBarTime > 0 && BossbarConfig.bossBarEnabled) {
            --BossStatus.statusBarTime;

            ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
            int i = scaledresolution.getScaledWidth();
            if (Settings.BOSSBAR_TEXT_ONLY || (!BossbarConfig.barEnabled && BossbarConfig.textEnabled)) {
                String s = BossStatus.bossName;
                if (BossbarConfig.x != -1) {
                    parent.getFontRenderer().drawStringWithShadow(s, (float) (BossbarConfig.x + 91 - parent.getFontRenderer().getStringWidth(s) / 2), BossbarConfig.y - 10, 16777215);
                } else {
                    parent.getFontRenderer().drawStringWithShadow(s, (float) (i / 2 - parent.getFontRenderer().getStringWidth(s) / 2), BossbarConfig.y - 10, 16777215);
                }
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                Minecraft.getMinecraft().getTextureManager().bindTexture(Gui.icons);
                return;
            }
            int j = 182;
            if (BossbarConfig.barEnabled) {
                int l = (int) (BossStatus.healthScale * (float) (j + 1));
                if (BossbarConfig.x != -1) {
                    parent.drawTexturedModalRect(BossbarConfig.x, BossbarConfig.y, 0, 74, j, 5);
                    if (l > 0) {
                        parent.drawTexturedModalRect(BossbarConfig.x, BossbarConfig.y, 0, 79, l, 5);
                    }
                } else {
                    int k = i / 2 - j / 2;
                    parent.drawTexturedModalRect(k, BossbarConfig.y, 0, 74, j, 5);
                    if (l > 0) {
                        parent.drawTexturedModalRect(k, BossbarConfig.y, 0, 79, l, 5);
                    }
                }
            }

            String s = BossStatus.bossName;
            if (BossbarConfig.textEnabled) {
                if (BossbarConfig.x != -1) {
                    parent.getFontRenderer().drawStringWithShadow(s, (float) (BossbarConfig.x + j / 2 - parent.getFontRenderer().getStringWidth(s) / 2), BossbarConfig.y - 10, 16777215);
                } else {
                    parent.getFontRenderer().drawStringWithShadow(s, (float) (i / 2 - parent.getFontRenderer().getStringWidth(s) / 2), BossbarConfig.y - 10, 16777215);
                }
            }
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().getTextureManager().bindTexture(Gui.icons);
        }
    }

    public void showCrosshair(CallbackInfoReturnable<Boolean> ci) {
        if (CustomCrosshairAddon.getCrosshairMod() == null) {
            return;
        }

        if (CustomCrosshairAddon.getCrosshairMod().getCrosshair() == null) {
            return;
        }

        if (CustomCrosshairAddon.getCrosshairMod().getCrosshair().getEnabled()) {
            ci.setReturnValue(false);
        }
    }
}
