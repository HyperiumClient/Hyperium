package cc.hyperium.mixinsimp;

import cc.hyperium.config.Settings;
import net.minecraft.util.StatCollector;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class HyperiumEnchantment {

    public void getTranslatedName(int level, CallbackInfoReturnable<String> ci, String name) {
        if (!Settings.ROMAN_NUMERALS) {
            String s = StatCollector.translateToLocal(name);
            //    String binary = "00000000";
            //    binary = binary.substring(Integer.toBinaryString(level).length()) + Integer.toBinaryString(level);
            ci.setReturnValue(s + " " + level);
        }
    }
}
