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

package cc.hyperium.addons.sidebar;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.AbstractAddon;
import cc.hyperium.addons.sidebar.commands.CommandSidebar;
import cc.hyperium.addons.sidebar.gui.GuiSidebar;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.render.RenderScoreboardEvent;

public class SidebarAddon extends AbstractAddon {

    private GuiSidebar guiSidebar;

    @Override
    public AbstractAddon init() {
        EventBus.INSTANCE.register(this);
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandSidebar(this));
        guiSidebar = new GuiSidebar();
        Hyperium.CONFIG.register(guiSidebar);
        return this;
    }

    @Override
    public Metadata getAddonMetadata() {
        Metadata metadata = new Metadata(this, "SidebarAddon", "1.0.1", "Amplifiable");
        metadata.setDescription("Allows for full scoreboard customization");
        return metadata;
    }

    public GuiSidebar getSidebarGui() {
        return guiSidebar;
    }

    @InvokeEvent
    public void renderScoreboard(RenderScoreboardEvent e) {
        e.setCancelled(true);
        guiSidebar.drawSidebar(e.getObjective(), e.getResolution());
    }
}
