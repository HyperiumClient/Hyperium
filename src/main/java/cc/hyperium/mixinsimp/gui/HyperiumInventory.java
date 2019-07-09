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

package cc.hyperium.mixinsimp.gui;

import cc.hyperium.mixins.gui.IMixinGuiContainer;
import cc.hyperium.mixins.gui.IMixinInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.InventoryEffectRenderer;

public class HyperiumInventory {
    private InventoryEffectRenderer parent;

    public HyperiumInventory(InventoryEffectRenderer parent) {
        this.parent = parent;
    }

    public void updateActivePotionEffects(int xSize) {
        ((IMixinInventory) parent).setHasActivePotionEffects(!Minecraft.getMinecraft().thePlayer.getActivePotionEffects().isEmpty());
        ((IMixinGuiContainer) parent).setGuiLeft((parent.width - xSize) / 2);
    }
}
