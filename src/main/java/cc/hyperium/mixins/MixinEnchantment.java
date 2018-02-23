/*
 * Hyperium Client, Free client with huds and popular mod
 *     Copyright (C) 2018  Hyperium Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mixins;

import cc.hyperium.gui.settings.items.GeneralSetting;
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
