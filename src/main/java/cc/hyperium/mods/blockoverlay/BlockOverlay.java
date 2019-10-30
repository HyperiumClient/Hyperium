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

package cc.hyperium.mods.blockoverlay;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;

public class BlockOverlay extends AbstractMod {
    public final static Minecraft mc = Minecraft.getMinecraft();

    private final Metadata meta;
    private BlockOverlaySettings settings;

    public BlockOverlay() {
        meta = new Metadata(this, "BlockOverlay", "1.0", "aycy & powns");
        meta.setDisplayName(ChatColor.RED + "BlockOverlay");
    }

    @Override
    public AbstractMod init() {
        settings = new BlockOverlaySettings(Hyperium.folder);
        settings.load();
        EventBus.INSTANCE.register(new BlockOverlayRender(this));
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new BlockOverlayCommand(this));
        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return meta;
    }

    public BlockOverlaySettings getSettings() {
        return settings;
    }
}
