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

package cc.hyperium.mixinsimp.util;

import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class HyperiumChatStyle {
    private String cachedState;
    private ChatStyle parent;

    public HyperiumChatStyle(ChatStyle parent) {
        this.parent = parent;
    }

    public String getFormattingCode(ChatStyle parentStyle) {
        if (cachedState != null) return cachedState;
        if (parent.isEmpty()) {
            return parentStyle != null ? parentStyle.getFormattingCode() : "";
        } else {
            StringBuilder stringbuilder = new StringBuilder();

            if (parent.getColor() != null) stringbuilder.append(parent.getColor());
            if (parent.getBold()) stringbuilder.append(EnumChatFormatting.BOLD);
            if (parent.getItalic()) stringbuilder.append(EnumChatFormatting.ITALIC);
            if (parent.getUnderlined()) stringbuilder.append(EnumChatFormatting.UNDERLINE);
            if (parent.getObfuscated()) stringbuilder.append(EnumChatFormatting.OBFUSCATED);
            if (parent.getStrikethrough()) stringbuilder.append(EnumChatFormatting.STRIKETHROUGH);

            String s = stringbuilder.toString();
            cachedState = s;
            return s;
        }
    }

    public void resetCache() {
        cachedState = null;
    }
}
