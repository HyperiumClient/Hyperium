/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.installer;

import cc.hyperium.installer.components.MotionPanel;
import cc.hyperium.installer.utils.DownloadTask;
import cc.hyperium.internal.addons.AddonManifest;
import cc.hyperium.internal.addons.misc.AddonManifestParser;
import com.google.common.io.Files;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.JarFile;

/**
 * @author Cubxity
 */
public class InstallerFrame implements PropertyChangeListener {
    /**
     * Width & height for installer GUI
     */
    private static final int WIDTH = 400;
    private static final int HEIGHT = 160;
    private static final char[] hexCodes = "0123456789ABCDEF".toCharArray();
    private StringBuilder log = new StringBuilder();
    private JLabel display;
    private JLabel error;
    private JProgressBar progressBar;
    private JButton exit;
    private final JFrame frame;

    /**
     * Constructor
     */
    InstallerFrame(String dir, int wam, List<String> components, InstallerConfig frame, String version) {
        final PrintStream ps = System.out;
        PrintStream logStream = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                log.append((char) b);
                ps.append((char) b);
            }
        });
        System.setOut(logStream);
        System.setErr(logStream);
        this.frame = frame;
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == 'l') {
                    try {
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(new StringSelection(log.toString()), null);
                        error.setText("LOG COPIED TO THE CLIPBOARD");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        error.setText("FAILED TO COPY THE LOG");
                    }
                }
            }
        });
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
        initComponents();
        install(new File(dir), wam, components, version);
    }

    private static String toHex(byte[] bytes) {
        StringBuilder r = new StringBuilder(bytes.length * 2);

        for (byte b : bytes) {
            r.append(hexCodes[(b >> 4) & 0xF]);
            r.append(hexCodes[(b & 0xF)]);
        }

        return r.toString();
    }

    public static String get(String url) throws IOException {
        URL u = new URL(url);
        HttpsURLConnection conn = (HttpsURLConnection) u.openConnection();
        conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0");
        InputStream is = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String inputLine;
        StringBuilder out = new StringBuilder();
        while ((inputLine = br.readLine()) != null) {
            out.append(inputLine);
        }
        br.close();
        return out.toString();
    }

    /**
     * Method to do everything
     */
    private void install(File mc, int wam, List<String> components, String ver) {
        if (!mc.exists()) {
            display.setText("INSTALLATION FAILED");
            error.setText("NO MINECRAFT INSTALLATION FOUND");
            exit.setVisible(true);
            return;
        }
        progressBar.setValue(20);
        display.setText("CHECKING VERSION");
        File versions = new File(mc, "versions");
        File origin = new File(versions, "1.8.9");
        File originJson = new File(origin, "1.8.9.json");
        File originJar = new File(origin, "1.8.9.jar");
        if (!origin.exists() || !originJson.exists() || !originJar.exists()) {
            display.setText("INSTALLATION FAILED");
            error.setText("VERSION '1.8.9' DOES NOT EXIST");
            exit.setVisible(true);
            return;
        }
        progressBar.setValue(30);
        display.setText("CHECKING PREVIOUS INSTALLATION");
        File target = new File(versions, "Hyperium 1.8.9");
        if (target.exists()) {
            try {
                delete(target);
            } catch (IOException e) {
                e.printStackTrace();
                display.setText("INSTALLATION FAILED");
                error.setText("COULDN'T DELETE OLD INSTALLATION, IS THE GAME RUNNING?");
                exit.setVisible(true);
                return;
            }
        }
        progressBar.setValue(40);
        display.setText("GETTING FILES");
        String versions_url = "https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/installer/versions.json";
        AtomicReference<JSONObject> version = new AtomicReference<>();
        String hash = null;
        JSONObject versionsJson;
        try {
            versionsJson = new JSONObject(get(versions_url));
            JSONArray versionsArray = versionsJson.getJSONArray("versions");
            List<JSONObject> versionsObjects = new ArrayList<>();
            for (Object o : versionsArray)
                versionsObjects.add((JSONObject) o);

            versionsObjects.forEach(o -> {
                if (o.getString("name").equals(ver))
                    version.set(o);
            });
        } catch (IOException ex) {
            ex.printStackTrace();
            display.setText("INSTALLATION FAILED");
            error.setText("FAILED TO FETCH JSON DATA");
            exit.setVisible(true);
            return;
        }
        if (ver.equals("LOCAL")) {
            File local;
            try {
                local = new File(InstallerMain.class.getProtectionDomain().getCodeSource().getLocation().toURI());
                System.out.println("local="+local.getPath());
            } catch (URISyntaxException e) {
                e.printStackTrace();
                display.setText("INSTALLATION FAILED");
                error.setText("FAILED TO GET LOCAL JAR");
                exit.setVisible(true);
                return;
            }
            progressBar.setValue(90);
            display.setText("COPYING FILES...");
            try {
                new File(new File(mc, "libraries"), "cc/hyperium/Hyperium/LOCAL").mkdirs();
                Files.copy(local, new File(new File(mc, "libraries"), "cc/hyperium/Hyperium/LOCAL" + File.separator + "Hyperium-LOCAL.jar"));
            } catch (IOException e) {
                e.printStackTrace();
                display.setText("INSTALLATION FAILED");
                error.setText("FAILED TO COPY LOCAL JAR");
                return;
            }
        } else {
            File downloaded;
            try {
                File dl = new File(
                        new File(
                                mc,
                                "libraries"),
                        version.get().getString("path").replaceAll("/Hyperium-\\d\\.\\d\\.jar", ""));
                System.out.println("Download dest folder = " + dl.getAbsolutePath());
                //noinspection ResultOfMethodCallIgnored
                dl.mkdirs();
                DownloadTask task = new DownloadTask(
                        version.get().getString("url"),
                        dl.getAbsolutePath());
                task.addPropertyChangeListener(this);
                task.execute();
                task.get();
                downloaded = new File(dl, task.getFileName());
                System.out.println("Download dest file = " + downloaded.getAbsolutePath());
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
                display.setText("INSTALLATION FAILED");
                error.setText("FAILED TO GATHER REQUIRED FILES");
                exit.setVisible(true);
                return;
            }

            display.setText("VERIFYING FILE");
            hash = toHex(checksum(downloaded, "SHA-256")).toLowerCase();
            System.out.println("SHA-256 Checksum = " + hash + " Expected: " + version.get().getString("sha256"));
            if (!hash.equals(version.get().getString("sha256"))) {
                display.setText("INSTALLATION FAILED");
                error.setText("FILE'S SHA256 CHECKSUM DOES NOT MATCH");
                exit.setVisible(true);
                return;
            }
            hash = toHex(checksum(downloaded, "SHA1")).toLowerCase();
            System.out.println("SHA-1 Checksum = " + hash);
            if (!hash.equals(version.get().getString("sha1"))) {
                display.setText("INSTALLATION FAILED");
                error.setText("FILE'S SHA1 CHECKSUM DOES NOT MATCH");
                exit.setVisible(true);
                return;
            }
        }
        File optifine = null;
        if (components.contains("Optifine")) {
            try {
                optifine = exportTempOptifine();
                optifine.deleteOnExit();
            } catch (Exception e) {
                e.printStackTrace();
                display.setText("INSTALLATION FAILED");
                error.setText("FAILED TO FIND OPTIFINE FILE");
                exit.setVisible(true);
                return;
            }
            System.out.println("Temp optifine=" + optifine.getAbsolutePath());
        }
        progressBar.setValue(91);
        display.setText("COPYING FILES");
        target.mkdir();
        File targetJson = new File(target, "Hyperium 1.8.9.json");
        File targetJar = new File(target, "Hyperium 1.8.9.jar");
        try {
            Files.copy(originJson, targetJson);
            Files.copy(originJar, targetJar);
        } catch (IOException e) {
            e.printStackTrace();
            display.setText("INSTALLATION FAILED");
            error.setText("FAILED TO COPY FILES FROM VERSION '1.8.9'");
            exit.setVisible(true);
            return;
        }
        progressBar.setValue(95);
        display.setText("INSTALLING COMPONENTS");
        if (components.contains("Optifine")) {
            File optifineLibDir = new File(mc, "libraries/optifine/OptiFine/1.8.9_HD_U_I7");
            optifineLibDir.mkdirs();
            File optifineLib = new File(optifineLibDir, "OptiFine-1.8.9_HD_U_I7.jar");
            ProcessBuilder builder = new ProcessBuilder("java", "-cp", optifine.getAbsolutePath(), "optifine.Patcher", originJar.getAbsolutePath(), optifine.getAbsolutePath(), optifineLib.getAbsolutePath());
            builder.inheritIO();
            builder.redirectErrorStream(true);
            Process proc;
            try {
                proc = builder.start();
                if (proc.waitFor() != 0)
                    throw new IOException("Failed to install components");
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
                display.setText("INSTALLATION FAILED");
                error.setText("FAILED TO PATCH OPTIFINE");
                exit.setVisible(true);
                return;
            }
        }
        Map<File, AddonManifest> installedAddons = new HashMap<>();
        File addonsDir = new File(mc, "addons");
        if (addonsDir.exists()) {
            for (File a : Objects.requireNonNull(addonsDir.listFiles((dir, name) -> name.endsWith(".jar")))) {
                try {
                    installedAddons.put(a, new AddonManifestParser(new JarFile(a)).getAddonManifest());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else addonsDir.mkdirs();
        try {
            List<JSONObject> ao = new ArrayList<>();
            for (Object o : versionsJson.getJSONArray("addons"))
                ao.add((JSONObject) o);
            for (String comp : components) {
                if (comp.startsWith("Addon :: ")) {
                    String aid = comp.replace("Addon :: ", "");
                    Optional<JSONObject> addon = ao.stream().filter(jo -> jo.getString("name").equals(aid)).findAny();
                    if (addon.isPresent()) {
                        installedAddons.forEach((f, m) -> {
                            if (m.getName() != null)
                                if (m.getName().equals(aid))
                                    if (!f.delete())
                                        System.err.println("Failed to delete: " + f.getAbsolutePath());
                        });
                        try {
                            System.out.println("Downloading: " + addon.get().getString("url"));
                            File aOut = new File(addonsDir, aid + "-" + addon.get().getString("version") + ".jar");
                            download(aOut, addon.get().getString("url"));
                            if (!toHex(checksum(aOut, "SHA-256")).equalsIgnoreCase(addon.get().getString("sha256"))) {
                                display.setText("INSTALLATION FAILED");
                                error.setText("COMPONENT'S SHA256 DOES NOT MATCH");
                                exit.setVisible(true);
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println("Failed to install " + comp);
                            display.setText("INSTALLATION FAILED");
                            error.setText("FAILED TO INSTALL COMPONENTS");
                            exit.setVisible(true);
                            return;
                        }
                    } else System.err.println(comp + " is not found");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            display.setText("INSTALLATION FAILED");
            error.setText("FAILED TO INSTALL COMPONENTS");
            exit.setVisible(true);
            return;
        }
        progressBar.setValue(99);
        display.setText("CREATING PROFILE");
        //noinspection ResultOfMethodCallIgnored
        JSONObject json;
        JSONObject launcherProfiles;
        try {
            json = new JSONObject(Files.toString(targetJson, Charset.defaultCharset()));
            launcherProfiles = new JSONObject(Files.toString(new File(mc, "launcher_profiles.json"), Charset.defaultCharset()));
        } catch (IOException e) {
            e.printStackTrace();
            display.setText("INSTALLATION FAILED");
            error.setText("FAILED TO READ JSON FILES");
            return;
        }
        JSONObject lib = new JSONObject();
        lib.put("name", ver.equals("LOCAL") ? "cc.hyperium:Hyperium:LOCAL" : version.get().getString("artifact-name"));
        if (version.get() != null && hash != null)
            lib.put("downloads", new JSONObject().put("artifact", new JSONObject()
                    .put("size", version.get().getLong("size"))
                    .put("sha1", hash)
                    .put("path", version.get().getString("path"))
                    .put("url", version.get().getString("url"))
            ));
        JSONArray libs = json.getJSONArray("libraries");
        libs.put(lib);
        libs.put(new JSONObject().put("name", "net.minecraft:launchwrapper:1.7"));
        if (components.contains("Optifine"))
            libs.put(new JSONObject().put("name", "optifine:OptiFine:1.8.9_HD_U_I7"));
        versionsJson.getJSONArray("libs").forEach(libs::put);
        json.put("libraries", libs);
        json.put("id", "Hyperium 1.8.9");
        json.put("mainClass", "net.minecraft.launchwrapper.Launch");
        json.put("minecraftArguments", json.getString("minecraftArguments") + " --tweakClass=" + (ver.equals("LOCAL") ? "cc.hyperium.launch.HyperiumTweaker" : version.get().getString("tweak-class")));

        JSONObject profiles = launcherProfiles.getJSONObject("profiles");
        Instant instant = Instant.ofEpochMilli(System.currentTimeMillis());
        String installedUUID = UUID.randomUUID().toString();
        for (String key : profiles.keySet()) {
            if (profiles.getJSONObject(key).has("name"))
                if (profiles.getJSONObject(key).getString("name").equals("Hyperium 1.8.9"))
                    installedUUID = key;
        }
        profiles.put(installedUUID, new JSONObject()
                .put("name", "Hyperium 1.8.9")
                .put("type", "custom")
                .put("created", instant.toString())
                .put("lastUsed", instant.toString())
                .put("lastVersionId", "Hyperium 1.8.9")
                .put("javaArgs", "-Xms512M -Xmx" + wam + "G -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:G1NewSizePercent=20 -XX:G1ReservePercent=20 -XX:MaxGCPauseMillis=50 -XX:G1HeapRegionSize=16M -XX:+DisableExplicitGC")
                .put("icon", "data:image/png;base64,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"));
        launcherProfiles.put("profiles", profiles);
        try {
            Files.write(json.toString(), targetJson, Charset.defaultCharset());
            Files.write(launcherProfiles.toString(), new File(mc, "launcher_profiles.json"), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
            display.setText("INSTALLATION FAILED");
            error.setText("FAILED TO WRITE JSON FILES");
            return;
        }
        progressBar.setValue(100);
        display.setText("INSTALLATION SUCCESS");
        error.setText("OPEN LAUNCHER AND LAUNCH 'Hyperium 1.8.9' PROFILE");
        exit.setVisible(true);
    }

    private File exportTempOptifine() throws Exception {
        File tmp = File.createTempFile("Optifine", ".jar");
        download(tmp, "https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/files/mods/OptiFine_1.8.9_HD_U_I7.jar");
        return tmp;
    }

    private void download(File file, String url) throws Exception {
        URLConnection conn = new URL(url).openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
        if (!file.exists())
            file.createNewFile();
        try (BufferedInputStream in = new BufferedInputStream(conn.getInputStream()); FileOutputStream fout = new FileOutputStream(file)) {
            final byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1)
                fout.write(data, 0, count);
        }
    }

    /**
     * Initialize components for GUI
     */
    private void initComponents() {
        // Top Panel
        JPanel topPanel = new MotionPanel(frame);
        topPanel.setBackground(new Color(30, 30, 30));
        topPanel.setBounds(0, 0, 400, 20);
        topPanel.setLayout(null);

        // Progressbar
        progressBar = new JProgressBar();
        progressBar.setBorderPainted(false);
        progressBar.setMaximum(100);
        progressBar.setValue(10);
        progressBar.setForeground(new Color(149, 201, 144));
        progressBar.setBackground(new Color(54, 54, 54));
        progressBar.setBounds((WIDTH - 300) / 2, 80, 300, 20);

        // Display Text
        display = new JLabel("CHECKING MINECRAFT", SwingConstants.CENTER);
        display.setSize(400, 20);
        display.setLocation(0, 50);
        try {
            display.setFont(Font.createFont(Font.TRUETYPE_FONT, InstallerMain.class.getResourceAsStream("/assets/hyperium/fonts/Montserrat-Regular.ttf")).deriveFont(15.0F));
        } catch (FontFormatException | IOException e) {
            display.setFont(Font.getFont("Arial")); //backup
            e.printStackTrace();
        }
        display.setForeground(new Color(255, 254, 254));

        // Error text
        error = new JLabel("", SwingConstants.CENTER);
        error.setSize(400, 10);
        error.setLocation(0, 105);
        try {
            error.setFont(Font.createFont(Font.TRUETYPE_FONT, InstallerMain.class.getResourceAsStream("/assets/hyperium/fonts/Montserrat-Regular.ttf")).deriveFont(10.0F));
        } catch (FontFormatException | IOException e) {
            error.setFont(Font.getFont("Arial")); //backup
            e.printStackTrace();
        }
        error.setForeground(new Color(255, 254, 254));

        // Exit
        exit = new JButton("EXIT");
        exit.setUI(new BasicButtonUI());
        exit.setBackground(new Color(255, 254, 254));
        exit.setForeground(new Color(30, 30, 30));
        try {
            exit.setFont(Font.createFont(Font.TRUETYPE_FONT, InstallerMain.class.getResourceAsStream("/assets/hyperium/fonts/Montserrat-Regular.ttf")).deriveFont(12.0F));
        } catch (FontFormatException | IOException e) {
            exit.setFont(Font.getFont("Arial")); //backup
            e.printStackTrace();
        }
        exit.setBorderPainted(false);
        exit.setFocusPainted(false);
        exit.setBounds((WIDTH - 100) / 2, 130, 100, 20);
        exit.addActionListener(event -> System.exit(0));
        exit.setVisible(false);

        // Container
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(30, 30, 30));

        // Add Items
        contentPane.add(topPanel);
        contentPane.add(progressBar);
        contentPane.add(display);
        contentPane.add(error);
        contentPane.add(exit);
    }

    /**
     * @param evt when progress of download changed
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("progress")) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(40 + progress / 2);
        }
    }

    private void delete(File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : Objects.requireNonNull(f.listFiles()))
                delete(c);
        }
        if (!f.delete())
            throw new FileNotFoundException("Failed to delete file: " + f);
    }

    private byte[] checksum(File input, String name) {
        try (InputStream in = new FileInputStream(input)) {
            MessageDigest digest = MessageDigest.getInstance(name);
            byte[] block = new byte[4096];
            int length;
            while ((length = in.read(block)) > 0) {
                digest.update(block, 0, length);
            }
            return digest.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Checks os
     */
    public static class OsCheck {
        // cached result of OS detection
        private static OSType detectedOS;

        /**
         * detect the operating system from the os.name System property and cache
         * the result
         *
         * @return - the operating system detected
         */
        public static OSType getOperatingSystemType() {
            if (detectedOS == null) {
                String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
                if ((OS.contains("mac")) || (OS.contains("darwin"))) {
                    detectedOS = OsCheck.OSType.MacOS;
                } else if (OS.contains("win")) {
                    detectedOS = OsCheck.OSType.Windows;
                } else if (OS.contains("nux")) {
                    detectedOS = OsCheck.OSType.Linux;
                } else {
                    detectedOS = OSType.Other;
                }
            }
            return detectedOS;
        }

        /**
         * types of Operating Systems
         */
        public enum OSType {
            Windows, MacOS, Linux, Other
        }
    }
}
