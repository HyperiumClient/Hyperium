/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mixins.crash;

import cc.hyperium.mixinsimp.crash.HyperiumCrashReport;
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

    @Final @Shadow private CrashReportCategory theReportCategory;

    private HyperiumCrashReport hyperiumCrashReport = new HyperiumCrashReport((CrashReport) (Object) this);

    @Inject(method = "populateEnvironment", at = @At("HEAD"))
    private void add(CallbackInfo info) {
        theReportCategory.addCrashSectionCallable(
            "ct.js modules",
            () -> ModuleManager.INSTANCE.getCachedModules().toString()
        );

        hyperiumCrashReport.add();
    }

}
