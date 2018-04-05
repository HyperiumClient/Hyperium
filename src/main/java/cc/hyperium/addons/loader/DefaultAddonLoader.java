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

package cc.hyperium.addons.loader;

import cc.hyperium.addons.AddonManifest;
import cc.hyperium.addons.annotations.Addon;
import cc.hyperium.event.EventBus;
import cc.hyperium.exceptions.HyperiumException;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.lang.annotation.Annotation;
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

        classloader.addURL(file.toURI().toURL());

        Class<?> addonMain = classloader.findClass(manifest.getMain());
        Object instance = addonMain.newInstance();
        assignInstances(instance);

        Addon addon = addonMain.getAnnotation(Addon.class);
        // do whatever with addon annotation
        if(addon == null)
            throw new HyperiumException("Failed to load addon: "+manifest.getName()+". No @Addon annotation present");
        EventBus.INSTANCE.register(instance);
    }
}
