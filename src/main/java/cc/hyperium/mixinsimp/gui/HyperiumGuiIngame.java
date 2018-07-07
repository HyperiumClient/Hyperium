package cc.hyperium.mixinsimp.gui;

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.RenderSelectedItemEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.boss.BossStatus;

public class HyperiumGuiIngame {
  private GuiIngame parent;
  public void renderSelectedItem(ScaledResolution sr) {
    EventBus.INSTANCE.post(new RenderSelectedItemEvent(sr));
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
}
