package cc.hyperium.mixins.client;

import cc.hyperium.mixinsimp.client.HyperiumGameSettings;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GameSettings.class)
public abstract class MixinGameSettings {

    private HyperiumGameSettings hyperiumGameSettings = new HyperiumGameSettings();

    /**
     * @author boomboompower
     * @reason Hyperium keybinds
     */
    @Overwrite
    public void setOptionKeyBinding(KeyBinding binding, int value) {
        hyperiumGameSettings.setOptionKeyBinding(binding, value);
    }
}
