package tk.amplifiable.mcgradle;

import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.internal.project.DefaultProject;
import org.gradle.invocation.DefaultGradle;
import org.slf4j.Logger;

import java.io.File;

public class MCGradleConstants {
    public static File CACHE_DIRECTORY;
    public static File ROOT_PROJECT_DIR;
    public static DefaultProject ROOT_PROJECT;
    public static DefaultGradle GRADLE;
    public static Logger LOGGER;
    public static Extension EXTENSION = new Extension();

    public static String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:69.0) Gecko/20100101 Firefox/69.0";
    public static String VERSION_MANIFEST_URL = "https://launchermeta.mojang.com/mc/game/version_manifest.json";

    public static void setProjectFields(Project p) {
        ROOT_PROJECT_DIR = prepareDirectory(p.getRootDir());
        ROOT_PROJECT = (DefaultProject) p;
        GRADLE = (DefaultGradle) ROOT_PROJECT.getGradle();
        LOGGER = ROOT_PROJECT.getLogger();
        CACHE_DIRECTORY = prepareDirectory(new File(p.getGradle().getGradleUserHomeDir(), "mcgradle"));
    }

    public static File prepareDirectory(File dir) {
        if (!dir.isDirectory()) {
            if (dir.exists()) {
                throw new GradleException(dir.getAbsolutePath() + " already exists, but isn't a directory.");
            }
            if (!dir.mkdirs()) {
                throw new GradleException("Failed to create directory " + dir.getAbsolutePath());
            }
        }
        return dir;
    }
}
