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

package cc.hyperium.handlers.handlers.chat;

import cc.hyperium.config.ConfigOpt;
import net.minecraft.util.IChatComponent;

public class AutoWhoChatHandler extends HyperiumChatHandler {
    @ConfigOpt
    private boolean enabled = true;

    @Override
    public boolean chatReceived(IChatComponent component, String text) {
        //Idk took this check from 2Pi's AutoWHO
        if (text.equalsIgnoreCase("Teaming is not allowed on Ranked Mode!")) {
            getHyperium().getHandlers().getCommandQueue().queue("/who");
        }
        return false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
