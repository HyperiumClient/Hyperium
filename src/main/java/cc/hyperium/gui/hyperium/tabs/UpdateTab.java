package cc.hyperium.gui.hyperium.tabs;

import cc.hyperium.Hyperium;
import cc.hyperium.Metadata;
import cc.hyperium.gui.hyperium.HyperiumMainGui;
import cc.hyperium.gui.hyperium.components.AbstractTab;
import cc.hyperium.installer.api.entities.VersionManifest;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.utils.DownloadTask;
import cc.hyperium.utils.InstallerUtils;
import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Mouse;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

/*
 * Created by Cubxity on 25/09/2018
 */
public class UpdateTab extends AbstractTab {
    private VersionManifest latest;
    private boolean buttonState;
    private String installState = "";
    private DownloadTask dl;

    public UpdateTab(HyperiumMainGui gui) {
        super(gui, "tab.update.name");
        Multithreading.runAsync(() -> latest = InstallerUtils.getManifest().getLatest());
    }

    @Override
    public void render(int x, int y, int width, int height) {
        boolean ua = latest != null && latest.getId() > Metadata.getVersionID();
        String text = latest == null ? "Loading..." : ua ? "Hyperium " + (latest.getName() + (latest.getBeta() ? " (" + latest.getId() + ")" : "")) : I18n.format("tab.update.noupdates");
        gui.getTitle().drawString(text, x + 10, y + 10, 0xffffff);
        if (ua) {
            gui.getFont().drawString(dl != null && dl.getProgress() == 100 ? I18n.format("tab.update.autorestart") : I18n.format("tab.update.changelog"), x + 10, y + 25, 0x969696);
            Gui.drawRect(x + 10, y + height - 60, x + 160, y + height - 10, 0x70000000);
            ScaledResolution sr = ResolutionUtil.current();
            int sw = sr.getScaledWidth();
            int sh = sr.getScaledHeight();
            buttonState = false;
            final int mx = Mouse.getX() * sw / Minecraft.getMinecraft().displayWidth;           // Mouse X
            final int my = sh - Mouse.getY() * sh / Minecraft.getMinecraft().displayHeight - 1; // Mouse Y
            if (mx >= x + 10 && mx <= x + 160 && my >= y + height - 60 && my <= y + height - 10 && Mouse.isButtonDown(0)) {
                if (!buttonState) {
                    buttonState = true;
                    if (dl == null)
                        try {
                            File tmp = Files.createTempDirectory("hyperiumUpdate").toFile();
                            tmp.deleteOnExit();
                            dl = new DownloadTask(latest.getUrl(), tmp.getAbsolutePath());
                            Multithreading.runAsync(() -> {
                                dl.execute();
                                try {
                                    dl.get();
                                    installState = I18n.format("tab.update.installer.state.loading");
                                    URLClassLoader ucl = new URLClassLoader(new URL[]{new File(tmp, dl.getFileName()).toURI().toURL()});
                                    Thread.currentThread().setContextClassLoader(ucl);
                                    try {
                                        Class<?> ic = ucl.loadClass("cc.hyperium.installer.api.Installer");
                                        Class<?> icc = ucl.loadClass("cc.hyperium.installer.api.entities.InstallerConfig");
                                        Class<?> scc = ucl.loadClass("cc.hyperium.installer.api.callbacks.StatusCallback");
                                        Class<?> ecc = ucl.loadClass("cc.hyperium.installer.api.callbacks.ErrorCallback");
                                        Class<?> vmc = ucl.loadClass("cc.hyperium.installer.api.entities.VersionManifest");
                                        Method scgmm = scc.getDeclaredMethod("getMessage");
                                        Method ecgmm = ecc.getDeclaredMethod("getMessage");
                                        Method ecgem = ecc.getDeclaredMethod("getError");
                                        Method iccsv = icc.getDeclaredMethod("setVersion", vmc);
                                        int api = ic.getField("API_VERSION").getInt(null);

                                        System.out.println(Arrays.toString(vmc.getConstructors()));
                                        //java.lang.String,int,java.lang.String,java.lang.String,java.lang.String,long,long,boolean,int
                                        Object local = vmc.getDeclaredConstructor(String.class, int.class, String.class, String.class, String.class, long.class, long.class, boolean.class, int.class)
                                                .newInstance("LOCAL", latest.getId(), latest.getUrl(), latest.getSha256(), latest.getSha1(), latest.getSize(), latest.getTime(), latest.getBeta(), latest.getTargetInstaller());
                                        Object config;
                                        File prev = new File(System.getProperty("user.home"), "hinstaller-state.json");
                                        if (prev.exists())
                                            try {
                                                config = new Gson().fromJson(new String(Files.readAllBytes(prev.toPath()), Charset.defaultCharset()), icc);
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                                config = icc.newInstance();
                                            }
                                        else
                                            config = icc.newInstance();
                                        iccsv.invoke(config, local);
                                        Object installer = ic.getDeclaredConstructor(icc, Consumer.class).newInstance(config, (Consumer) c -> {
                                            Thread.currentThread().setContextClassLoader(ucl);
                                            if (c.getClass().isAssignableFrom(scc)) {
                                                try {
                                                    String s = (String) scgmm.invoke(c);
                                                    if (s.contains("success")) {
                                                        installState = I18n.format("tab.update.installer.state.restarting");
                                                        Multithreading.runAsync(() -> {
                                                            String cmd = Hyperium.INSTANCE.getLaunchCommand(true);
                                                            System.out.println("Restart cmd: " + cmd);
                                                            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                                                                try {
                                                                    Runtime.getRuntime().exec(cmd);
                                                                    System.out.println("Restarting...");
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }));
                                                            Minecraft.getMinecraft().shutdown();
                                                        });
                                                    } else
                                                        installState = s;
                                                } catch (IllegalAccessException | InvocationTargetException e) {
                                                    e.printStackTrace();
                                                    installState = I18n.format("tab.update.installer.state.unknown");
                                                }
                                            } else if (c.getClass().isAssignableFrom(ecc)) {
                                                try {
                                                    ((Exception) ecgem.invoke(c)).printStackTrace();
                                                    System.err.println("E: " + ecgmm.invoke(c));
                                                } catch (IllegalAccessException | InvocationTargetException e) {
                                                    e.printStackTrace();
                                                }
                                                installState = I18n.format("tab.update.installer.state.manualupdate.api", api);
                                            }
                                        });
                                        installState = I18n.format("tab.update.installer.state.starting");
                                        ic.getDeclaredMethod("install").invoke(installer);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        installState = I18n.format("tab.update.installer.state.manualupdate");
                                    }
                                } catch (InterruptedException | ExecutionException | MalformedURLException e) {
                                    e.printStackTrace();
                                    dl = null;
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            dl = null;
                        }
                }
            } else if (buttonState)
                buttonState = false;
            if (dl == null)
                gui.getTitle().drawCenteredString(I18n.format("tab.update.update"), x + 85, y + height - 45, 0xffffff);
            else {
                gui.getTitle().drawCenteredString(dl.getProgress() == 100 ? I18n.format("tab.update.updating") : I18n.format("tab.update.downloading"), x + 85, y + height - 45, 0x969696);
                gui.getFont().drawCenteredString(dl.getProgress() == 100 ? installState : dl.getProgress() + "%", x + 85, y + height - 30, 0x969696);
                if (dl.getProgress() != 100) {
                    Gui.drawRect(0, gui.height - 2, gui.width / 100 * dl.getProgress(), gui.height, 0xff95c990);
                }
            }
        }
    }

    public boolean isBusy() {
        return dl != null;
    }
}
