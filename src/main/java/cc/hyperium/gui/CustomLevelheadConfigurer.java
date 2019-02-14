package cc.hyperium.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.packets.serverbound.ServerCrossDataPacket;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UUIDUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

/**
 * Created by mitchellkatz on 5/2/18. Designed for production use on Sk1er.club
 */
public class CustomLevelheadConfigurer extends HyperiumGui {
    int cooldown = 0;
    private GuiTextField header;
    private GuiTextField level;
    private JsonHolder levelhead_propose = new JsonHolder();

    @Override
    public void initGui() {
        super.initGui();
        int i = ResolutionUtil.current().getScaledWidth() - 20;
        header = new GuiTextField(nextId(), fontRendererObj, 5, 30, i / 2, 20);
        level = new GuiTextField(nextId(), fontRendererObj, ResolutionUtil.current().getScaledWidth() / 2 + 5, 30, i / 2, 20);
        Multithreading.runAsync(() -> {
            JsonHolder jsonHolder = PurchaseApi.getInstance().get("https://sk1er.club/newlevel/" + UUIDUtil.getUUIDWithoutDashes());
            header.setText(jsonHolder.optString("header"));
            level.setText(jsonHolder.optString("true_footer"));
        });
        Multithreading.runAsync(() -> levelhead_propose = PurchaseApi.getInstance().get("https://api.hyperium.cc/levelhead_propose/" + UUIDUtil.getUUIDWithoutDashes()));
        Multithreading.runAsync(() -> {
            JsonHolder jsonHolder = PurchaseApi.getInstance().get("https://api.hyperium.cc/levelhead/" + UUIDUtil.getUUIDWithoutDashes());
            if (!jsonHolder.optBoolean("levelhead")) {
                if (Minecraft.getMinecraft().currentScreen instanceof CustomLevelheadConfigurer) {
                    mc.displayGuiScreen(null);
                    GeneralChatHandler.instance().sendMessage("You must purchase Custom Levelhead to use this!");
                }
            }
        });
        refresh();
    }

    @Override
    protected void pack() {
        int i = ResolutionUtil.current().getScaledWidth() - 20;

        NettyClient client1 = NettyClient.getClient();
        reg("RESET", new GuiButton(nextId(), 5, 55, i / 2, 20, "Reset to default"), button -> {
            if (client1 != null)
                client1.write(ServerCrossDataPacket.build(new JsonHolder().put("levelhead_propose", true).put("reset", true).put("internal", true)));
        }, button -> {
        });
        reg("PROPOSE", new GuiButton(nextId(), ResolutionUtil.current().getScaledWidth() / 2 + 5, 55, i / 2, 20, "Send for review"), button -> {
            ServerCrossDataPacket build = ServerCrossDataPacket.build(new JsonHolder().put("levelhead_propose", true).put("internal", true).put("propose", true).put("header", header.getText()).put("level", level.getText()));
            if (client1 != null)
                client1.write(build);
        }, button -> {

        });
        reg("Refresh", new GuiButton(nextId(), ResolutionUtil.current().getScaledWidth() / 2 - i / 4, 80, i / 2, 20, "Refresh"), button -> {
            refresh();
            cooldown = 0;
        }, button -> {
            cooldown++;
            button.visible = cooldown > 20;
        });
    }

    public void refresh() {
        Multithreading.runAsync(() -> {
            JsonHolder jsonHolder = PurchaseApi.getInstance().get("https://sk1er.club/newlevel/" + UUIDUtil.getUUIDWithoutDashes());
            header.setText(jsonHolder.optString("header"));
            level.setText(jsonHolder.optString("true_footer"));
        });
        Multithreading.runAsync(() -> {
            levelhead_propose = PurchaseApi.getInstance().get("https://api.hyperium.cc/levelhead_propose/" + UUIDUtil.getUUIDWithoutDashes());

        });
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        header.drawTextBox();
        level.drawTextBox();
        int stringWidth = fontRendererObj.getStringWidth("Custom Levelhead Configurer");
        drawCenteredString(mc.fontRendererObj, ChatColor.YELLOW + "Custom Levelhead Configurer", this.width / 2, 10, Color.WHITE.getRGB());
        drawHorizontalLine(this.width / 2 - stringWidth / 2 - 5, this.width / 2 + stringWidth / 2 + 5, 20, Color.WHITE.getRGB());
        if (levelhead_propose.getKeys().size() == 0) {
            drawCenteredString(fontRendererObj, ChatColor.RED + "Loading: " + ChatColor.RED + "Denied", width / 2, 115, Color.WHITE.getRGB());

            return;
        }
        if (levelhead_propose.optBoolean("denied")) {
            drawCenteredString(fontRendererObj, ChatColor.WHITE + "Status: " + ChatColor.RED + "Denied", width / 2, 115, Color.WHITE.getRGB());
            return;
        }
        if (levelhead_propose.optBoolean("enabled")) {
            int i = 115;
            drawCenteredString(fontRendererObj, ChatColor.WHITE + "Status: " + ChatColor.GREEN + "Accepted", width / 2, i - 10, Color.WHITE.getRGB());

            List<String> header = fontRendererObj.listFormattedStringToWidth(ChatColor.YELLOW + "Header: " + ChatColor.AQUA + levelhead_propose.optString("header"), width - 20);
            for (String s : header) {
                drawCenteredString(fontRendererObj, s, width / 2, i, Color.WHITE.getRGB());
                i += 10;
            }
            header = fontRendererObj.listFormattedStringToWidth(ChatColor.YELLOW + "Level: " + ChatColor.AQUA + levelhead_propose.optString("strlevel"), width - 20);
            for (String s : header) {
                drawCenteredString(fontRendererObj, s, width / 2, i, Color.WHITE.getRGB());
                i += 10;
            }
        } else {
            int i = 115;

            drawCenteredString(fontRendererObj, ChatColor.WHITE + "Status: " + ChatColor.YELLOW + "Pending", width / 2, i - 10, Color.WHITE.getRGB());
            List<String> header = fontRendererObj.listFormattedStringToWidth(ChatColor.YELLOW + "Header: " + ChatColor.AQUA + levelhead_propose.optString("header"), width - 20);
            for (String s : header) {
                drawCenteredString(fontRendererObj, s, width / 2, i, Color.WHITE.getRGB());
                i += 10;
            }
            header = fontRendererObj.listFormattedStringToWidth(ChatColor.YELLOW + "Level: " + ChatColor.AQUA + levelhead_propose.optString("strlevel"), width - 20);
            for (String s : header) {
                drawCenteredString(fontRendererObj, s, width / 2, i, Color.WHITE.getRGB());
                i += 10;
            }
            drawCenteredString(fontRendererObj, ChatColor.YELLOW + "It will be reviewed by a Hyperium Admin soon!", width / 2, i, Color.WHITE.getRGB());


        }

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        header.mouseClicked(mouseX, mouseY, mouseButton);
        level.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        header.textboxKeyTyped(typedChar, keyCode);
        level.textboxKeyTyped(typedChar, keyCode);
    }
}
