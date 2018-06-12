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

package cc.hyperium.handlers.handlers.privatemessages;


import cc.hyperium.config.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public class PrivateMessageHandler {

    private final Map<String, PrivateMessageChat> chats = new HashMap<>();

    public PrivateMessageChat getChat(String with) {
        return chats.computeIfAbsent(with.toLowerCase(), tmp -> new PrivateMessageChat(with));
    }

    public List<PrivateMessageChat> getAllInOrder() {
        List<PrivateMessageChat> chats = new ArrayList<>();
        Set<Map.Entry<String, PrivateMessageChat>> entries = this.chats.entrySet();
        for (Map.Entry<String, PrivateMessageChat> entry : entries) {
            chats.add(entry.getValue());
        }
        chats.sort((o1, o2) -> Long.compare(o2.getLastAction(), o1.getLastAction()));
        return chats;
    }

    public void outboundMessage(String to, String message) {
        getChat(to).newMessage(message, to, true);
    }

    public void inboundMessage(String from, String message) {
        if (Settings.PING_ON_DM) {
            SoundHandler soundHandler = Minecraft.getMinecraft().getSoundHandler();
            if (soundHandler != null && Minecraft.getMinecraft().theWorld != null) {
                soundHandler.playSound(PositionedSoundRecord.create(new ResourceLocation("zoo"), (float) Minecraft.getMinecraft().thePlayer.posX, (float) Minecraft.getMinecraft().thePlayer.posY, (float) Minecraft.getMinecraft().thePlayer.posZ));
            }

        }
        getChat(from).newMessage(message, from, false);
    }


}
