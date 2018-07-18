package cc.hyperium.addons.sidebar.versions;

import cc.hyperium.addons.sidebar.SidebarAddon;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.ServerJoinEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class UpdateChecker implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger("SidebarAddon");
    private boolean isLatestVersion = true;
    private Versions versions;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final boolean PRERELEASE = false;

    @Override
    public void run() {
        InputStream in;
        try {
            in = new URL("https://raw.githubusercontent.com/Amplifiable2/SidebarAddon/master/versions.json").openStream();
        } catch (IOException e) {
            LOGGER.error("An exception occurred while checking if the addon is up-to-date: " + e, e);
            return;
        }

        String[] lines;

        try {
            lines = (String[]) IOUtils.readLines(in).toArray();
            IOUtils.closeQuietly(in);
        } catch (IOException | NullPointerException e) {
            LOGGER.error("An exception occurred while checking if the addon is up-to-date: " + e, e);
            IOUtils.closeQuietly(in);
            return;
        }
        StringBuilder json = new StringBuilder();
        for (String line : lines) {
            json.append(line).append("\n");
        }
        String jsonString = json.toString();
        this.versions = UpdateChecker.GSON.fromJson(jsonString, Versions.class);
        LOGGER.info("Latest addon version: " + this.getLatestVersion());
        this.isLatestVersion = SidebarAddon.VERSION.equals(this.getLatestVersion());
        LOGGER.info("Running latest version: " + this.isLatestVersion());
    }

    @InvokeEvent
    public void join(ServerJoinEvent event) {
        if(!Objects.equals(getLatestVersion(), SidebarAddon.VERSION)) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(ChatUtils.of("SidebarMod needs an update! Version: " + getLatestVersion() + "." + " Go to: https://github.com/Amplifiable2/SidebarAddon/releases to download the latest release.").setColor(EnumChatFormatting.GREEN).setBold(false).build());
        }
    }

    private boolean isLatestVersion() {
        return isLatestVersion;
    }

    private String getLatestVersion() {
        return UpdateChecker.PRERELEASE ? versions.getLatestPre() : versions.getLatestStable();
    }
}