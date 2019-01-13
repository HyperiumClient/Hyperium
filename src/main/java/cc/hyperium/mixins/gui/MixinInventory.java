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

package cc.hyperium.mixins.gui;

import cc.hyperium.mixinsimp.gui.HyperiumInventory;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(InventoryEffectRenderer.class)
public abstract class MixinInventory extends GuiContainer {

    private HyperiumInventory hyperiumInventory = new HyperiumInventory((InventoryEffectRenderer) (Object) this);

    public MixinInventory(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    /**
     * @author Kevin
     * @reason Removes the inventory going to the left once potion effects have worn out
     */
    @Overwrite
    protected void updateActivePotionEffects() {
        hyperiumInventory.updateActivePotionEffects(this.xSize);
    }

}
