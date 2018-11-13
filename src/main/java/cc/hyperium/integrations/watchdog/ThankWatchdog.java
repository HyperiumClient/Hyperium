package cc.hyperium.integrations.watchdog;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.ChatEvent;
import cc.hyperium.event.InvokeEvent;
import net.minecraft.client.Minecraft;

public class ThankWatchdog {

  @InvokeEvent
  public void onChat(ChatEvent e) {
    final String watchdogBanMessage = "A player has been removed from your game for hacking or abuse. Thanks for reporting it!";
    String thankWatchDogMessage = "Thanks Watchdog!";
    if (e.getChat().getUnformattedText().contains(watchdogBanMessage) && Settings.THANK_WATCHDOG) {
      Hyperium.LOGGER.debug("Thank watchdog has been triggered.");
      Minecraft.getMinecraft().thePlayer.sendChatMessage(thankWatchDogMessage);
    }
  }
}

