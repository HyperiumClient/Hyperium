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

package cc.hyperium.handlers.handlers.chat;

import cc.hyperium.Hyperium;
import cc.hyperium.handlers.handlers.remoteresources.HyperiumResource;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.util.IChatComponent;

import java.util.regex.Pattern;

/**
 * @author Sk1er
 */
public abstract class HyperiumChatHandler {
    //Resource *should* be loaded by then sooooo
    protected static HyperiumResource regexs;
    protected static Pattern guildChatPattern = null;
    protected static Pattern partyChatPattern = null;
    protected static Pattern skywarsRankedRating = null;
    protected static Pattern privateMessageTo = null;
    protected static Pattern privateMessageFrom = null;
    protected static Pattern completePattern = null;


    public Hyperium getHyperium() {
        return Hyperium.INSTANCE;
    }

    /**
     * @param component Entire component from evnet
     * @param text      Pure text for parsign
     * @return boolean to cancel event
     */
    public abstract boolean chatReceived(IChatComponent component, String text);

    public void callback(JsonHolder data) {

    }
}
