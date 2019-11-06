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
import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.render.RenderHUDEvent;
import cc.hyperium.event.render.RenderSelectedItemEvent;
import cc.hyperium.mods.chromahud.displayitems.hyperium.ScoreboardDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.scoreboard.ScoreObjective;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class HyperiumGuiIngame {
    public static boolean renderScoreboard = true;

    public static boolean renderHealth = true;
    public static boolean renderFood = true;
    public static boolean renderArmor = true;

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
        EventBus.INSTANCE.post(new RenderHUDEvent(new ScaledResolution(Minecraft.getMinecraft()), part));
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().mcProfiler.endSection();

    }

    public void renderScoreboard(ScoreObjective objective, ScaledResolution resolution) {
        //For *extra* scoreboards
        ScoreboardDisplay.objective = objective;
        ScoreboardDisplay.resolution = resolution;

        if (renderScoreboard) Hyperium.INSTANCE.getHandlers().getScoreboardRenderer().render(objective, resolution);
    }

    public void renderBossHealth() {
        if (BossStatus.bossName != null && BossStatus.statusBarTime > 0 && Settings.BOSSBAR_ALL) {
            --BossStatus.statusBarTime;

            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
            ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
            double scaledWidth = resolution.getScaledWidth();
            double scaledHeight = resolution.getScaledHeight();

            String bossName = BossStatus.bossName;

            if (Settings.BOSSBAR_TEXT) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(scaledWidth * Settings.BOSSBAR_X, scaledHeight * Settings.BOSSBAR_Y - (10D * Settings.BOSSBAR_SCALE), 0);
                GlStateManager.scale(Settings.BOSSBAR_SCALE, Settings.BOSSBAR_SCALE, Settings.BOSSBAR_SCALE);
                GlStateManager.translate(-fontRenderer.getStringWidth(bossName) / 2F , 0, 0);
                fontRenderer.drawStringWithShadow(bossName, 0, 0, -1);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                Minecraft.getMinecraft().getTextureManager().bindTexture(Gui.icons);
                GlStateManager.popMatrix();
            }

            int widthLocation = 182;

            if (Settings.BOSSBAR_BAR) {
                int healthScale = (int) (BossStatus.healthScale * (float) (widthLocation + 1) * Settings.BOSSBAR_SCALE);

                GlStateManager.pushMatrix();
                GlStateManager.translate(Settings.BOSSBAR_X * scaledWidth - widthLocation / 2F * Settings.BOSSBAR_SCALE, Settings.BOSSBAR_Y * scaledHeight , 0);
                GlStateManager.scale(Settings.BOSSBAR_SCALE, Settings.BOSSBAR_SCALE, Settings.BOSSBAR_SCALE);

                parent.drawTexturedModalRect(0, 0, 0, 74, widthLocation, 5);
                if (healthScale > 0) parent.drawTexturedModalRect(0, 0, 0, 79, widthLocation, 5);
                GlStateManager.popMatrix();
            }

        }
    }

    public void showCrosshair(CallbackInfoReturnable<Boolean> ci) {
        if (CustomCrosshairAddon.getCrosshairMod() == null || CustomCrosshairAddon.getCrosshairMod().getCrosshair() == null) {
            return;
        }

        if (CustomCrosshairAddon.getCrosshairMod().getCrosshair().getEnabled()) {
            ci.setReturnValue(false);
        }
    }
}
