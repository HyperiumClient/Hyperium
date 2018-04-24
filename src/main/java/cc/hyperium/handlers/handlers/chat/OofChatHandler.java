package cc.hyperium.handlers.handlers.chat;

import cc.hyperium.Hyperium;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;

public class OofChatHandler extends HyperiumChatHandler {

    @Override
    public boolean chatReceived(IChatComponent component, String text) {
        String setting = "playOofWhenSpokenEnabled";
        JsonObject generalJsonObject = Hyperium.CONFIG.getConfig().get("cc.hyperium.gui.settings.items.GeneralSetting").getAsJsonObject();
        boolean playOofWhenSpoken;
        if (!generalJsonObject.has(setting)) {
            generalJsonObject.addProperty(setting, false);
            Hyperium.CONFIG.save();
        }
        playOofWhenSpoken = generalJsonObject.get(setting).getAsBoolean();
        if (text.toLowerCase().contains("oof") && playOofWhenSpoken) {
            Minecraft.getMinecraft().thePlayer.playSound("minecraft:oof", 1, 1);
        }
        return false;
    }
}
