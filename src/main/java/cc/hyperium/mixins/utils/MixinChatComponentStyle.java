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
