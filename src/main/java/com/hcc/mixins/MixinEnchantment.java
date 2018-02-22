package com.hcc.mixins;

import com.hcc.gui.settings.items.GeneralSetting;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.StatCollector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class MixinEnchantment {

    @Shadow public abstract String getName();

    @Inject(method = "getTranslatedName", at = @At("HEAD"), cancellable = true)
    private void getTranslatedName(int level, CallbackInfoReturnable<String> ci)
    {
        if(!GeneralSetting.romanNumeralsEnabled) {
            String s = StatCollector.translateToLocal(this.getName());
        //    String binary = "00000000";
        //    binary = binary.substring(Integer.toBinaryString(level).length()) + Integer.toBinaryString(level);
            ci.setReturnValue(s + " " + level);
        }
    }
}
