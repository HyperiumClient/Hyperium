package cc.hyperium.handlers.handlers.server;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.network.server.ServerJoinEvent;
import cc.hyperium.event.network.server.SingleplayerJoinEvent;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.protocol.ProtocolDetector;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;

public class ProtocolChecker {

  @InvokeEvent
  public void connectSingleplayer(SingleplayerJoinEvent event) {
    GuiChat.maxStringLength = 256;
  }

  @InvokeEvent
  public void connectToServer(ServerJoinEvent event) {
    CompletableFuture<Boolean> future = ProtocolDetector.INSTANCE.isCompatibleWithVersion(
        Minecraft.getMinecraft().getCurrentServerData().serverIP,
        315
    );

    Multithreading.runAsync(() -> {
      try {
        if (future.get()) {
          GuiChat.maxStringLength = 256;
        } else {
          GuiChat.maxStringLength = 100;
        }
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    });
  }
}
