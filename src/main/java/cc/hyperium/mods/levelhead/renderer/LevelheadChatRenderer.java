/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods.levelhead.renderer;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.network.chat.ServerChatEvent;
import cc.hyperium.mods.levelhead.Levelhead;
import cc.hyperium.mods.levelhead.display.DisplayConfig;
import cc.hyperium.mods.levelhead.display.LevelheadDisplay;
import cc.hyperium.utils.ChatColor;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

import java.util.List;
import java.util.UUID;

public class LevelheadChatRenderer {

    private Levelhead levelhead;

    public LevelheadChatRenderer(Levelhead levelhead) {
        this.levelhead = levelhead;
    }

    public static IChatComponent modifyChat(IChatComponent component, String tag, DisplayConfig config) {
        ChatComponentText text = new ChatComponentText(config.getHeaderColor() + "[" + config.getFooterColor() + tag + config.getHeaderColor() + "]" + ChatColor.RESET);
        text.appendSibling(component);
        return text;
    }

    @InvokeEvent
    public void chat(ServerChatEvent event) {
        if (!levelhead.getDisplayManager().getMasterConfig().isEnabled()) return;
        LevelheadDisplay chat = Levelhead.getInstance().getDisplayManager().getChat();
        if (chat == null || !levelhead.getLevelheadPurchaseStates().isChat() || !chat.getConfig().isEnabled()) return;
        List<IChatComponent> siblings = event.getChat().getSiblings();
        if (siblings.size() == 0) return;

        IChatComponent chatComponent = siblings.get(0);

        if (chatComponent instanceof ChatComponentText) {
            ChatStyle style = chatComponent.getChatStyle();
            ClickEvent clickEvent = style.getChatClickEvent();

            if (clickEvent != null && clickEvent.getAction() == ClickEvent.Action.RUN_COMMAND) {
                String value = clickEvent.getValue();

                HoverEvent hoverEvent = style.getChatHoverEvent();

                if (hoverEvent != null && hoverEvent.getAction() == HoverEvent.Action.SHOW_TEXT) {
                    String[] split = value.split(" ");

                    if (split.length == 2) {
                        String uuid = split[1];
                        UUID key = UUID.fromString(uuid);
                        String tag = chat.getTrueValueCache().get(key);

                        if (tag != null) {
                            event.setChat(modifyChat(event.getChat(), tag, chat.getConfig()));
                        } else if (!(chat.getCache().get(key) instanceof NullLevelheadTag)) {
                            levelhead.fetch(key, chat, false);
                        }
                    }
                }
            }
        }
    }
}
