package cc.hyperium.mixins;

import cc.hyperium.mixinsimp.HyperiumCrashReport;
import net.minecraft.crash.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrashReport.class)
public abstract class MixinCrashReport {

    private HyperiumCrashReport hyperiumCrashReport = new HyperiumCrashReport((CrashReport) (Object) this);

    @Inject(method = "populateEnvironment", at = @At("HEAD"))
    public void add(CallbackInfo info) {
        hyperiumCrashReport.add(info);
    }

}
