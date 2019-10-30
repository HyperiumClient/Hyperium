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

package cc.hyperium.event.network.chat;

import cc.hyperium.event.CancellableEvent;
import net.minecraft.util.IChatComponent;

/**
 * Invoked when a chat packet is received, will not detect messages directly printed to the chat
 */
public class ServerChatEvent extends CancellableEvent {

    private final byte type;
    private IChatComponent chat;


    public ServerChatEvent(byte type, IChatComponent chat) {
        this.type = type;
        this.chat = chat;
    }

    public byte getType() {
        return type;
    }

    public IChatComponent getChat() {
        return chat;
    }

    public void setChat(IChatComponent chat) {
        this.chat = chat;
    }
}
