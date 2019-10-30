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

package cc.hyperium.handlers.handlers.chat;

import cc.hyperium.config.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;

import java.util.regex.Matcher;

public class DMChatHandler extends HyperiumChatHandler {


    @Override
    public boolean chatReceived(IChatComponent component, String text) {
        if (!Settings.PING_ON_DM)
            return false;

        Matcher matcher = regexPatterns.get(ChatRegexType.PRIVATE_MESSAGE_FROM).matcher(text);
        if (matcher.matches()) {
            SoundHandler soundHandler = Minecraft.getMinecraft().getSoundHandler();
            if (soundHandler != null && Minecraft.getMinecraft().theWorld != null) {
                soundHandler.playSound(PositionedSoundRecord.create(new ResourceLocation("note.pling"),
                    (float) Minecraft.getMinecraft().thePlayer.posX, (float) Minecraft.getMinecraft().thePlayer.posY, (float) Minecraft.getMinecraft().thePlayer.posZ));
            }
        }

        return false;
    }
}
