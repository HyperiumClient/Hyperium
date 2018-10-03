package net.montoyo.mcef.setup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SetupUtil {

    static boolean tryDelete(File f) {
        if(!f.exists())
            return true;

        if(f.delete())
            return true;
        else {
            File dst = new File(f.getParentFile(), f.getName() + "_" + System.currentTimeMillis() + "_" + ((int) (Math.random() * 10000)) + ".tmp");

            if(f.renameTo(dst)) {
                if(!dst.delete())
                    dst.deleteOnExit();

                return true;
            } else
                return false;
        }
    }

    static File getSelfJarLocation() {
        try {
            File ret = new File(SetupUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            if(ret.exists() && ret.isFile())
                return ret;
        } catch(Throwable t) {
            System.err.println("Could not locate own JAR (try #1):");
            t.printStackTrace();
        }

        try {
            File ret = new File(ClassLoader.getSystemClassLoader().getResource(".").getPath());
            if(ret.exists() && ret.isFile())
                return ret;
        } catch(Throwable t) {
            System.err.println("Could not locate own JAR (try #2):");
            t.printStackTrace();
        }

        return null;
    }

    static void silentClose(Object o) {
        try {
            o.getClass().getMethod("close").invoke(o);
        } catch(Throwable t) {}
    }

    static boolean copyFile(File src, File dst) {
        byte[] buf = new byte[65536];
        int read;

        try {
            FileInputStream fis = new FileInputStream(src);
            FileOutputStream fos = new FileOutputStream(dst);

            while((read = fis.read(buf)) > 0)
                fos.write(buf, 0, read);

            silentClose(fos);
            silentClose(fis);
            return true;
        } catch(Throwable t) {
            System.err.println("Could NOT copy \"" + src.getAbsolutePath() + "\" to \"" + dst.getAbsolutePath() + "\":");
            t.printStackTrace();
            return false;
        }
    }

    static boolean areFileEqual(File a, File b) {
        if(a == null || b == null)
            return false;

        try {
            String ap = a.getCanonicalPath();
            String bp = b.getCanonicalPath();

            return System.getProperty("os.name").toLowerCase().contains("win") ? ap.equalsIgnoreCase(bp) : ap.equals(bp); //Windows paths are case-insensitive
        } catch(IOException e) {
            System.err.println("Could not compare file path, returning non-equal:");
            e.printStackTrace();
            return false;
        }
    }

}
