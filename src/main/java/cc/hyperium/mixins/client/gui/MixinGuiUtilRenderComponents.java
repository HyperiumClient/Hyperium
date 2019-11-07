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

package cc.hyperium.mixins.client.gui;

import cc.hyperium.mixinsimp.client.gui.HyperiumGuiUtilRenderComponents;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;

@Mixin(GuiUtilRenderComponents.class)
abstract class MixinGuiUtilRenderComponents {

    /**
     * @author Sk1er
     * @reason Fixed next line resetting chat formatting
     */
    @Overwrite
    public static List<IChatComponent> splitText(IChatComponent chatComponent, int maxTextLength, FontRenderer fontRenderer, boolean p_178908_3_, boolean forceTextColor) {
        return HyperiumGuiUtilRenderComponents.splitText(chatComponent, maxTextLength, fontRenderer, p_178908_3_, forceTextColor);
    }
}
