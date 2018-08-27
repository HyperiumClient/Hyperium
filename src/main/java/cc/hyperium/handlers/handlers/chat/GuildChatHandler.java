package cc.hyperium.handlers.handlers.chat;

import cc.hyperium.config.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;

public class GuildChatHandler extends HyperiumChatHandler {

    @Override
    public boolean chatReceived(IChatComponent component, String text) {
        String playerJoinEndStr = " joined the guild!";
        if (text.endsWith(playerJoinEndStr) && Settings.SEND_GUILD_WELCOME_MESSAGE) {
            int rankHeader = 0;
            if (text.contains("["))
                rankHeader = text.indexOf("]") + 1;

            String playerName = String.valueOf(text.subSequence(rankHeader, text.length() - playerJoinEndStr.length())).trim();
            String message = "/gc Welcome to the guild " + playerName + "!";
            System.out.println(message);

            Minecraft.getMinecraft().thePlayer.sendChatMessage(message);
        }

        return false;
    }
}
