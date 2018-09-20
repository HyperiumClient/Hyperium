package com.chattriggers.ctjs.minecraft.mixins;

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
public class MixinCrashReport {
    @Final @Shadow
    //#if MC<=10809
    private CrashReportCategory theReportCategory;
    //#else
    //$$ private CrashReportCategory systemDetailsCategory;
    //#endif

    @Inject(method = "populateEnvironment", at = @At("HEAD"))
    private void injectCTModules(CallbackInfo ci) {
        //#if MC<=10809
        this.theReportCategory.addCrashSectionCallable(
                "ct.js modules",
                () -> ModuleManager.INSTANCE.getCachedModules().toString()
        );
        //#else
        //$$ this.systemDetailsCategory.addCrashSection(
        //$$      "ct.js modules",
        //$$      ModuleManager.INSTANCE.getCachedModules().toString()
        //$$ );
        //#endif
    }
}
