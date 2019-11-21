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

package cc.hyperium.launch;

import cc.hyperium.Hyperium;
import cc.hyperium.internal.addons.AddonBootstrap;
import cc.hyperium.launch.patching.PatchManager;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Contains utilities used to subscribe and invoke events
 *
 * @author Kevin
 * @since 10/02/2018 4:11 PM
 */
public class HyperiumTweaker implements ITweaker {

    private ArrayList<String> args = new ArrayList<>();

    private boolean isRunningOptifine = Launch.classLoader.getTransformers().stream()
            .anyMatch(p -> p.getClass().getName().toLowerCase(Locale.ENGLISH).contains("optifine"));

    @Override
    public void acceptOptions(List<String> args, File gameDir, final File assetsDir, String profile) {
        this.args.addAll(args);

        addArg("gameDir", gameDir);
        addArg("assetsDir", assetsDir);
        addArg("version", profile);
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        Hyperium.LOGGER.info("Initialising patcher...");
        try {
            PatchManager.INSTANCE.setup(classLoader, false);
        } catch (IOException e) {
            throw new RuntimeException(e); // we want to crash because users will bug us if we just return so just throw it
        }
        classLoader.registerTransformer("cc.hyperium.launch.patching.PatchTransformer");
        Hyperium.LOGGER.info("[Addons] Loading Addons...");

        Hyperium.LOGGER.info("Initialising Bootstraps...");
        MixinBootstrap.init();
        AddonBootstrap.INSTANCE.init();

        Hyperium.LOGGER.info("Applying transformers...");

        MixinEnvironment environment = MixinEnvironment.getDefaultEnvironment();

        if (isRunningOptifine) {
            environment.setObfuscationContext("notch"); // Switch's to notch mappings
        }

        if (environment.getObfuscationContext() == null) {
            environment.setObfuscationContext("notch"); // Switch's to notch mappings
        }

        try {
            classLoader.addURL(new File(System.getProperty("java.home"), "lib/ext/nashorn.jar").toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        environment.setSide(MixinEnvironment.Side.CLIENT);
    }

    @Override
    public String[] getLaunchArguments() {
        return isRunningOptifine ? new String[0] : args.toArray(new String[]{});
    }

    private void addArg(String label, Object value) {
        args.add("--" + label);
        args.add(value instanceof String ? (String) value : value instanceof File ? ((File) value).getAbsolutePath() : ".");
    }
}
