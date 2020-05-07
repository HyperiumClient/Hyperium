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

package cc.hyperium.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.network.server.ServerLeaveEvent;
import cc.hyperium.gui.hyperium.HyperiumMainGui;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.JsonHolder;
import java.io.IOException;
import java.text.DecimalFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiShareToLan;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class GuiHyperiumScreenIngameMenu extends GuiScreen {

  private static JsonHolder data = new JsonHolder();
  private final DecimalFormat formatter = new DecimalFormat("#,###");
  private long lastUpdate;
  private int cooldown;
  private int baseAngle;

  private GuiButton achievements, stats;

  @Override
  public void initGui() {
    super.initGui();

    buttonList.clear();

    buttonList.add(new GuiButton(1, width / 2 - 100,
        Settings.CLEANER_MENUS ? height / 4 + 111 : height / 4 + 124,
        I18n.format("menu.returnToMenu")));

    /* If Client is on server, add disconnect button */
    if (!mc.isIntegratedServerRunning()) {
      (buttonList.get(0)).displayString = I18n.format("menu.disconnect");
    }

    /* Add initial buttons */
    buttonList.add(new GuiButton(4, width / 2 - 100,
        Settings.CLEANER_MENUS ? height / 4 + 39 : height / 4 + 28,
        I18n.format("menu.returnToGame")));

    buttonList.add(new GuiButton(0, width / 2 - 100,
        Settings.CLEANER_MENUS ? height / 4 + 87 : height / 4 + 100, 98,
        20, I18n.format("menu.options")));

    GuiButton guibutton;
    buttonList.add(guibutton = new GuiButton(7, width / 2 + 2,
        Settings.CLEANER_MENUS ? height / 4 + 87 : height / 4 + 100,
        98, 20, I18n.format("menu.shareToLan")));
    guibutton.enabled = mc.isSingleplayer() && !mc.getIntegratedServer().getPublic();

    buttonList.add(achievements = new GuiButton(5, width / 2 - 100, height / 4 + 52, 98, 20,
        I18n.format("gui.achievements")));
    buttonList.add(
        stats = new GuiButton(6, width / 2 + 2, height / 4 + 52, 98, 20, I18n.format("gui.stats")));

    buttonList.add(new GuiButton(9, width / 2 - 100,
        Settings.CLEANER_MENUS ? height / 4 + 63 : height / 4 + 76,
        98, 20, I18n.format("button.ingame.hyperiumsettings")));
    buttonList.add(new GuiButton(8, width / 2 + 2,
        Settings.CLEANER_MENUS ? height / 4 + 63 : height / 4 + 76,
        98, 20, I18n.format("button.ingame.hyperiumcredits")));

  }

  @Override
  public void actionPerformed(GuiButton button) throws IOException {
    super.actionPerformed(button);

    switch (button.id) {
      case 0:
        mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
        break;

      case 1:
        if (Settings.CONFIRM_DISCONNECT) {
          Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler()
              .setDisplayNextTick(new GuiConfirmDisconnect());
        } else {
          EventBus.INSTANCE.post(new ServerLeaveEvent());
          boolean integratedServerRunning = mc.isIntegratedServerRunning();
          button.enabled = false;
          mc.theWorld.sendQuittingDisconnectingPacket();
          mc.loadWorld(null);
          Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(
              integratedServerRunning || Hyperium.INSTANCE.isFromMainMenu() ? new GuiMainMenu()
                  : new GuiMultiplayer(new GuiMainMenu()));
        }

        break;

      case 4:
        mc.displayGuiScreen(null);
        mc.setIngameFocus();
        break;

      case 5:
        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler()
            .setDisplayNextTick(new GuiAchievements(this, mc.thePlayer.getStatFileWriter()));
        break;

      case 6:
        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler()
            .setDisplayNextTick(new GuiStats(this, mc.thePlayer.getStatFileWriter()));
        break;

      case 7:
        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler()
            .setDisplayNextTick(new GuiShareToLan(this));
        break;

      case 8:
        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler()
            .setDisplayNextTick(new GuiHyperiumCredits(Minecraft.getMinecraft().currentScreen));
        break;

      case 9:
        HyperiumMainGui.INSTANCE.setTab(0);
        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler()
            .setDisplayNextTick(HyperiumMainGui.INSTANCE);
        break;

      default:
        break;
    }
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();

    if (Settings.CLEANER_MENUS) {
      achievements.visible = false;
      stats.visible = false;
    }

    if (PurchaseApi.getInstance() != null && PurchaseApi.getInstance().getSelf() != null) {
      JsonHolder response = PurchaseApi.getInstance().getSelf().getResponse();
      int credits = response.optInt("remaining_credits");

      /* Render player credits count and username */
      fontRendererObj
          .drawStringWithShadow(Minecraft.getMinecraft().getSession().getUsername(), 3, 5,
              0xFFFFFF);
      fontRendererObj
          .drawStringWithShadow(I18n.format("menu.profile.credits", credits), 3, 15, 0xFFFF00);
    }

    super.drawScreen(mouseX, mouseY, partialTicks);
    GlStateManager.translate(0, height - 50, 0);

    if (System.currentTimeMillis() - lastUpdate > 2000L) {
      refreshData();
    }

    baseAngle %= 360;

    ScaledResolution current = ResolutionUtil.current();
    GlStateManager.translate(current.getScaledWidth() >> 1, 25, 0);

    drawCenteredString(fontRendererObj, I18n.format("gui.ingamemenu.playercount"), 0, -5, 0xFFFFFF);
    GlStateManager.translate(0F, 10F, 0F);
    GlStateManager.scale(1, 1, 1);
    GlStateManager.rotate(baseAngle, 1.0F, 0.0F, 0.0F);

    float z = 4F;
    float e = 80;
    float i = 0;

    GlStateManager.translate(0.0F, 0.0F, z);

    if (baseAngle < e) {
      i = (e - Math.abs(baseAngle)) / e;
    } else if (baseAngle > 360 - e) {
      i = (e - (Math.abs((360) - baseAngle))) / e;
    }

    if (i > 0) {
      drawCenteredString(fontRendererObj, I18n.format("gui.ingamemenu.playercount.now",
          ChatColor.GREEN + formatter.format(data.optInt("online")) + ChatColor.RESET), 0, 0,
          0xFFFFFF);
    }

    GlStateManager.translate(0.0F, 0.0F, -z);
    GlStateManager.rotate(90, 1.0F, 0.0F, 0.0F);
    GlStateManager.translate(0.0F, 0.0F, z);
    i = (e - Math.abs(270 - baseAngle)) / e;

    if (i > 0) {
      drawCenteredString(fontRendererObj, I18n.format("gui.ingamemenu.playercount.lastday",
          ChatColor.GREEN + formatter.format(data.optInt("day")) + ChatColor.RESET), 0, 0,
          0xFFFFFF);
    }

    GlStateManager.translate(0.0F, 0.0F, -z);
    GlStateManager.rotate(90, 1.0F, 0.0F, 0.0F);
    GlStateManager.translate(0.0F, 0.0F, z);
    i = (e - Math.abs(180 - baseAngle)) / e;

    if (i > 0) {
      drawCenteredString(fontRendererObj, I18n.format("gui.ingamemenu.playercount.lastweek",
          ChatColor.GREEN + formatter.format(data.optInt("week")) + ChatColor.RESET), 0, 0,
          0xFFFFFF);
    }

    GlStateManager.translate(0.0F, 0.0F, -z);
    GlStateManager.rotate(90, 1.0F, 0.0F, 0.0F);
    GlStateManager.translate(0.0F, 0.0F, z);
    i = (e - Math.abs(90 - baseAngle)) / e;

    if (i > 0) {
      drawCenteredString(fontRendererObj, I18n.format("gui.ingamemenu.playercount.alltime",
          ChatColor.GREEN + formatter.format(data.optInt("all")) + ChatColor.RESET), 0, 0,
          0xFFFFFF);
    }
  }

  private synchronized void refreshData() {
    lastUpdate = System.currentTimeMillis() << 1;
    Multithreading.runAsync(() -> {
      data = PurchaseApi.getInstance().get("https://api.hyperium.cc/users");
      lastUpdate = System.currentTimeMillis();
    });

  }

  @Override
  public void updateScreen() {
    super.updateScreen();
    cooldown++;
    if (cooldown > 40) {
      baseAngle += 9;
      if (cooldown >= 50) {
        cooldown = 0;
      }
    }
  }
}
