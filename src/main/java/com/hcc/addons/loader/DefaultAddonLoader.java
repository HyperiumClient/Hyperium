package com.hcc.addons.loader;

import com.hcc.addons.AddonManifest;
import com.hcc.addons.annotations.Addon;
import com.hcc.event.EventBus;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.jar.JarFile;

public class DefaultAddonLoader extends AddonLoaderStrategy {

    /**
     * Classloader
     */
    private static LaunchClassLoader classloader = Launch.classLoader;

    /**
     * @param file addons jar
     * @throws Exception when exception occurs
     */
    @Override
    public void load(File file) throws Exception {
        JarFile jar = new JarFile(file);
        AddonManifest manifest = new AddonManifest(jar);

        URI uri = file.toURI();
        classloader.addURL(uri.toURL());

        Class<?> addonMain = Class.forName(manifest.getMain());
        Object instance = addonMain.newInstance();
        assignInstances(instance);

        for (Annotation annotation : addonMain.getAnnotations()) {
            if (annotation instanceof Addon) {
                // do whatever with Addon annotation?
                EventBus.INSTANCE.register(instance);
                break;
            }
        }
    }
}
