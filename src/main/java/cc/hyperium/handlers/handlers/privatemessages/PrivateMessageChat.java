/*
 * Hyperium Client, Free client with huds and popular mod
 *     Copyright (C) 2018  Hyperium Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.handlers.handlers.privatemessages;

import cc.hyperium.Hyperium;
import club.sk1er.website.api.requests.HypixelApiPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PrivateMessageChat implements Comparator<PrivateMessage> {
    private String to;
    private List<PrivateMessage> messages;
    private long lastAction = System.currentTimeMillis();

    public PrivateMessageChat(String to) {
        this.to = to;
        messages = new ArrayList<>();
    }

    public List<PrivateMessage> getMessages() {
        return messages;
    }

    public HypixelApiPlayer getOtherPlayer() {
        return Hyperium.INSTANCE.getHandlers().getDataHandler().getPlayer(getTo());
    }

    public void newMessage(String message, String person, boolean self) {
        lastAction = System.currentTimeMillis();
        System.out.println("Message: " + message + ". self:" + self);
        messages.add(new PrivateMessage(person, message, self));
    }

    public String getToLower() {
        return getTo().toLowerCase();
    }

    public String getTo() {
        return to;
    }

    @Override
    public int compare(PrivateMessage o1, PrivateMessage o2) {
        return Long.compare(o2.getTime(), o1.getTime());
    }

    public long getLastAction() {
        return lastAction;
    }
}
