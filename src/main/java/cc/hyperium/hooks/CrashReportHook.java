package cc.hyperium.hooks;

import cc.hyperium.Metadata;
import cc.hyperium.commands.defaults.CommandDebug;
import com.chattriggers.ctjs.engine.ModuleManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;

public class CrashReportHook {

    public static void populateEnvironment(CrashReport report) {
        report.theReportCategory.addCrashSectionCallable(
                "ct.js modules",
                () -> ModuleManager.INSTANCE.getCachedModules().toString()
        );

        CrashReportCategory category = report.makeCategoryDepth("Affected level", 1);
        category.addCrashSection("Hyperium Version", Metadata.getVersion() + " (" + Metadata.getVersionID() + ")");
        category.addCrashSection("Everything else", CommandDebug.get());
    }
}
