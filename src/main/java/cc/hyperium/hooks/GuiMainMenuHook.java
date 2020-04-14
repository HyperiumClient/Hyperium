package cc.hyperium.hooks;

import cc.hyperium.Hyperium;
import cc.hyperium.gui.GuiAddonError;
import cc.hyperium.gui.GuiHyperiumScreenMainMenu;
import cc.hyperium.gui.GuiHyperiumScreenTos;
import cc.hyperium.handlers.handlers.GuiDisplayHandler;
import cc.hyperium.internal.addons.AddonMinecraftBootstrap;
import net.minecraft.client.gui.GuiMainMenu;

public class GuiMainMenuHook {

  public static void initGui(GuiMainMenu screen) {
    if (Hyperium.INSTANCE.isAcceptedTos()) {
      screen.drawDefaultBackground();
    }
  }

  public static void drawScreen() {
    GuiDisplayHandler displayHandler = Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler();

    if (!Hyperium.INSTANCE.isAcceptedTos() && !Hyperium.INSTANCE.isDevEnv()) {
      Hyperium.LOGGER.info("Hasn't accepted! Redirecting them!");
      displayHandler.setDisplayNextTick(new GuiHyperiumScreenTos());
    } else if (!AddonMinecraftBootstrap.getDependenciesLoopMap().isEmpty() || !AddonMinecraftBootstrap.getMissingDependenciesMap().isEmpty()) {
      displayHandler.setDisplayNextTick(new GuiAddonError());
    } else {
      displayHandler.setDisplayNextTick(new GuiHyperiumScreenMainMenu());
    }
  }
}
