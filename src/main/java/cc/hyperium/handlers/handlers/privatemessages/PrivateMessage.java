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

public class PrivateMessage {
    private final String with;
    private final String message;
    private final long time;
    private final boolean isUser;

    public PrivateMessage(String with, String message, boolean isUser) {
        this.with = with;
        this.message = message;
        this.isUser = isUser;
        this.time = System.currentTimeMillis();
    }

    public boolean isUser() {
        return isUser;
    }

    public String withLower() {
        return getWith().toLowerCase();
    }

    public String getWith() {
        return with;
    }

    public String getMessage() {
        return message;
    }

    public long getTime() {
        return time;
    }
}
