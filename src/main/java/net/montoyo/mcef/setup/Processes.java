package net.montoyo.mcef.setup;

import javax.swing.*;
import java.io.File;
import java.util.Iterator;

public class Processes {

    public static boolean install(JFrame parent, File dst) {
        File curJar = SetupUtil.getSelfJarLocation();
        if(curJar == null) {
            JOptionPane.showMessageDialog(parent, "Could not locate the current JAR file.\nThis shouldn't happen, contact mod author.\nCannot continue.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        File mods = new File(dst, "mods");
        if(mods.exists()) {
            File[] modList = mods.listFiles();

            for(File f: modList) {
                String fname = f.getName().toLowerCase();

                if(SetupUtil.areFileEqual(f, curJar)) {
                    SetupUI.INSTANCE.abortSelfDestruct();
                    JOptionPane.showMessageDialog(parent, "MCEF was successfully installed!\nIn fact, it was already installed here.\nAlso make sure Forge is installed!", "Well... there was nothing to do!", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else if(f.isFile() && fname.startsWith("mcef") && fname.endsWith(".jar")) {
                    while(!SetupUtil.tryDelete(f)) {
                        if(JOptionPane.showConfirmDialog(parent, "An older version of MCEF has been found and cannot be deleted.\nPlease close Minecraft or remove the following file manually:\n" + f.getAbsolutePath(), "WARNING", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION)
                            return false;
                    }
                }
            }
        } else if(!mods.mkdir()) {
            JOptionPane.showMessageDialog(parent, "Could not create mods directory. Make sure you have the rights to do that.\nCannot continue.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(SetupUtil.copyFile(curJar, new File(mods, curJar.getName()))) {
            JOptionPane.showMessageDialog(parent, "MCEF was successfully installed!\nDon't forget to install Forge!", "All done!", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(parent, "Installation failed\nCould not copy JAR to mods folder.", "Critical error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private static boolean recursiveDelete(File dir) {
        if(!dir.exists())
            return true;

        File[] files = dir.listFiles();
        boolean allOk = true;

        for(File f: files) {
            if(f.isDirectory()) {
                if(!recursiveDelete(f))
                    allOk = false;
            } else if(!SetupUtil.tryDelete(f))
                allOk = false;
        }

        return SetupUtil.tryDelete(dir) && allOk;
    }

    public static boolean uninstall(JFrame parent, File dst) {
        File configDir = new File(dst, "config");
        FileListing fl = new FileListing(configDir);
        if(!fl.load()) {
            JOptionPane.showMessageDialog(parent, "Could not locate MCEF file listing. It is either missing,\nor you selected the wrong Minecraft location.\nCannot continue.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        //Destroy resources and cache
        boolean allDeleted = true;
        Iterator<String> files = fl.iterator();

        while(files.hasNext()) {
            if(!SetupUtil.tryDelete(new File(dst, files.next())))
                allDeleted = false;
        }

        if(!recursiveDelete(new File(dst, "MCEFCache")))
            allDeleted = false;

        //Destroy file listing and configs
        if(!fl.selfDestruct())
            allDeleted = false;

        if(!SetupUtil.tryDelete(new File(configDir, "MCEF.cfg")))
            allDeleted = false;

        if(!SetupUtil.tryDelete(new File(dst, "mcef2.json")))
            allDeleted = false;

        //Destroy mod file
        File curJar = SetupUtil.getSelfJarLocation();
        File mods = new File(dst, "mods");

        if(mods.exists()) {
            File[] modList = mods.listFiles();

            for(File f: modList) {
                String fname = f.getName().toLowerCase();

                if(SetupUtil.areFileEqual(f, curJar)) {
                    //Can't self-destruct JAR; add to delete-at-exit file list
                    SetupUI.INSTANCE.initiateSelfDestruct(f);
                } else if(f.isFile() && fname.startsWith("mcef") && fname.endsWith(".jar")) {
                    if(!SetupUtil.tryDelete(f))
                        allDeleted = false;
                }
            }
        }

        //Show results
        if(allDeleted)
            JOptionPane.showMessageDialog(parent, "MCEF was successfully uninstalled!\nThanks for using it!", "All done!", JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(parent, "MCEF was uninstalled, but some files couldn't be removed; sorry about that...", "Almost everything done!", JOptionPane.WARNING_MESSAGE);

        return true;
    }

}
