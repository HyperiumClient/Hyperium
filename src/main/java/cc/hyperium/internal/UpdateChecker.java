package cc.hyperium.internal;

import cc.hyperium.Hyperium;
import cc.hyperium.Metadata;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.network.server.ServerJoinEvent;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.HyperiumDesktop;
import cc.hyperium.utils.UpdateUtils;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.net.URI;

public class UpdateChecker {

  private static UpdateChecker instance;
  private boolean asked;

  public UpdateChecker() {
    instance = this;
  }

  public static UpdateChecker getInstance() {
    return instance;
  }

  @InvokeEvent
  public void serverJoinEvent(ServerJoinEvent event) {
    if (asked) {
      return; //If they were already asked, don't even make the new thread.
    }
    UpdateUtils updateUtils = new UpdateUtils();
    boolean latest = updateUtils.isAbsoluteLatest();
    boolean beta = updateUtils.isBeta();
    int version = Metadata.getVersionID();
    Hyperium.LOGGER.info("version is: " + version);
    if (version == -1) {
      return; // dev
    }
    if (beta) {
      return; // dont alert beta users
    }
    if (latest) {
      return; //If they're on the latest version, I don't want to mess with them.
    }

    Multithreading.runAsync(() -> {
      if (Minecraft.getMinecraft().theWorld != null) {
        Hyperium.INSTANCE.getHandlers().getNotificationCenter()
            .display("You have an update pending.",
                "Click here to be sent to the installer.",
                10f,
                null, () -> {
                  try {
                    HyperiumDesktop.INSTANCE.browse(new URI("https://hyperium.cc/downloads"));
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                }, new Color(200, 150, 50));
        asked = true;
      }
    });
  }
}
