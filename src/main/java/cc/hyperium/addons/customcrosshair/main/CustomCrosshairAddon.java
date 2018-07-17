package cc.hyperium.addons.customcrosshair.main;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.AbstractAddon;
import cc.hyperium.addons.customcrosshair.command.CommandCustomCrosshair;
import cc.hyperium.addons.customcrosshair.crosshair.Crosshair;
import cc.hyperium.addons.customcrosshair.utils.SaveUtils;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.awt.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

public class CustomCrosshairAddon extends AbstractAddon {
    private Crosshair crosshair;
    private static CustomCrosshairAddon crosshairAddon;
    private String guiKeyBind;
    public static boolean showMessage;
    public static boolean showCredits = true;
    public static String VERSION = "0.5.3-hyperium";

    @Override
    public AbstractAddon init() {
        EventBus.INSTANCE.register(this);
        crosshairAddon = this;
        this.crosshair = new Crosshair();
        this.setGuiKeyBind("G");
        if (!SaveUtils.readSaveFile(this)) {
            SaveUtils.writeSaveFileDefault();
        }
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandCustomCrosshair());
        EventBus.INSTANCE.register(this.crosshair);
        EventBus.INSTANCE.register(new KeyInputHandler());

        return this;
    }

    @Override
    public Metadata getAddonMetadata() {
        AbstractAddon.Metadata metadata = new AbstractAddon.Metadata(this, "Custom Crosshair Addon", "0.5.3", "Amplifiable");
        metadata.setDisplayName(ChatColor.GREEN + "Custom Crosshair Addon");
        metadata.setDescription("Customise the Rich Presence of Hyperium");

        return metadata;
    }

    public void addChatMessage(String message) {
        String pre = "\u00a79[Custom Crosshair Addon] \u00a7r";
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation(pre + message));
    }

    @InvokeEvent
    public void onWorldChange(cc.hyperium.event.PacketReceivedEvent e) {
        if (showCredits) {
            addChatMessage("Thanks for installing Custom Crosshair Addon!");
            addChatMessage("This mod has been ported for Hyperium by Amplifiable.");
            addChatMessage("Credits:");
            addChatMessage("sparkless101 for creating the original mod");
            addChatMessage("boomboompower for fixing sliders. ");
            ChatComponentText text2 = new ChatComponentText("\u00a79[Custom Crosshair Addon] \u00a7rHis twitter: https://twitter.com/xBOOMBOOMPOWERx ");
            ChatStyle style = new ChatStyle();
            style.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://twitter.com/xBOOMBOOMPOWERx"));
            ChatStyle style2 = new ChatStyle();
            style2.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.youtube.com/channel/UC63_abmxSPyoGneeDPsVpmQ"));
            text2.setChatStyle(style);
            ChatComponentText text3 = new ChatComponentText("\u00a79[Custom Crosshair Addon] \u00a7rHis YouTube channel: https://www.youtube.com/channel/UC63_abmxSPyoGneeDPsVpmQ");
            text3.setChatStyle(style2);
            Minecraft.getMinecraft().thePlayer.addChatMessage(text2);
            Minecraft.getMinecraft().thePlayer.addChatMessage(text3);
            showCredits = false;
        }
    }

    public void openWebLink(String url) {
        try {
            URI location = new URI(url);
            Desktop.getDesktop().browse(location);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public CrosshairVersion sendLatestVersionGetRequest() {
        try {
            URL url = new URL("https://amplifiable.tk/files/customcrosshair2/versions.txt");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            int responseCode = connection.getResponseCode();
            List<String> getRequestOutput = IOUtils.readLines(HttpClients.createDefault().execute(new HttpGet(url.toString())).getEntity().getContent(), Charset.defaultCharset());
            for (String inputLine : getRequestOutput) {
                String lineVersion = inputLine.split(" ")[0];
                if (lineVersion.equals("1.8.9")) {
                    CrosshairVersion version = new CrosshairVersion();
                    version.downloadUrl = inputLine.split(" ")[2];
                    version.version = inputLine.split(" ")[1];
                    return version;
                }
            }
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static CustomCrosshairAddon getCrosshairMod() {
        return CustomCrosshairAddon.crosshairAddon;
    }

    public Crosshair getCrosshair() {
        return this.crosshair;
    }

    public void resetCrosshair() {
        this.crosshair = new Crosshair();
    }

    public String getGuiKeyBind() {
        return this.guiKeyBind;
    }

    public void setGuiKeyBind(String guiKeyBind) {
        this.guiKeyBind = guiKeyBind;
    }

    static {
        CustomCrosshairAddon.crosshairAddon = new CustomCrosshairAddon();
        CustomCrosshairAddon.showMessage = true;
    }
}
