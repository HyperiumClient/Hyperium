package cc.hyperium.mixins.client.resources;

import cc.hyperium.mixinsimp.client.resources.HyperiumLocale;
import net.minecraft.client.resources.Locale;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

@Mixin(Locale.class)
public abstract class MixinLocale {
    @Shadow
    protected abstract void loadLocaleData(InputStream p_135021_1_) throws IOException;

    @Redirect(
        method = "loadLocaleDataFiles",
        at = @At(
            value = "INVOKE",
            target = "Ljava/lang/String;format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;"
        )
    )
    private String injectI18nData(String format, Object... args) {
        for (Supplier<InputStream> supplier : HyperiumLocale.LANG_FILES.get(args[0].toString())) {
            try (InputStream stream = supplier.get()) {
                loadLocaleData(stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return String.format(format, args);
    }
}
