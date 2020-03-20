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

package cc.hyperium.internal;

import cc.hyperium.Hyperium;
import cc.hyperium.internal.addons.AddonBootstrap;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;

/**
 * This class is used to load the addons from the same classloader as the game itself is in and not the classloader that the tweaker uses.
 */
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
