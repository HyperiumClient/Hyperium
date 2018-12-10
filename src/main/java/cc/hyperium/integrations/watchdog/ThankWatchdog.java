package cc.hyperium.integrations.watchdog;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.ChatEvent;
import cc.hyperium.event.InvokeEvent;
import net.minecraft.client.Minecraft;

public class ThankWatchdog {
  private static final String WATCHDOG_BAN_TRIGGER = "A player has been removed from your game for hacking or abuse. Thanks for reporting it!";
  private static final String WATCHDOG_ANNOUNCEMENT_TRIGGER = "[WATCHDOG ANNOUNCEMENT]";
  private static final String THANK_WATCHDOG_MESSAGE = "Thanks Watchdog!";

  @InvokeEvent
  public void onChat(ChatEvent e) {
    if (e.getChat().getUnformattedText().contains(WATCHDOG_BAN_TRIGGER) || e.getChat().getUnformattedText().contains(WATCHDOG_ANNOUNCEMENT_TRIGGER)) {
      if(Settings.THANK_WATCHDOG) { Minecraft.getMinecraft().thePlayer.sendChatMessage("/achat " + THANK_WATCHDOG_MESSAGE); }
    }
  }
}

