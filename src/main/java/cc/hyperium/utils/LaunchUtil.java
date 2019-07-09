package cc.hyperium.utils;

import cc.hyperium.Hyperium;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import net.minecraft.client.Minecraft;

/**
 * @author KodingKing
 */
public class LaunchUtil {

    public static void launch() {
        try {
            boolean windows = InstallerUtils.getOS() == InstallerUtils.OSType.Windows;
            //Class<?> c = getClass();
            //String n = c.getName().replace('.', '/');
            String cs = "";
            for (URL u : ((URLClassLoader) Hyperium.class.getClassLoader()).getURLs()) {
                if (u.getPath().contains("Hyperium")) {
                    cs = u.getPath();
                }
            }
            System.out.println("cs=" + cs);
            Runtime.getRuntime().exec(new String[]{
                windows ? "cmd" : "bash",
                windows ? "/c" : "-c",
                "java",
                "-jar",
                cs,
                Minecraft.getMinecraft().mcDataDir.getAbsolutePath()
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
