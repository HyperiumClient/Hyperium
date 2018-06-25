package cc.hyperium.mixins.client;

import cc.hyperium.Hyperium;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GameSettings.class)
public abstract class MixinGameSettings {

    /**
     * @author boomboompower
     * @reason Hyperium keybinds
     */
    /*@Overwrite
    public void setOptionKeyBinding(KeyBinding binding, int value) {
        binding.setKeyCode(value);

        if (binding instanceof HyperiumBind) {
            Hyperium.INSTANCE.getHandlers().getKeybindHandler().getKeyBindConfig().save();
        } else {
            this.saveOptions();
        }
    }*/

    @Shadow
    public abstract void saveOptions();
}
