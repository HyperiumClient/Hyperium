/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.gui.integrations;

import cc.hyperium.Hyperium;
import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.GuiBoxItem;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.handlers.handlers.privatemessages.PrivateMessage;
import cc.hyperium.handlers.handlers.privatemessages.PrivateMessageChat;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.RenderUtils;
import club.sk1er.website.api.requests.HypixelApiPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HypixelPrivateMessage extends HyperiumGui {

    private final PrivateMessageChat chat;
    private final List<GuiBoxItem<PrivateMessageChat>> chatBoxes = new ArrayList<>();
    private GuiTextField text;
    private boolean lockDown = false;

    //TODO add option to close PM's, autoremove players that do not exist, add a text box to start a new convo with a user. Add a darker color if unread messages and make the sorting go by unread messages.
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
        for (GuiBoxItem<PrivateMessageChat> chatBox : chatBoxes) {
            if (chatBox.getBox().isMouseOver(mouseX, mouseY)) {
                new HypixelPrivateMessage(chatBox.getObject()).show();
            }
        }
    }

    private boolean isGuildOrParty() {
        return isParty() || isGuild();
    }

    private boolean isParty() {
        return chat.getTo().equalsIgnoreCase("party");
    }

    private boolean isGuild() {
        return chat.getTo().equalsIgnoreCase("guild");
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        //draw recent messages
        this.chatBoxes.clear();
        List<PrivateMessageChat> allInOrder = Hyperium.INSTANCE.getHandlers().getPrivateMessageHandler().getAllInOrder();
        allInOrder.remove(chat);
        int max = (ResolutionUtil.current().getScaledHeight() - 50) / 30;
        if (allInOrder.size() > max)
            allInOrder = allInOrder.subList(0, max);

        GuiBlock lastBlock = new GuiBlock(10, Math.max(ResolutionUtil.current().getScaledWidth() / 9, 100) - 25, offset, offset + 20); //Shifted up by 20 because system takes next one
        lastBlock.ensureWidth(fontRendererObj.getStringWidth("################"), true);
        lastBlock.drawString("Recent chats", fontRendererObj, true, true, lastBlock.getWidth() / 2, 10, true, false, Color.RED.getRGB(), true);

        for (PrivateMessageChat privateMessageChat : allInOrder) {
            GuiBlock block = new GuiBlock(lastBlock.getLeft(), lastBlock.getRight(), lastBlock.getBottom() + 5, lastBlock.getBottom() + 25);
            Gui.drawRect(block.getLeft(), block.getTop(), block.getRight(), block.getBottom(), new Color(0, 0, 0, 60).getRGB());
            block.drawString(privateMessageChat.getTo(), fontRendererObj, true, true, block.getWidth() / 2, 6, true, false, Color.GREEN.getRGB(), true);
            chatBoxes.add(new GuiBoxItem<>(block, privateMessageChat));
            lastBlock = block;

        }

        String name = null;
        //Draw chat
        if (chat.getTo().equalsIgnoreCase("guild")) {
            name = ChatColor.GREEN.toString() + "Guild";
        } else if (chat.getTo().equalsIgnoreCase("party")) {
            name = ChatColor.BLUE.toString() + "Party";
        }
        //save api call by checking if it is a party or guild chat
        if (name == null) {
            HypixelApiPlayer otherPlayer = chat.getOtherPlayer();
            if (otherPlayer.isLoaded()) {
                if (otherPlayer.isValid()) {
                    boolean empty = otherPlayer.getDisplayString().isEmpty();
                    if (empty) {
                        if (otherPlayer.getName().isEmpty()) {
                            lockDown = true;
                            name = "Invalid player";
                        } else {
                            name = otherPlayer.getDisplayString();
                        }
                    } else {
                        name = otherPlayer.getDisplayString();
                    }
                } else {
                    lockDown = true;
                    name = "Invalid player";
                }
            } else name = "Loading..";
        }
        drawCenteredString(fontRendererObj, "Chatting with " + name, ResolutionUtil.current().getScaledWidth() / 2, 20, Color.WHITE.getRGB());
        if (lockDown) {
            Minecraft.getMinecraft().displayGuiScreen(null);
            GeneralChatHandler.instance().sendMessage("Player not found!");
        }
        text.drawTextBox();
        final int bottom = ResolutionUtil.current().getScaledHeight() - 30;
        final int top = 40;
        chat.getMessages().sort(chat);
        int yLevel = bottom - 10 + offset;
        int leftSideX = ResolutionUtil.current().getScaledWidth() / 2 - ResolutionUtil.current().getScaledWidth() / 6;
        int rightSideX = ResolutionUtil.current().getScaledWidth() / 2 + ResolutionUtil.current().getScaledWidth() / 6;
        String lastAuthor = "";
        for (PrivateMessage privateMessage : chat.getMessages()) {
            boolean user = privateMessage.isUser();
            String message = privateMessage.getMessage();
            if (message.isEmpty())
                continue;
            long time = privateMessage.getTime();
            GuiBlock block = new GuiBlock(leftSideX, rightSideX, yLevel, yLevel);
            block.setExpandRight(!user);
            //Compound message into the list of strings and expand boxes to ensure space
            List<String> messages = breakIntoBits(message, rightSideX - leftSideX - 25, true);
            block.ensureHeight(messages.size() * 11, false);
            if (block.getTop() < 20 || block.getBottom() > bottom - 10) {
                yLevel -= block.getHeight();
                yLevel -= 5;
                continue;
            }
//            Gui.drawRect(block.getLeft() + (user ? 25 : 3), block.getTop(), block.getRight() + (user ? 0 : -25), block.getBottom(), user ? new Color(0, 0, 255, 255).getRGB() : new Color(0, 255, 0, 255).getRGB());
//            RenderUtils.drawFilledCircle(block.getLeft() + (user ? 25 : 3), block.getTop() + 4, 4, user ? new Color(0, 0, 255, 255).getRGB() : new Color(0, 255, 0, 255).getRGB());
//            RenderUtils.drawFilledCircle(block.getLeft() + (user ? 25 : 3), block.getBottom() - 4, 4, user ? new Color(0, 0, 255, 255).getRGB() : new Color(0, 255, 0, 255).getRGB());
//            Gui.drawRect(block.getLeft()-4+(user ? 25 : 3), block.getTop() + 3, block.getLeft()+(user ? 25 : 3) , block.getBottom() - 3, user ? new Color(0, 0, 255, 200).getRGB() : new Color(0, 255, 0, 200).getRGB());
            RenderUtils.drawSmoothRect(block.getLeft() + (user ? 25 : 3), block.getTop(), block.getRight() + (user ? 0 : -25), block.getBottom(), user ? new Color(0, 0, 255, 255).getRGB() : new Color(0, 255, 0, 255).getRGB());
            int y = 1;
            for (String s : messages) {
                block.drawString(s.trim(), fontRendererObj, true, false, user ? 7 : 5, y, false, false, Color.WHITE.getRGB(), !user);
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
            if (isGuildOrParty())
                timeString += " " + privateMessage.getWith();
            if (!lastAuthor.equalsIgnoreCase(privateMessage.getWith()))
                fontRendererObj.drawString(timeString, user ? block.getRight() + 2 : block.getLeft() - fontRendererObj.getStringWidth(timeString) - 5, block.getTop() + 3 + (10 * (messages.size() - 1)), Color.GRAY.getRGB(), true);
            lastAuthor = privateMessage.getWith();
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
            String baseCommand;
            if (isGuildOrParty()) {
                if (isParty()) baseCommand = "/pchat ";
                else baseCommand = "/gchat ";
            } else baseCommand = "/w " + chat.getTo() + " ";
            int charsPerMessage = 100 - baseCommand.length();

            for (String message : breakIntoBits(text1, charsPerMessage, false)) {
                Hyperium.INSTANCE.getHandlers().getCommandQueue().queue(baseCommand + message.trim());
            }

            text.setText("");
        }
    }
}
