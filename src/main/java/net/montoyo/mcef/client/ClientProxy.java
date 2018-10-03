package net.montoyo.mcef.client;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderTickEvent;
import cc.hyperium.mods.browser.HyperiumProgressListener;
import net.minecraft.client.Minecraft;
import net.montoyo.mcef.BaseProxy;
import net.montoyo.mcef.MCEF;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.api.IDisplayHandler;
import net.montoyo.mcef.api.IJSQueryHandler;
import net.montoyo.mcef.api.IScheme;
import net.montoyo.mcef.remote.RemoteConfig;
import net.montoyo.mcef.utilities.IProgressListener;
import net.montoyo.mcef.utilities.Log;
import net.montoyo.mcef.utilities.Util;
import net.montoyo.mcef.virtual.VirtualBrowser;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.OS;
import org.cef.browser.CefBrowserOsr;
import org.cef.browser.CefMessageRouter;
import org.cef.browser.CefMessageRouter.CefMessageRouterConfig;
import org.cef.browser.CefRenderer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientProxy extends BaseProxy {

    public static String ROOT;
    public static boolean VIRTUAL = false;
    private final ArrayList<CefBrowserOsr> browsers = new ArrayList<>();
    private final Minecraft mc = Minecraft.getMinecraft();
    private final DisplayHandler displayHandler = new DisplayHandler();
    private final HashMap<String, String> mimeTypeMap = new HashMap<>();
    public CefClient cefClient;
    private AppHandler appHandler;
    private CefApp cefApp;
    private CefMessageRouter cefRouter;
    private String updateStr;

    @Override
    public void onPreInit() {
    }

    @Override
    public void onInit() {
        if (MCEF.DISABLE_GPU_RENDERING) {
            Log.info("GPU rendering is disabled because the new launcher sucks.");
            appHandler = new AppHandler(new String[]{"--disable-gpu"});
        } else {
            appHandler = new AppHandler(new String[0]);
        }

//        ROOT = mc.mcDataDir.getAbsolutePath() + File.separator + "MCEF";
        ROOT = Hyperium.folder.getAbsolutePath() + File.separator + "libs";

        File rootDir = new File(ROOT);
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }

        File fileListing = new File(new File(ROOT), "config");
        if (!fileListing.exists()) {
            fileListing.mkdirs();
        }

        IProgressListener ipl = new HyperiumProgressListener();
        RemoteConfig cfg = new RemoteConfig();

        cfg.load();
        File[] resourceArray = cfg.getResourceArray();



        if (!cfg.downloadMissing(ipl)) {
            Log.warning("Going in virtual mode; couldn't download resources.");
            VIRTUAL = true;
            return;
        }


        updateStr = cfg.getUpdateString();
        ipl.onProgressEnd();

        if (VIRTUAL) {
            return;
        }

        Log.info("Now adding \"%s\" to java.library.path", ROOT);

        try {
            String paths = System.getProperty("java.library.path");

            String jreHome = System.getProperty("java.home");
            if (jreHome.contains("jdk")) {
                List<String> split = new ArrayList<>(Arrays.asList(jreHome.split(
                        "[" + (File.separator.equalsIgnoreCase("\\") ? "\\\\" : File.separator)
                                + "]")));
                split.remove(split.size() - 1);
                jreHome = String.join(File.separator, split.toArray(new String[0])) + File.separator
                        + "jre" + File.separator + "bin";
            }
            paths += ";" + jreHome;

            String newRoot = ROOT.replace('/', File.separatorChar);
            if (OS.isMacintosh()) {
                newRoot += File.separatorChar + RemoteConfig.PLATFORM + ("-natives");
            }
            if (newRoot.endsWith("."))
                newRoot = newRoot.substring(0, newRoot.length() - 1);
            paths += ";" + newRoot;
            System.out.println("CEF resources root: " + newRoot);
            System.setProperty("java.library.path", paths);

            Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
            sysPathsField.setAccessible(true);
            sysPathsField.set(null, null);

            System.out.println(paths);
        } catch (Exception e) {
            Log.error("Failed to do it! Entering virtual mode...");
            e.printStackTrace();

            VIRTUAL = true;
            return;
        }

        Log.info("Done without errors.");

        if (OS.isLinux()) {
            Log.info("Applying linux patch...");
            LinuxPatch.doPatch(resourceArray);
        }

        String exeSuffix;
        if (OS.isWindows()) {
            exeSuffix = ".exe";
        } else {
            exeSuffix = "";
        }

        CefSettings settings = new CefSettings();
        settings.windowless_rendering_enabled = true;
        settings.background_color = settings.new ColorType(0, 255, 255, 255);
        settings.locales_dir_path = (new File(ROOT, "MCEFLocales")).getAbsolutePath();
        settings.cache_path = (new File(ROOT, "MCEFCache")).getAbsolutePath();
        settings.browser_subprocess_path = (new File(ROOT, "jcef_helper" + exeSuffix))
                .getAbsolutePath(); //Temporary fix
        //settings.log_severity = CefSettings.LogSeverity.LOGSEVERITY_VERBOSE;

        try {
            ArrayList<String> libs = new ArrayList<>();

            if (OS.isWindows()) {
                libs.add("jawt");
                libs.add("chrome_elf");
                libs.add("libcef");
            } else {
                libs.add("cef");
            }
            libs.add("jcef");

            for (String lib : libs) {
                String loadName = lib;
                if (loadName.contains(".")) {
                    loadName = loadName.substring(0, loadName.length() - loadName.split("[.]")[1].length() - 1);
                }
                try {
                    System.loadLibrary(loadName);
                    System.out.println("Loaded " + loadName);
                } catch (UnsatisfiedLinkError error) {
                    error.printStackTrace();
                }
            }

            cefApp = CefApp.getInstance(settings);
            //cefApp.myLoc = ROOT.replace('/', File.separatorChar);

            loadMimeTypeMapping();
            CefApp.addAppHandler(appHandler);
            cefClient = cefApp.createClient();
        } catch (Throwable t) {
            Log.error("Going in virtual mode; couldn't initialize CEF.");
            t.printStackTrace();

            VIRTUAL = true;
            return;
        }

        Log.info(cefApp.getVersion().toString());
        cefRouter = CefMessageRouter.create(new CefMessageRouterConfig("mcefQuery", "mcefCancel"));
        cefClient.addMessageRouter(cefRouter);
        cefClient.addDisplayHandler(displayHandler);

        EventBus.INSTANCE.register(this);

        Log.info("MCEF loaded successfuly.");
    }

    public CefApp getCefApp() {
        return cefApp;
    }

    @Override
    public IBrowser createBrowser(String url, boolean transp) {
        if (VIRTUAL) {
            return new VirtualBrowser();
        }

        CefBrowserOsr ret = (CefBrowserOsr) cefClient.createBrowser(url, true, transp);
        browsers.add(ret);
        return ret;
    }

    @Override
    public void registerDisplayHandler(IDisplayHandler idh) {
        displayHandler.addHandler(idh);
    }

    @Override
    public boolean isVirtual() {
        return VIRTUAL;
    }

    @Override
    public void openExampleBrowser(String url) {
    }

    @Override
    public void registerJSQueryHandler(IJSQueryHandler iqh) {
        if (!VIRTUAL) {
            cefRouter.addHandler(new MessageRouter(iqh), false);
        }
    }

    @Override
    public void registerScheme(String name, Class<? extends IScheme> schemeClass, boolean std,
                               boolean local, boolean displayIsolated) {
        appHandler.registerScheme(name, schemeClass, std, local, displayIsolated);
    }

    @Override
    public boolean isSchemeRegistered(String name) {
        return appHandler.isSchemeRegistered(name);
    }

    @InvokeEvent
    public void onTick(RenderTickEvent ev) {
        mc.mcProfiler.startSection("MCEF");

        for (CefBrowserOsr b : browsers) {
            b.mcefUpdate();
        }

        displayHandler.update();
        mc.mcProfiler.endSection();

    }


    public void removeBrowser(CefBrowserOsr b) {
        browsers.remove(b);
    }

    @Override
    public IBrowser createBrowser(String url) {
        return createBrowser(url, false);
    }

    @Override
    public void onShutdown() {
        if (VIRTUAL) {
            return;
        }

        Log.info("Shutting down JCEF...");
        CefBrowserOsr.CLEANUP = false; //Workaround

        for (CefBrowserOsr b : browsers) {
            b.close();
        }

        browsers.clear();
        cefClient.dispose();

        if (MCEF.CHECK_VRAM_LEAK) {
            CefRenderer.dumpVRAMLeak();
        }

        try {
            //Yea sometimes, this is needed for some reasons.
            Thread.sleep(100);
        } catch (Throwable ignored) {
        }

        cefApp.N_Shutdown();
    }

    public void loadMimeTypeMapping() {
        Pattern p = Pattern.compile("^(\\S+)\\s+(\\S+)\\s*(\\S*)\\s*(\\S*)$");
        String line = "";
        int cLine = 0;
        mimeTypeMap.clear();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    ClientProxy.class.getResourceAsStream("/assets/mcef/mime.types")));

            while (true) {
                cLine++;
                line = br.readLine();
                if (line == null) {
                    break;
                }

                line = line.trim();
                if (!line.startsWith("#")) {
                    Matcher m = p.matcher(line);
                    if (!m.matches()) {
                        continue;
                    }

                    mimeTypeMap.put(m.group(2), m.group(1));
                    if (m.groupCount() >= 4 && !m.group(3).isEmpty()) {
                        mimeTypeMap.put(m.group(3), m.group(1));

                        if (m.groupCount() >= 5 && !m.group(4).isEmpty()) {
                            mimeTypeMap.put(m.group(4), m.group(1));
                        }
                    }
                }
            }

            Util.close(br);
        } catch (Throwable e) {
            Log.error("[Mime Types] Error while parsing \"%s\" at line %d:", line, cLine);
            e.printStackTrace();
        }

        Log.info("Loaded %d mime types", mimeTypeMap.size());
    }

    @Override
    public String mimeTypeFromExtension(String ext) {
        ext = ext.toLowerCase();
        String ret = mimeTypeMap.get(ext);
        if (ret != null) {
            return ret;
        }

        //If the mimeTypeMap couldn't be loaded, fall back to common things
        switch (ext) {
            case "htm":
            case "html":
                return "text/html";

            case "css":
                return "text/css";

            case "js":
                return "text/javascript";

            case "png":
                return "image/png";

            case "jpg":
            case "jpeg":
                return "image/jpeg";

            case "gif":
                return "image/gif";

            case "svg":
                return "image/svg+xml";

            case "xml":
                return "text/xml";

            case "txt":
                return "text/plain";

            default:
                return null;
        }
    }
}
