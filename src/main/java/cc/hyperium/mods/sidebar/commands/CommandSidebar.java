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

package cc.hyperium.mods.sidebar.commands;

import cc.hyperium.Hyperium;
import cc.hyperium.mods.sidebar.SidebarAddon;
import cc.hyperium.mods.sidebar.gui.screen.GuiScreenSettings;
import cc.hyperium.commands.BaseCommand;

import java.util.Arrays;
import java.util.List;

public class CommandSidebar implements BaseCommand {

  private final SidebarAddon addon;

  public CommandSidebar(SidebarAddon addon) {
    this.addon = addon;
  }

  @Override
  public String getName() {
    return "sidebaraddon";
  }

  @Override
  public String getUsage() {
    return "/sidebaraddon";
  }

  @Override
  public void onExecute(String[] strings) {
    Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler()
        .setDisplayNextTick(new GuiScreenSettings(addon));
  }

  @Override
  public List<String> getCommandAliases() {
    return Arrays.asList("sidebarmod", "sidebar");
  }
}
