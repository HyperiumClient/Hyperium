package cc.hyperium.mixins;

import cc.hyperium.mixinsimp.HyperiumCrashReport;
import com.chattriggers.ctjs.engine.ModuleManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrashReport.class)
public abstract class MixinCrashReport {

    private HyperiumCrashReport hyperiumCrashReport = new HyperiumCrashReport((CrashReport) (Object) this);

    @Final
    @Shadow
    private CrashReportCategory theReportCategory;

    @Inject(method = "populateEnvironment", at = @At("HEAD"))
    public void add(CallbackInfo info) {
        this.theReportCategory.addCrashSectionCallable(
            "ct.js modules",
            () -> ModuleManager.INSTANCE.getCachedModules().toString()
        );

        hyperiumCrashReport.add(info);
    }

}
