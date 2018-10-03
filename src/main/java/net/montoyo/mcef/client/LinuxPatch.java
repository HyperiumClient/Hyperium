package net.montoyo.mcef.client;

import net.montoyo.mcef.utilities.Log;
import net.montoyo.mcef.utilities.Util;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LinuxPatch {

    private static String[] getBinDirs() {
        return new String[] { "/bin", "/usr/bin", "/usr/sbin", "/usr/local/bin", "/usr/local/sbin" };
    }

    private static String[] getPatchFiles() {
        return new String[] { "icudtl.dat", "natives_blob.bin", "snapshot_blob.bin", "v8_context_snapshot.bin" };
    }

    private static String getExeLocation(String exe) {
        String[] bins = getBinDirs();
        for(String b: bins) {
            File f = new File(b, exe);

            if(f.exists())
                return f.getAbsolutePath();
        }

        return null;
    }

    private static String getGksudoLocation() {
        String ret = getExeLocation("gksudo");
        return (ret == null) ? getExeLocation("gksu") : ret;
    }

    public static File getScriptFile() {
        return new File(ClientProxy.ROOT, "mcefLinuxPatch.sh");
    }

    public static void generateScript() throws Throwable {
        String[] files = getPatchFiles();
        BufferedWriter bw = new BufferedWriter(new FileWriter(getScriptFile()));

        bw.write("#!/bin/sh\n");
        bw.write("MCEF_ROOT=\"" + ClientProxy.ROOT + "\"\n");
        bw.write("JAVA_ROOT=\"" + System.getProperty("java.home") + "/bin\"\n\n");

        for(String f: files)
            bw.write("rm -f \"$JAVA_ROOT/" + f + "\"\n");

        bw.write("\n\n");

        for(String f: files)
            bw.write("ln -s \"$MCEF_ROOT/" + f + "\" \"$JAVA_ROOT/" + f + "\"\n");

        bw.write("\n\n");
        Util.close(bw);
    }

    public static boolean chmodX(File p) {
        try {
            return Runtime.getRuntime().exec(new String[] { "chmod", "+x", p.getAbsolutePath() }).waitFor() == 0;
        } catch(IOException | InterruptedException ex) {
            return false;
        }
    }

    public static boolean runScript() {
        String cmd = getGksudoLocation();
        if(cmd == null)
            return false;

        try {
            if(!chmodX(getScriptFile())) {
                Log.error("chmod failed!");
                return false;
            }

            if(Runtime.getRuntime().exec(new String[] { cmd, getScriptFile().getAbsolutePath() }).waitFor() != 0) {
                Log.error("gksudo failed!");
                return false;
            }

            for(int i = 0; i < 6; i++) {
                if(isPatched())
                    break;

                try {
                    Thread.sleep(1000);
                } catch(Throwable t) {
                }
            }

            return true;
        } catch(IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean isPatched() {
        File root = new File(System.getProperty("java.home"), "bin");
        String[] files = getPatchFiles();

        for(String f: files) {
            if(!(new File(root, f)).exists())
                return false;
        }

        return true;
    }

    public static boolean doPatch(File[] resourceArray) {
        for(File f: resourceArray) {
            if(f.exists() && !chmodX(f))
                Log.warning("Couldn't give execution access to %s", f.getAbsolutePath());
        }

        if(isPatched())
            return true;

        try {
            generateScript();
        } catch(Throwable t) {
            Log.error("Could not apply linux patch:");
            t.printStackTrace();
            return false;
        }

        int ans = JOptionPane.showConfirmDialog(null, "An existing bug in JCEF requires some files to be copied\ninto the Java home directory in order to make MCEF working.\nThis operations requires root privileges.\nDo you want MCEF to try to do it automatically?",
                "MCEF Linux", JOptionPane.YES_NO_OPTION);

        if(ans != JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null, "MCEF will enter virtual mode.\nA script containing the patch was generated here:\n" + getScriptFile().getAbsolutePath(), "MCEF Linux", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        return runScript();
    }

}
