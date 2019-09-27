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

package cc.hyperium.mods.keystrokes;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.mods.keystrokes.screen.GuiScreenKeystrokes;

import java.util.Collections;
import java.util.List;

public class CommandKeystrokes implements BaseCommand {

    private final KeystrokesMod mod;

    CommandKeystrokes(KeystrokesMod mod) {
        this.mod = mod;
    }

    @Override
    public String getName() {
        return "keystrokesmod";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("keystrokes");
    }

    @Override
    public String getUsage() {
        return "Usage: " + getName();
    }

    @Override
    public void onExecute(String[] args) {
        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiScreenKeystrokes(mod));
    }
}
