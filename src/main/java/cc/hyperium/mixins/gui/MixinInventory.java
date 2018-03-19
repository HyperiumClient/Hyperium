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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(InventoryEffectRenderer.class)
public abstract class MixinInventory extends GuiContainer {

    @Shadow
    private boolean hasActivePotionEffects;

    public MixinInventory(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    /**
     * Removes the inventory going to the left once potion effects have worn out
     *
     * @author Kevin
     */
    @Overwrite
    protected void updateActivePotionEffects() {
        this.hasActivePotionEffects = !Minecraft.getMinecraft().thePlayer.getActivePotionEffects().isEmpty();
        this.guiLeft = (this.width - this.xSize) / 2;
    }

}