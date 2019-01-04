/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.launch;

import cc.hyperium.Hyperium;
import cc.hyperium.internal.addons.AddonBootstrap;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Contains utilities used to subscribe and invoke events
 *
 * @author Kevin
 * @since 10/02/2018 4:11 PM
 */
public class HyperiumTweaker implements ITweaker {

    public static HyperiumTweaker INSTANCE;

    private ArrayList<String> args = new ArrayList<>();

    private boolean isRunningForge = Launch.classLoader.getTransformers().stream()
        .anyMatch(p -> p.getClass().getName().contains("fml"));

    private boolean isRunningOptifine = Launch.classLoader.getTransformers().stream()
        .anyMatch(p -> p.getClass().getName().contains("optifine"));

    private boolean FORGE = false;
    private boolean OPTIFINE = false;

    public HyperiumTweaker() {
        INSTANCE = this;
    }

    @Override
    public void acceptOptions(List<String> args, File gameDir, final File assetsDir,
                              String profile) {
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
        //classLoader.addClassLoaderExclusion("org.apache.logging.log4j.simple.")

        Hyperium.LOGGER.info("[Addons] Loading Addons...");

        Hyperium.LOGGER.info("Initialising Bootstraps...");
        MixinBootstrap.init();
        AddonBootstrap.INSTANCE.init();

        Hyperium.LOGGER.info("Applying transformers...");
        //classLoader.registerTransformer("cc.hyperium.mods.memoryfix.ClassTransformer")

        // Excludes packages from classloader
        MixinEnvironment environment = MixinEnvironment.getDefaultEnvironment();
        Mixins.addConfiguration("mixins.hyperium.json");

        if (this.isRunningForge) {
            this.FORGE = true;
            environment.setObfuscationContext("searge"); // Switch's to forge searge mappings
        }

        if (this.isRunningOptifine) {
            this.OPTIFINE = true;

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

        Hyperium.LOGGER.info("Forge {}!", FORGE ? "found" : "not found");

        environment.setSide(MixinEnvironment.Side.CLIENT);
    }

    @Override
    public String[] getLaunchArguments() {
        if (FORGE || OPTIFINE) {
            return new String[0];
        } else {
            return args.toArray(new String[]{});
        }
    }

    public boolean isUsingForge() {
        return FORGE;
    }

    public boolean isUsingOptifine() {
        return OPTIFINE;
    }

    private void addArg(String label, String value) {
        if (!this.args.contains(("--" + label)) && value != null) {
            this.args.add(("--" + label));
            this.args.add(value);
        }
    }

    private void addArg(String args, File file) {
        if (file == null) {
            return;
        }

        addArg(args, file.getAbsolutePath());
    }

    private void addArgs(Map<String, ?> args) {
        args.forEach((label, value) -> {
            if (value instanceof String) {
                addArg(label, (String) value);
            } else if (value instanceof File) {
                addArg(label, (File) value);
            }
        });
    }
}
