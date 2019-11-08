package cc.hyperium.mixins.util;

import cc.hyperium.Hyperium;
import net.minecraft.util.MouseHelper;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MouseHelper.class)
public class MixinMouseHelper {

    @Redirect(method = "ungrabMouseCursor", at = @At(value = "INVOKE", remap = false, target = "Lorg/lwjgl/input/Mouse;setCursorPosition(II)V", ordinal = 0))
    private void ungrabMouseCursor(int new_x, int new_y) {
        if (Hyperium.INSTANCE.getHandlers().getMouseListener().shouldResetMouse()) {
            Mouse.setCursorPosition(new_x, new_y);
        }
    }
}
