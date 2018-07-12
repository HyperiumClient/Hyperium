package cc.hyperium.mixinsimp;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

public class HyperiumResourcePackRepository {

    public void func_183028_i(CallbackInfo callbackInfo, File dirServerResourcepacks) {
        try {
            FileUtils.listFiles(dirServerResourcepacks, TrueFileFilter.TRUE, null);
        } catch (Exception e) {
            callbackInfo.cancel();
        }
    }
}
