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

    @Shadow protected abstract void loadLocaleData(InputStream inputStream) throws IOException;

    @Redirect(
        method = "loadLocaleDataFiles",
        at = @At(
            value = "INVOKE",
            target = "Ljava/lang/String;format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;"
        )
    )
    private String injectI18nData(String format, Object... args) {
        HyperiumLocale.LANG_FILES.get(args[0].toString()).forEach(supplier -> {
            try (InputStream stream = supplier.get()) {
                loadLocaleData(stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return String.format(format, args);
    }
}
