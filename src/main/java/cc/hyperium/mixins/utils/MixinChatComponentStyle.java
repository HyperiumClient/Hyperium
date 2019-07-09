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

package cc.hyperium.mixins.utils;

import cc.hyperium.mixinsimp.utils.HyperiumChatComponentStyle;
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatComponentStyle.class)
public abstract class MixinChatComponentStyle implements IChatComponent {

    private HyperiumChatComponentStyle hyperiumChatComponentStyle = new HyperiumChatComponentStyle((ChatComponentStyle) (Object) this);

    @Shadow
    public abstract ChatStyle getChatStyle();

    @Inject(method = "setChatStyle", at = @At("HEAD"), cancellable = true)
    public void setChatStyle(ChatStyle style, CallbackInfoReturnable<IChatComponent> ci) {
        hyperiumChatComponentStyle.invalidateCache();
    }


    @Inject(method = "getFormattedText", at = @At("HEAD"), cancellable = true)
    public void getFormatedTextHeader(CallbackInfoReturnable<String> string) {
        hyperiumChatComponentStyle.getFormatedTextHeader(string);
    }

    @Inject(method = "getFormattedText", at = @At("RETURN"), cancellable = true)
    public void getFormatedTextReturn(CallbackInfoReturnable<String> string) {
        hyperiumChatComponentStyle.getFormatedTextReturn(string);
    }

}
