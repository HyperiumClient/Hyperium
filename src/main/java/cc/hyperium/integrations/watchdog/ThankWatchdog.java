package cc.hyperium.integrations.watchdog;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.ChatEvent;
import cc.hyperium.event.InvokeEvent;
import net.minecraft.client.Minecraft;

public class ThankWatchdog {
  private static final String watchdogBanMessage = "A player has been removed from your game for hacking or abuse. Thanks for reporting it!";
  private static final String thankWatchDogMessage = "Thanks Watchdog!";

  @InvokeEvent
  public void onChat(ChatEvent e) {
    if (e.getChat().getUnformattedText().contains(watchdogBanMessage) && Settings.THANK_WATCHDOG) {
      Minecraft.getMinecraft().thePlayer.sendChatMessage(thankWatchDogMessage);
    }
  }
}

