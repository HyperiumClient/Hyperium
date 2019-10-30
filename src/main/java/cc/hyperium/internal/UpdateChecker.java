package cc.hyperium.internal;

import cc.hyperium.Hyperium;
import cc.hyperium.Metadata;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.network.server.ServerJoinEvent;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.UpdateUtils;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

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
        if (asked) return; //If they were already asked, don't even make the new thread.
        UpdateUtils updateUtils = new UpdateUtils();
        Multithreading.runAsync(() -> {
            boolean latest = updateUtils.isAbsoluteLatest();
            boolean beta = updateUtils.isBeta();
            int version = Metadata.getVersionID();
            Hyperium.LOGGER.info("version is: " + version);
            if (beta) return; // dont alert beta users
            if (latest) return; //If they're on the latest version, I don't want to mess with them.

            Hyperium.INSTANCE.getNotification().display("You have an update pending.",
                "Click here to be sent to the installer.",
                10f,
                null, () -> {
                    try {
                        Desktop.getDesktop().browse(new URI("https://hyperium.cc/downloads"));
                    } catch (IOException | URISyntaxException e) {
                        IChatComponent urlComponent = new
                            ChatComponentText(ChatColor.RED + "[Hyperium] " +
                            ChatColor.GRAY + "Click to be sent to update Hyperium");
                        urlComponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://hyperium.cc/downloads"));
                        Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(urlComponent);
                    }
                }, new Color(200, 150, 50));
            asked = true;
        });
    }
}
