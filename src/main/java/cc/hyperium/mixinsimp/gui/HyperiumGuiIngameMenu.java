package cc.hyperium.mixinsimp.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.gui.GuiHyperiumCredits;
import cc.hyperium.gui.GuiIngameMultiplayer;
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
import net.minecraft.server.MinecraftServer;

public class HyperiumGuiIngameMenu {
  private static JsonHolder data = new JsonHolder();
  private final DecimalFormat formatter = new DecimalFormat("#,###");
  private long lastUpdate = 0L;
  private int cooldown = 0;
  private int baseAngle;
  private GuiIngameMenu parent;
  public HyperiumGuiIngameMenu(GuiIngameMenu parent) {
    this.parent = parent;
  }

  public void initGui(List<GuiButton> buttonList) {
    Hyperium.INSTANCE.getHandlers().getKeybindHandler().releaseAllKeybinds();
    buttonList.add(new GuiButton(8, parent.width - 200, parent.height - 20, 200, 20, "Credits"));
    WorldClient theWorld = Minecraft.getMinecraft().theWorld;

    // Used to detect if the player is on a singleplayer world or a multiplayer world.
    MinecraftServer integratedServer = Minecraft.getMinecraft().getIntegratedServer();
    if (theWorld != null && (integratedServer == null)) {
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
  public void draw(int mouseX, int mouseY, float partialTicks, FontRenderer fontRendererObj) {
    GlStateManager.pushMatrix();
    if (System.currentTimeMillis() - lastUpdate > 2000L) {
      refreshData();

    }
    baseAngle %= 360;
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
    lastUpdate = System.currentTimeMillis() * 2;
    Multithreading.runAsync(() -> {
      data = PurchaseApi.getInstance().get("https://api.hyperium.cc/users");
      lastUpdate = System.currentTimeMillis();
    });
  }
  public void update() {
    cooldown++;
    if (cooldown > 40) {
      baseAngle += 9;
      if (cooldown >= 50) {
        cooldown = 0;
      }
    }
  }
}
