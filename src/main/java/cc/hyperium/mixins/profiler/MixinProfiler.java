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

package cc.hyperium.mixins.profiler;

import com.google.common.collect.Lists;
import net.minecraft.profiler.Profiler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
@Mixin(Profiler.class)
public class MixinProfiler {


    /**
     * Flag profiling enabled
     */
    @Shadow
    public boolean profilingEnabled;
    @Shadow
    @Final
    private final List<Long> timestampList = Lists.<Long>newArrayList();
    /**
     * Current profiling section
     */
    @Shadow
    private String profilingSection = "";

    @Inject(method = "endSection", at = @At("HEAD"))
    private void endSection(CallbackInfo ci) {
        if (this.profilingEnabled) {
            long i = System.nanoTime();
            long j = this.timestampList.get(this.timestampList.size() - 1);
            long k = i - j;
           
            if (k > TimeUnit.MILLISECONDS.toNanos(20)) {
//                System.out.println(System.currentTimeMillis() + " Something\'s taking too long! \'" + this.profilingSection + "\' took aprox " + (double) k / 1000000.0D + " ms");
//                if (Minecraft.getMinecraft().renderGlobal == null) {
//
//                    System.out.println(Minecraft.getMinecraft().renderGlobal.getDebugInfoEntities());
//                    System.out.println(Minecraft.getMinecraft().renderGlobal.getDebugInfoRenders());
//                }
            }
        }
    }

}
