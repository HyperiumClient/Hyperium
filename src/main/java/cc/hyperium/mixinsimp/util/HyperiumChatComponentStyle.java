package cc.hyperium.mixinsimp.util;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class HyperiumChatComponentStyle {

    private String cache;

    public void invalidateCache() {
        cache = null;
    }

    public void getFormattedTextHeader(CallbackInfoReturnable<String> string) {
        if (cache != null) string.setReturnValue(cache);
    }

    public void getFormattedTextReturn(CallbackInfoReturnable<String> string) {
        cache = string.getReturnValue();
    }
}
