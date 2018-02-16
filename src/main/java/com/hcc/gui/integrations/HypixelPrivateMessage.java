package com.hcc.gui.integrations;

import club.sk1er.website.api.requests.HypixelApiPlayer;
import com.hcc.HCC;
import com.hcc.gui.GuiBlock;
import com.hcc.gui.HCCGui;
import com.hcc.handlers.handlers.privatemessages.PrivateMessage;
import com.hcc.handlers.handlers.privatemessages.PrivateMessageChat;
import com.hcc.mods.sk1ercommon.ResolutionUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HypixelPrivateMessage extends HCCGui {

    private PrivateMessageChat chat;
    private GuiTextField text;
    private boolean lockDown = false;

    public HypixelPrivateMessage(PrivateMessageChat chat) {
        this.chat = chat;

    }

    @Override
    public void initGui() {
        super.initGui();

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        text.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        HypixelApiPlayer otherPlayer = chat.getOtherPlayer();
        String name;
        if (otherPlayer.isLoaded()) {
            if (otherPlayer.isValid()) {
                name = otherPlayer.getDisplayString() + "";
            } else {
                lockDown = true;
                name = "Invalid player";
            }
        } else name = "Loading..";
        drawCenteredString(fontRendererObj, "Chatting with " + name, ResolutionUtil.current().getScaledWidth() / 2, 20, Color.WHITE.getRGB());
        if (lockDown)
            return;
        text.drawTextBox();
        final int bottom = ResolutionUtil.current().getScaledHeight() - 30;
        final int top = 40;
        chat.getMessages().sort(chat);
        int yLevel = bottom - 10;
        int leftSideX = ResolutionUtil.current().getScaledWidth() / 2 - ResolutionUtil.current().getScaledWidth() / 6;
        int rightSideX = ResolutionUtil.current().getScaledWidth() / 2 + ResolutionUtil.current().getScaledWidth() / 6;

        for (PrivateMessage privateMessage : chat.getMessages()) {
            boolean user = privateMessage.isUser();
            String message = privateMessage.getMessage();
            long time = privateMessage.getTime();
            GuiBlock block = new GuiBlock(leftSideX, rightSideX, yLevel, yLevel);
            block.setExpandRight(!user);
            //Compound message into the list of strings and expand boxes to ensure space
            List<String> messages = breakIntoBits(message, rightSideX - leftSideX - 25, true);
            block.ensureHeight(messages.size() * 11, false);
            Gui.drawRect(block.getLeft() + (user ? 25 : 0), block.getTop(), block.getRight() + (user ? 0 : -25), block.getBottom(), user ? new Color(0, 0, 255, 100).getRGB() : new Color(0, 255, 0, 100).getRGB());
            int y = 1;
            for (String s : messages) {
                block.drawString(s, fontRendererObj, true, false, user ? 5 : 0, y, false, false, Color.WHITE.getRGB(), !user);
//                System.out.println(s);
                y += 11;
            }
            long delta = System.currentTimeMillis() - time;
            delta /= 1000;
            String timeString = "";
            if (delta < 60) {
                timeString = Long.toString(delta) + "s";
            } else {
                delta /= 60;
                if (delta <= 60) {
                    timeString = Long.toString(delta) + "m";
                } else {
                    delta /= 60;
                    timeString = Long.toString(delta) + "h";
                }
            }

            fontRendererObj.drawString(timeString, user ? block.getRight() + 2 : block.getLeft() - fontRendererObj.getStringWidth(timeString) - 5, block.getTop()+3+(10*(messages.size()-1)), Color.GRAY.getRGB(),true);

            yLevel -= block.getHeight();
            yLevel -= 5;


        }

    }

    @Override
    protected void pack() {
        String lastText = null;
        if (text != null)
            lastText = text.getText();
        int width = ResolutionUtil.current().getScaledWidth() / 3;
        text = new GuiTextField(nextId(), fontRendererObj, ResolutionUtil.current().getScaledWidth() / 2 - width / 2, ResolutionUtil.current().getScaledHeight() - 30, width, 20);
        //4 times normal chat
        text.setMaxStringLength(256 * 4);
        if (lastText != null)
            text.setText(lastText);
    }

    public List<String> breakIntoBits(String input, int charsPerMessage, boolean fontObj) {
        String[] split = StringUtils.split(input);
        List<String> messages = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (String s : split) {
            if (fontObj ? fontRendererObj.getStringWidth(s) >= charsPerMessage : s.length() >= charsPerMessage) {
                char[] chars = s.toCharArray();
                for (char aChar : chars) {
                    builder.append(aChar);
                    if (fontObj ? fontRendererObj.getStringWidth(builder.toString()) >= charsPerMessage : builder.length() >= charsPerMessage) {
                        messages.add(builder.toString());
                        builder = new StringBuilder();
                        builder.append(aChar);
                    }
                }
            } else {
                if (fontObj ? fontRendererObj.getStringWidth(builder.toString() + " " + s) < charsPerMessage : builder.length() + 1 + s.length() < charsPerMessage) {
                    builder.append(" ").append(s);
                } else {
                    messages.add(builder.toString());
                    builder = new StringBuilder();
                    builder.append(s);
                }
            }
        }
        if (builder.length() != 0)
            messages.add(builder.toString());
        return messages;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (lockDown)
            return;
        super.keyTyped(typedChar, keyCode);
        text.textboxKeyTyped(typedChar, keyCode);
        if (keyCode == Keyboard.KEY_RETURN) {
            String text1 = this.text.getText();
            String baseCommand = "/w " + chat.getTo() + " ";
            int charsPerMessage = 256 - baseCommand.length();

            for (String message : breakIntoBits(text1, charsPerMessage, false)) {
                HCC.INSTANCE.getHandlers().getCommandQueue().queue(baseCommand + message.trim());
            }

            text.setText("");
        }
    }
}
