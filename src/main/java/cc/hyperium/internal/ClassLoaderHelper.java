package cc.hyperium.internal;

import cc.hyperium.Hyperium;
import cc.hyperium.internal.addons.AddonBootstrap;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;

public class ClassLoaderHelper {
    public static void injectIntoClassLoader(boolean optifine) {
        Hyperium.LOGGER.info("Loading Addons...");
        Hyperium.LOGGER.info("Initialising Bootstraps...");
        MixinBootstrap.init();
        AddonBootstrap.INSTANCE.init();
        Hyperium.LOGGER.info("Applying transformers...");
        MixinEnvironment environment = MixinEnvironment.getDefaultEnvironment();

        if (optifine || environment.getObfuscationContext() == null) {
            environment.setObfuscationContext("notch");
        }

        environment.setSide(MixinEnvironment.Side.CLIENT);
    }
}
