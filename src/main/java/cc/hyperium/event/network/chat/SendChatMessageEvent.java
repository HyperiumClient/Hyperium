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

/**
 * Invoked when the player presses the enter key in the GuiChat class
 */
public class SendChatMessageEvent extends CancellableEvent {

    private final String message;

    /**
     * @param message the message the player is sending
     */
    public SendChatMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
