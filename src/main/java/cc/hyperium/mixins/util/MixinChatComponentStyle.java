package cc.hyperium.mixins.util;

import cc.hyperium.mixinsimp.util.HyperiumChatComponentStyle;
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

    private HyperiumChatComponentStyle hyperiumChatComponentStyle = new HyperiumChatComponentStyle();

    @Shadow
    public abstract ChatStyle getChatStyle();

    @Inject(method = "setChatStyle", at = @At("HEAD"), cancellable = true)
    private void setChatStyle(ChatStyle style, CallbackInfoReturnable<IChatComponent> ci) {
        hyperiumChatComponentStyle.invalidateCache();
    }

    @Inject(method = "getFormattedText", at = @At("HEAD"), cancellable = true)
    private void getFormattedTextHeader(CallbackInfoReturnable<String> string) {
        hyperiumChatComponentStyle.getFormattedTextHeader(string);
    }

    @Inject(method = "getFormattedText", at = @At("RETURN"), cancellable = true)
    private void getFormattedTextReturn(CallbackInfoReturnable<String> string) {
        hyperiumChatComponentStyle.getFormattedTextReturn(string);
    }
}
