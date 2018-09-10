package net.montoyo.mcef.client;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderGuiEvent;
import cc.hyperium.event.RenderTickEvent;
import net.minecraft.client.Minecraft;
import net.montoyo.mcef.BaseProxy;
import net.montoyo.mcef.MCEF;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.api.IDisplayHandler;
import net.montoyo.mcef.api.IJSQueryHandler;
import net.montoyo.mcef.api.IScheme;
import net.montoyo.mcef.example.ExampleMod;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientProxy extends BaseProxy {

    public static String ROOT = ".";
    public static boolean VIRTUAL = false;
    private final ArrayList<CefBrowserOsr> browsers = new ArrayList<>();
    private final Minecraft mc = Minecraft.getMinecraft();
    private final DisplayHandler displayHandler = new DisplayHandler();
    private final HashMap<String, String> mimeTypeMap = new HashMap<>();
    private final AppHandler appHandler = new AppHandler();
    private CefApp cefApp;
    private CefClient cefClient;
    private CefMessageRouter cefRouter;
    private String updateStr;
    private ExampleMod exampleMod;

    @Override
    public void onPreInit() {
        exampleMod = new ExampleMod();
        exampleMod.onPreInit(); //Do it even if example mod is disabled because it registers the "mod://" scheme
    }

    @Override
    public void onInit() {
        if (MCEF.DISABLE_GPU_RENDERING) {
            Log.info("GPU rendering is disabled because the new launcher sucks.");
            appHandler.setArgs(new String[]{"--disable-gpu"});
        } else
            appHandler.setArgs(new String[0]);

        boolean enableForgeSplash = false;

        ROOT = mc.mcDataDir.getAbsolutePath().replaceAll("\\\\", "/");
        if (ROOT.endsWith("."))
            ROOT = ROOT.substring(0, ROOT.length() - 1);

        if (ROOT.endsWith("/"))
            ROOT = ROOT.substring(0, ROOT.length() - 1);

        File fileListing = new File(new File(ROOT), "config");

        IProgressListener ipl;
        RemoteConfig cfg = new RemoteConfig();
        ipl = new UpdateFrame();

        cfg.load();
        File[] resourceArray = cfg.getResourceArray();

        if (!cfg.updateFileListing(fileListing, false))
            Log.warning("There was a problem while establishing file list. Uninstall may not delete all files.");

        if (!cfg.downloadMissing(ipl)) {
            Log.warning("Going in virtual mode; couldn't download resources.");
            VIRTUAL = true;
            return;
        }

        if (!cfg.updateFileListing(fileListing, true))
            Log.warning("There was a problem while updating file list. Uninstall may not delete all files.");

        updateStr = cfg.getUpdateString();
        ipl.onProgressEnd();

        if (VIRTUAL)
            return;

        Log.info("Now adding \"%s\" to java.library.path", ROOT);

        try {
            Field pathsField = ClassLoader.class.getDeclaredField("usr_paths");
            pathsField.setAccessible(true);

            String[] paths = (String[]) pathsField.get(null);
            String[] newList = new String[paths.length + 1];

            System.arraycopy(paths, 0, newList, 1, paths.length);
            newList[0] = ROOT.replace('/', File.separatorChar);
            pathsField.set(null, newList);
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
        if (OS.isWindows())
            exeSuffix = ".exe";
        else
            exeSuffix = "";

        CefSettings settings = new CefSettings();
        settings.windowless_rendering_enabled = true;
        settings.background_color = settings.new ColorType(0, 255, 255, 255);
        settings.locales_dir_path = (new File(ROOT, "MCEFLocales")).getAbsolutePath();
        settings.cache_path = (new File(ROOT, "MCEFCache")).getAbsolutePath();
        settings.browser_subprocess_path = (new File(ROOT, "jcef_helper" + exeSuffix)).getAbsolutePath(); //Temporary fix
        //settings.log_severity = CefSettings.LogSeverity.LOGSEVERITY_VERBOSE;

        try {
            ArrayList<String> libs = new ArrayList<>();

            if (OS.isWindows()) {
                libs.add(System.getProperty("sun.arch.data.model").equals("64") ? "d3dcompiler_47.dll" : "d3dcompiler_43.dll");
                libs.add("libGLESv2.dll");
                libs.add("libEGL.dll");
                libs.add("libcef.dll");
                libs.add("jcef.dll");
            } else {
                libs.add("libcef.so");
                libs.add("libjcef.so");
            }

            for (String lib : libs) {
                File f = new File(ROOT, lib);
                try {
                    f = f.getCanonicalFile();
                } catch (IOException ex) {
                    f = f.getAbsoluteFile();
                }

                System.load(f.getPath());
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
        if (MCEF.ENABLE_EXAMPLE)
            exampleMod.onInit();

        Log.info("MCEF loaded successfuly.");
    }

    public CefApp getCefApp() {
        return cefApp;
    }

    @Override
    public IBrowser createBrowser(String url, boolean transp) {
        if (VIRTUAL)
            return new VirtualBrowser();

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
        if (MCEF.ENABLE_EXAMPLE)
            exampleMod.showScreen(url);
    }

    @Override
    public void registerJSQueryHandler(IJSQueryHandler iqh) {
        if (!VIRTUAL)
            cefRouter.addHandler(new MessageRouter(iqh), false);
    }

    @Override
    public void registerScheme(String name, Class<? extends IScheme> schemeClass, boolean std, boolean local, boolean displayIsolated) {
        appHandler.registerScheme(name, schemeClass, std, local, displayIsolated);
    }

    @Override
    public boolean isSchemeRegistered(String name) {
        return appHandler.isSchemeRegistered(name);
    }

    @InvokeEvent
    public void onTick(RenderTickEvent ev) {
        mc.mcProfiler.startSection("MCEF");

        for (CefBrowserOsr b : browsers)
            b.mcefUpdate();

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
        if (VIRTUAL)
            return;

        Log.info("Shutting down JCEF...");
        CefBrowserOsr.CLEANUP = false; //Workaround

        for (CefBrowserOsr b : browsers)
            b.close();

        browsers.clear();
        cefClient.dispose();

        if (MCEF.CHECK_VRAM_LEAK)
            CefRenderer.dumpVRAMLeak();

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
            BufferedReader br = new BufferedReader(new InputStreamReader(ClientProxy.class.getResourceAsStream("/assets/mcef/mime.types")));

            while (true) {
                cLine++;
                line = br.readLine();
                if (line == null)
                    break;

                line = line.trim();
                if (!line.startsWith("#")) {
                    Matcher m = p.matcher(line);
                    if (!m.matches())
                        continue;

                    mimeTypeMap.put(m.group(2), m.group(1));
                    if (m.groupCount() >= 4 && !m.group(3).isEmpty()) {
                        mimeTypeMap.put(m.group(3), m.group(1));

                        if (m.groupCount() >= 5 && !m.group(4).isEmpty())
                            mimeTypeMap.put(m.group(4), m.group(1));
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
        if (ret != null)
            return ret;

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
