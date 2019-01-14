/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mixins.utils;

import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MathHelper.class)
class MixinMathHelper {
    @Shadow
    @Final
    private static float[] SIN_TABLE;

    /**
     * @author Cubxity and Mojang
     * @reason In case if we want to change sin and cos (no code changed from Vanilla)
     */
    @Overwrite
    public static float sin(float p_76126_0_) {
        return SIN_TABLE[(int) (p_76126_0_ * 10430.378F) & 65535];
    }

    /**
     * @author Cubxity and Mojang
     * @reason In case if we want to change sin and cos (no code changed from Vanilla)
     */
    @Overwrite
    public static float cos(float value) {
        return SIN_TABLE[(int) (value * 10430.378F + 16384.0F) & 65535];
    }

}
