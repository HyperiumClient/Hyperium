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
package cc.hyperium.mods.rodcolor.rodcolor;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.client.TickEvent;
import cc.hyperium.mods.AbstractMod;
import net.minecraft.client.Minecraft;

public class RodColor extends AbstractMod {

    protected static boolean openGUI = false;

    @Override
    public AbstractMod init() {
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandRodColor());
        EventBus.INSTANCE.register(this);
        return this;
    }

    @InvokeEvent
    public void onTick(TickEvent event) {
        if (openGUI) {
            Minecraft.getMinecraft().displayGuiScreen(new RodColorGui());
            openGUI = false;
        }
    }

    @Override
    public Metadata getModMetadata() {
        return new Metadata(this, "RodColor", "1.3", "Reflxction");
    }
}
