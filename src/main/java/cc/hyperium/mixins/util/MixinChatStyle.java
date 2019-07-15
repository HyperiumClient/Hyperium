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

package cc.hyperium.mixins.util;

import cc.hyperium.mixinsimp.util.HyperiumChatStyle;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatStyle.class)
public abstract class MixinChatStyle {

    @Shadow private ChatStyle parentStyle;
    @Shadow public abstract boolean isEmpty();
    @Shadow public abstract EnumChatFormatting getColor();
    @Shadow public abstract boolean getBold();

    private HyperiumChatStyle hyperiumChatStyle = new HyperiumChatStyle((ChatStyle) (Object) (this));

    /**
     * @author Sk1er
     * @reason Fix a few inefficient methods
     */
    @Overwrite
    public String getFormattingCode() {
        return hyperiumChatStyle.getFormattingCode(parentStyle);
    }

    /**
     * @author Sk1er
     */
    @Inject(method = "setColor", at = @At("HEAD"))
    private void setColor(EnumChatFormatting color, CallbackInfoReturnable<ChatStyle> info) {
        hyperiumChatStyle.resetCache();
    }

    /**
     * @author Sk1er
     */
    @Inject(method = "setBold", at = @At("HEAD"))
    private void setBold(Boolean boldIn, CallbackInfoReturnable<ChatStyle> callbackInfoReturnable) {
        hyperiumChatStyle.resetCache();
    }

    /**
     * @author Sk1er
     */
    @Inject(method = "setItalic", at = @At("HEAD"))
    private void setItalic(Boolean boldIn, CallbackInfoReturnable<ChatStyle> callbackInfoReturnable) {
        hyperiumChatStyle.resetCache();
    }

    /**
     * @author Sk1er
     */
    @Inject(method = "setStrikethrough", at = @At("HEAD"))
    private void setStrikethrough(Boolean boldIn, CallbackInfoReturnable<ChatStyle> callbackInfoReturnable) {
        hyperiumChatStyle.resetCache();
    }

    /**
     * @author Sk1er
     */
    @Inject(method = "setUnderlined", at = @At("HEAD"))
    private void setUnderlined(Boolean boldIn, CallbackInfoReturnable<ChatStyle> callbackInfoReturnable) {
        hyperiumChatStyle.resetCache();
    }

    /**
     * @author Sk1er
     */
    @Inject(method = "setObfuscated", at = @At("HEAD"))
    private void setObfuscated(Boolean boldIn, CallbackInfoReturnable<ChatStyle> callbackInfoReturnable) {
        hyperiumChatStyle.resetCache();
    }

    /**
     * @author Sk1er
     */
    @Inject(method = "setChatClickEvent", at = @At("HEAD"))
    private void setChatClickEvent(ClickEvent boldIn, CallbackInfoReturnable<ChatStyle> callbackInfoReturnable) {
        hyperiumChatStyle.resetCache();
    }

    /**
     * @author Sk1er
     */
    @Inject(method = "setChatHoverEvent", at = @At("HEAD"))
    private void setChatHoverEvent(HoverEvent boldIn, CallbackInfoReturnable<ChatStyle> callbackInfoReturnable) {
        hyperiumChatStyle.resetCache();
    }

    /**
     * @author Sk1er
     */
    @Inject(method = "setInsertion", at = @At("HEAD"))
    private void setChatHoverEvent(String boldIn, CallbackInfoReturnable<ChatStyle> callbackInfoReturnable) {
        hyperiumChatStyle.resetCache();
    }

    /**
     * @author Sk1er
     */
    @Inject(method = "setParentStyle", at = @At("HEAD"))
    private void setChatHoverEvent(ChatStyle boldIn, CallbackInfoReturnable<ChatStyle> callbackInfoReturnable) {
        hyperiumChatStyle.resetCache();
    }
}
