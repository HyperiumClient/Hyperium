package cc.hyperium.mixinsimp.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

public class HyperiumGameSettings {
    public void setOptionKeyBinding(KeyBinding binding, int value) {
        binding.setKeyCode(value);
        Minecraft.getMinecraft().gameSettings.saveOptions();
    }
}
