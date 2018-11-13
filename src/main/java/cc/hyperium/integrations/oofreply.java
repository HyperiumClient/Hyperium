package cc.hyperium.integrations.oofreply;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.ChatEvent;
import cc.hyperium.event.InvokeEvent;

import net.minecraft.client.Minecraft;

public class oofreply {
  
  private static final String oofByOtherPlayer = "oof";
  private static final String selfOOF = "OOF!";
  
  @InvokeEvent
  public void onChat(ChatEvent e) {
    if (e.getChat().getUnformattedText().contains(oofByOtherPlayer) && Settings.OOF_REPLY) {
      Minecraft.getMinecraft().thePlayer.sendChatMessage(selfOOF);
    }
  
  }

}
