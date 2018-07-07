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

import cc.hyperium.mixinsimp.gui.HyperiumGuiResourcePack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.ResourcePackListEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResourcePackListEntry.class)
public abstract class MixinGuiResourcePack {

    @Shadow
    @Final
    private Minecraft mc;

    @Shadow
    protected abstract int func_183019_a();

    @Shadow
    protected abstract String func_148312_b();

    @Shadow
    protected abstract String func_148311_a();

    private HyperiumGuiResourcePack hyperiumGuiResourcePack = new HyperiumGuiResourcePack((ResourcePackListEntry) (Object) this);

    /**
     * Fixes Minecraft's shitty rendering code
     *
     * @author Kevin Brewster
     */
    @Inject(method = "drawEntry", at = @At("HEAD"), cancellable = true)
    private void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight,
                           int mouseX, int mouseY, boolean isSelected, CallbackInfo ci) {
        hyperiumGuiResourcePack.drawEntry(slotIndex,x,y,listWidth,slotHeight,mouseX,mouseY,isSelected,this.mc,ci);
    }
}
