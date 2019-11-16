package tk.amplifiable.mcgradle;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public final class Start {
    private static final File RUN_DIRECTORY = new File("${runDirectory}");
    private static final File NATIVE_DIRECTORY = new File("${nativeDirectory}");
    private static final String CLIENT_MAIN_CLASS = "${clientMainClass}";
    private static final String SERVER_MAIN_CLASS = "${serverMainClass}";

    private Start() {
    }

    public static void main(String... args) throws Exception {
        addNatives();
        boolean client = true;
        List<String> effectiveArgs = Lists.newArrayList();
        for (String s : args) {
            if (s.equalsIgnoreCase("--server")) {
                client = false;
            } else {
                effectiveArgs.add(s);
            }
        }
        effectiveArgs.add("--version");
        effectiveArgs.add("MCGradle");
        effectiveArgs.add("--accessToken");
        effectiveArgs.add("MCGradle");
        effectiveArgs.add("--assetsDir");
        effectiveArgs.add(new File("${assetsDirectory}").getAbsolutePath());
        effectiveArgs.add("--assetIndex");
        effectiveArgs.add("${assetIndex}");
        effectiveArgs.add("--userProperties");
        effectiveArgs.add("{}");
        effectiveArgs.add("--gameDir");
        effectiveArgs.add(RUN_DIRECTORY.getAbsolutePath());
        Class<?> mainClass = Class.forName(client ? CLIENT_MAIN_CLASS : SERVER_MAIN_CLASS);
        Method mainMethod = mainClass.getDeclaredMethod("main", String[].class);
        mainMethod.setAccessible(true);
        System.gc();
        mainMethod.invoke(null, (Object) effectiveArgs.toArray(new String[0]));
    }

    private static void addNatives() {
        String nativesDir = NATIVE_DIRECTORY.getAbsolutePath();
        String paths = System.getProperty("java.library.path");
        if (Strings.isNullOrEmpty(paths)) {
            paths = nativesDir;
        } else {
            paths += File.pathSeparator + nativesDir;
        }
        System.setProperty("java.library.path", paths);
        try {
            Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
            sysPathsField.setAccessible(true);
            sysPathsField.set(null, null);
        } catch (Throwable ignored) {
        }
    }
}
