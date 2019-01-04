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

package cc.hyperium.mods.spotify;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.integrations.spotify.Spotify;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.util.Collections;
import java.util.List;

public class SpotifyCommand implements BaseCommand {
    @Override
    public String getName() {
        return "spotifycontrols";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("spotify");
    }

    @Override
    public String getUsage() {
        return "Usage: " + getName();
    }

    @Override
    public void onExecute(String[] args) {
        if (Spotify.instance == null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(
                new ChatComponentText(ChatColor.RED + "Spotify is currently disabled as they made an API change on their end. We're looking into ways we can reintroduce it."));

            return;
        }

        new SpotifyGui().show();
    }
}
