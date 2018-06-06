package cc.hyperium.mixins;

import cc.hyperium.Metadata;
import cc.hyperium.commands.defaults.CommandDebug;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrashReport.class)
public abstract class MixinCrashReport {


    @Inject(method = "populateEnvironment", at = @At("HEAD"))
    public void add(CallbackInfo info) {
        CrashReportCategory category = makeCategoryDepth("Affected level", 1);
        category.addCrashSection("Hyperium Version", Metadata.getVersion() + " (" + Metadata.getVersionID() + ")");
        category.addCrashSection("Everything else", CommandDebug.get());


    }

    @Shadow
    public abstract CrashReportCategory makeCategory(String name);

    @Shadow
    public abstract CrashReportCategory makeCategoryDepth(String categoryName, int stacktraceLength);
}
