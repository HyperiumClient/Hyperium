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

package cc.hyperium.mixinsimp.utils;

import net.minecraft.util.ChatComponentStyle;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class HyperiumChatComponentStyle {

    private ChatComponentStyle parent;
    private String cache;

    public HyperiumChatComponentStyle(ChatComponentStyle parent) {
        this.parent = parent;
    }

    public void invalidateCache() {
        this.cache = null;
    }

    public void getFormatedTextHeader(CallbackInfoReturnable<String> string) {
        if (cache != null)
            string.setReturnValue(cache);
    }

    public void getFormatedTextReturn(CallbackInfoReturnable<String> string) {
        this.cache = string.getReturnValue();
    }
}
