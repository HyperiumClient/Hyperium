package cc.hyperium.mixinsimp.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.RenderHUDEvent;
import cc.hyperium.event.RenderSelectedItemEvent;
import cc.hyperium.mods.chromahud.displayitems.hyperium.ScoreboardDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.scoreboard.ScoreObjective;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class HyperiumGuiIngame {
    private GuiIngame parent;

    public HyperiumGuiIngame(GuiIngame parent) {
        this.parent = parent;
    }

    public void renderSelectedItem(ScaledResolution sr) {
        EventBus.INSTANCE.post(new RenderSelectedItemEvent(sr));
    }
    public void renderGameOverlay(float partialTicks, CallbackInfo ci) {
        EventBus.INSTANCE.post(new RenderHUDEvent(partialTicks));
    }

    public void renderScoreboard(ScoreObjective objective, ScaledResolution resolution) {
        //For *extra* scoreboards
        ScoreboardDisplay.p_180475_1_ = objective;
        ScoreboardDisplay.p_180475_2_ = resolution;

        Hyperium.INSTANCE.getHandlers().getScoreboardRenderer().render(objective, resolution);
    }
    public void renderBossHealth() {
        if (BossStatus.bossName != null && BossStatus.statusBarTime > 0) {
            --BossStatus.statusBarTime;

            FontRenderer fontrenderer = Minecraft.getMinecraft().fontRendererObj;
            ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
            int i = scaledresolution.getScaledWidth();
            int i1 = 12;

            if (Settings.BOSSBAR_TEXT_ONLY) {
                String s = BossStatus.bossName;
                parent.getFontRenderer().drawStringWithShadow(s, (float) (i / 2 - parent.getFontRenderer().getStringWidth(s) / 2), (float) (i1 - 10), 16777215);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                Minecraft.getMinecraft().getTextureManager().bindTexture(Gui.icons);
                return;
            }

            int j = 182;
            int k = i / 2 - j / 2;
            int l = (int) (BossStatus.healthScale * (float) (j + 1));
            parent.drawTexturedModalRect(k, i1, 0, 74, j, 5);
            parent.drawTexturedModalRect(k, i1, 0, 74, j, 5);

            if (l > 0) {
                parent.drawTexturedModalRect(k, i1, 0, 79, l, 5);
            }

            String s = BossStatus.bossName;
            parent.getFontRenderer().drawStringWithShadow(s, (float) (i / 2 - parent.getFontRenderer().getStringWidth(s) / 2), (float) (i1 - 10), 16777215);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().getTextureManager().bindTexture(Gui.icons);
        }
    }

    public void showCrosshair(CallbackInfoReturnable<Boolean> ci){
        if (CustomCrosshairAddon.getCrosshairMod() == null){
            return;
        }

        if (CustomCrosshairAddon.getCrosshairMod().getCrosshair() == null){
            return;
        }

        if (CustomCrosshairAddon.getCrosshairMod().getCrosshair().getEnabled()) {
            ci.setReturnValue(false);
        }
    }
}
