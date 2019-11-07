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

package cc.hyperium.mixinsimp.client.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import java.util.List;

public class HyperiumGuiUtilRenderComponents {

    public static List<IChatComponent> splitText(IChatComponent chatComponent, int maxTextLength, FontRenderer fontRenderer, boolean p_178908_3_, boolean forceTextColor) {
        int i = 0;
        IChatComponent ichatcomponent = new ChatComponentText("");
        List<IChatComponent> list = Lists.newArrayList();
        List<IChatComponent> list1 = Lists.newArrayList(chatComponent);

        for (int j = 0; j < list1.size(); ++j) {
            IChatComponent ichatcomponent1 = list1.get(j);
            String s = ichatcomponent1.getUnformattedTextForChat();
            boolean flag = false;

            if (s.contains("\n")) {
                int k = s.indexOf(10);
                String s1 = s.substring(k + 1);
                s = s.substring(0, k + 1);
                ChatComponentText chatcomponenttext = new ChatComponentText(s1);
                chatcomponenttext.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());
                list1.add(j + 1, chatcomponenttext);
                flag = true;
            }

            String s4 = GuiUtilRenderComponents.func_178909_a(ichatcomponent1.getChatStyle().getFormattingCode() + s, forceTextColor);
            String s5 = s4.endsWith("\n") ? s4.substring(0, s4.length() - 1) : s4;
            int i1 = fontRenderer.getStringWidth(s5);
            ChatComponentText chatcomponenttext1 = new ChatComponentText(s5);
            chatcomponenttext1.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());

            if (i + i1 > maxTextLength) {
                String s2 = fontRenderer.trimStringToWidth(s4, maxTextLength - i, false);
                String s3 = s2.length() < s4.length() ? s4.substring(s2.length()) : null;

                if (s3 != null && s3.length() > 0) {
                    int l = s2.lastIndexOf(" ");

                    if (l >= 0 && fontRenderer.getStringWidth(s4.substring(0, l)) > 0) {
                        s2 = s4.substring(0, l);

                        if (p_178908_3_) {
                            ++l;
                        }

                        s3 = s4.substring(l);
                    } else if (i > 0 && !s4.contains(" ")) {
                        s2 = "";
                        s3 = s4;
                    }
                    s3 = FontRenderer.getFormatFromString(s2) + s3;
                    ChatComponentText chatcomponenttext2 = new ChatComponentText(s3);
                    chatcomponenttext2.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());
                    list1.add(j + 1, chatcomponenttext2);
                }

                i1 = fontRenderer.getStringWidth(s2);
                chatcomponenttext1 = new ChatComponentText(s2);
                chatcomponenttext1.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());
                flag = true;
            }

            if (i + i1 <= maxTextLength) {
                i += i1;
                ichatcomponent.appendSibling(chatcomponenttext1);
            } else {
                flag = true;
            }

            if (flag) {
                list.add(ichatcomponent);
                i = 0;
                ichatcomponent = new ChatComponentText("");
            }
        }

        list.add(ichatcomponent);
        return list;
    }
}
