package cc.hyperium.internal;

import cc.hyperium.Hyperium;
import cc.hyperium.Metadata;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.ServerJoinEvent;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.UpdateUtils;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
        if(asked) return; //If they were already asked, don't even make the new thread.
        UpdateUtils updateUtils = new UpdateUtils();
        Multithreading.runAsync(() -> {
            boolean latest = updateUtils.isAbsoluteLatest();
            int version = Metadata.getVersionID();
            Hyperium.LOGGER.info("version is: " + version);
            if (latest) return; //If they're on the latest version, I don't want to mess with them.

            //Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("You have an update pending.");
            Hyperium.INSTANCE.getNotification().display("You have an update pending.",
                "Click here to be sent to the installer.",
                10f,
                null, () -> {
                    try {
                        Desktop.getDesktop().browse(new URI("https://hyperium.cc/downloads"));
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                }, new Color(200, 150, 50));
            asked = true;
        });
    }
}
