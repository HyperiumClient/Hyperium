package cc.hyperium.integrations.optifine;

import cc.hyperium.Hyperium;
import cc.hyperium.utils.ChatColor;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import java.awt.*;
import java.math.BigInteger;
import java.net.URL;
import java.util.Random;

public class GuiScreenCapeOF extends GuiScreen {

    @Override
    public void initGui() {
        buttonList.clear();
        buttonList.add(new GuiButton(0, width / 2 - 155, calculateHeight(), 155, 20, I18n.format("gui.optifinecape.opencape")));
        buttonList.add(new GuiButton(1, width / 2 + 5, calculateHeight(), 155, 20, I18n.format("gui.done")));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }


    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                try {
                    GameProfile gameProfile = Minecraft.getMinecraft().thePlayer.getGameProfile();
                    String username = gameProfile.getName();
                    String userId = gameProfile.getId().toString().replace("-", "");
                    String accessToken = Minecraft.getMinecraft().getSession().getToken();
                    Random random = new Random();
                    Random random2 = new Random(System.identityHashCode(new Object()));
                    BigInteger randomBigInt = new BigInteger(128, random);
                    BigInteger randomBigInt2 = new BigInteger(128, random2);
                    BigInteger serverBigInt = randomBigInt.xor(randomBigInt2);
                    String serverId = serverBigInt.toString(16);
                    Minecraft.getMinecraft().getSessionService().joinServer(gameProfile, accessToken, serverId);
                    String urlStr = "https://optifine.net/capeChange?u=" + userId + "&n=" + username + "&s=" + serverId;

                    try {
                        Desktop.getDesktop().browse(new URL(urlStr).toURI());
                    } catch (Exception e) {
                        IChatComponent urlComponent = new ChatComponentText(ChatColor.RED + "[Hyperium] " + ChatColor.GRAY + "Edit your Optifine cape.");
                        urlComponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, urlStr));
                        urlComponent.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(
                            ChatColor.GRAY + "Edit your Optifine cape using this url!"
                        )));
                        Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(urlComponent);
                        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(null);
                        e.printStackTrace();
                    }
                } catch (AuthenticationException e) {
                    e.printStackTrace();
                }

                break;

            case 1:
                mc.displayGuiScreen(null);
                break;
        }
    }

    private int calculateHeight() {
        return 55 + 4 * 23;
    }
}
