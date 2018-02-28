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
    private List<Long> timestampList = Lists.<Long>newArrayList();
    /**
     * Current profiling section
     */
    @Shadow
    private String profilingSection = "";

    @Inject(method = "endSection", at = @At("HEAD"))
    private void endSection(CallbackInfo ci) {
        if (this.profilingEnabled) {
            long i = System.nanoTime();
            long j = (Long) this.timestampList.get(this.timestampList.size() - 1);
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
