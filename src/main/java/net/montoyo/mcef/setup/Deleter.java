package net.montoyo.mcef.setup;

import java.io.File;

/**
 * This class will be extracted in another temporary JAR in order to self-destroy the MCEF jar after exit.
 * It has to be standalone and must remain as light as possible for the user's convenience.
 */
public class Deleter {

    //Sorry about the copy/paste, but this class needs to be standalone!
    private static boolean tryDelete(File f) {
        if(!f.exists())
            return true;

        if(f.delete())
            return true;
        else {
            File dst = new File(f.getParentFile(), f.getName() + "_" + System.currentTimeMillis() % ((int) (Math.random() * 10000)) + ".tmp");

            if(f.renameTo(dst)) {
                if(!dst.delete())
                    dst.deleteOnExit();

                return true;
            } else
                return false;
        }
    }

    public static void main(String[] args) {
        File f = new File(args[0]);
        String lowerName = f.getName().toLowerCase();

        if(lowerName.startsWith("mcef") && lowerName.endsWith(".jar")) {
            for(int i = 0; i < 30; i++) {
                if(tryDelete(f))
                    return;

                try {
                    Thread.sleep(3000);
                } catch(Throwable t) {}
            }
        }
    }

}
