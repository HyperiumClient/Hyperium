package cc.hyperium.mixinsimp.gui;

import cc.hyperium.gui.GuiHyperiumCredits;
import cc.hyperium.gui.GuiIngameMultiplayer;
import cc.hyperium.mixins.gui.MixinGuiIngameMenu2;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.JsonHolder;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;

public class HyperiumGuiIngameMenu {
  private GuiIngameMenu parent;
  public HyperiumGuiIngameMenu(GuiIngameMenu parent) {
    this.parent = parent;
  }
  public void initGui(List<GuiButton> buttonList) {
    buttonList.add(new GuiButton(8, parent.width - 200, parent.height - 20, 200, 20, "Credits"));
//        this.buttonList.get(3).displayString = Minecraft.getMinecraft().getIntegratedServer().getPublic() ? "Change Server" : this.buttonList.get(3).displayString;
//        this.buttonList.get(3).enabled = Minecraft.getMinecraft().getIntegratedServer().getPublic() ? true : this.buttonList.get(3).enabled;
    WorldClient theWorld = Minecraft.getMinecraft().theWorld;
    if (theWorld != null && theWorld.isRemote) {
      GuiButton oldButton = buttonList.remove(3);
      GuiButton newButton = new GuiButton(10, oldButton.xPosition, oldButton.yPosition, oldButton.getButtonWidth(), 20, "Server List");
      buttonList.add(newButton);
    }
  }
  public void actionPerformed(GuiButton button) {
    if (button.id == 8)
      Minecraft.getMinecraft().displayGuiScreen(new GuiHyperiumCredits(Minecraft.getMinecraft().currentScreen));
    if (button.id == 10 && Minecraft.getMinecraft().theWorld.isRemote)
      Minecraft.getMinecraft().displayGuiScreen(new GuiIngameMultiplayer(Minecraft.getMinecraft().currentScreen));
  }
  public void draw(int mouseX, int mouseY, float partialTicks, long lastUpdate, int baseAngle, FontRenderer fontRendererObj, JsonHolder data, DecimalFormat formatter) {
    GlStateManager.pushMatrix();
    if (System.currentTimeMillis() - lastUpdate > 2000L) {
      refreshData();

    }
    MixinGuiIngameMenu2 parentA = (MixinGuiIngameMenu2) parent;
    parentA.setBaseAngle(baseAngle % 360);
    ScaledResolution current = ResolutionUtil.current();
    GlStateManager.translate(current.getScaledWidth() / 2, 10, 0);
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    parent.drawCenteredString(fontRendererObj, "Hyperium Stats", 0, 0, new Color(249 / 255F, 76 / 255F, 238 / 255F, 1.0F).getRGB());
    GlStateManager.translate(0F, 10F, 0F);
    GlStateManager.scale(2.0F, 2.0F, 2.0F);
    GlStateManager.rotate(baseAngle, 1.0F, 0.0F, 0.0F);
    GlStateManager.enableAlpha();
    float z = 4F;
    float e = 80;
    float i = 0;
    GlStateManager.translate(0.0F, 0.0F, z);
    if (baseAngle < e) {
      i = (e - Math.abs(baseAngle)) / e;
    } else if (baseAngle > 360 - e) {
      i = (e - (Math.abs((360) - baseAngle))) / e;
    }

    if (i > 0)
      parent.drawCenteredString(fontRendererObj, "Online players: " + formatter.format(data.optInt("online")), 0, 0, new Color(249 / 255F, 76 / 255F, 238 / 255F, i).getRGB());


    GlStateManager.translate(0.0F, 0.0F, -z);
    GlStateManager.rotate(90, 1.0F, 0.0F, 0.0F);
    GlStateManager.translate(0.0F, 0.0F, z);
    i = (e - Math.abs(270 - baseAngle)) / e;

    if (i > 0)
      parent.drawCenteredString(fontRendererObj, "Last Day: " + formatter.format(data.optInt("day")), 0, 0, new Color(249 / 255F, 76 / 255F, 238 / 255F, i).getRGB());


    GlStateManager.translate(0.0F, 0.0F, -z);
    GlStateManager.rotate(90, 1.0F, 0.0F, 0.0F);
    GlStateManager.translate(0.0F, 0.0F, z);
    i = (e - Math.abs(180 - baseAngle)) / e;

    if (i > 0)
      parent.drawCenteredString(fontRendererObj, "Last Week: " + formatter.format(data.optInt("week")), 0, 0, new Color(249 / 255F, 76 / 255F, 238 / 255F, i).getRGB());


    GlStateManager.translate(0.0F, 0.0F, -z);
    GlStateManager.rotate(90, 1.0F, 0.0F, 0.0F);
    GlStateManager.translate(0.0F, 0.0F, z);
    i = (e - Math.abs(90 - baseAngle)) / e;
    if (i > 0)
      parent.drawCenteredString(fontRendererObj, "All time: " + formatter.format(data.optInt("all")), 0, 0, new Color(249 / 255F, 76 / 255F, 238 / 255F, i).getRGB());

    GlStateManager.popMatrix();
  }

  private synchronized void refreshData() {
    ((MixinGuiIngameMenu2) parent).setLastUpdate(System.currentTimeMillis() * 2);
    Multithreading.runAsync(() -> {
      ((MixinGuiIngameMenu2) parent).setData(PurchaseApi.getInstance().get("https://api.hyperium.cc/users"));
      ((MixinGuiIngameMenu2) parent).setLastUpdate(System.currentTimeMillis());
    });
  }
}
