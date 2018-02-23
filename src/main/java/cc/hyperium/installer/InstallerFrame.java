/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  Hyperium Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.installer;

import com.google.common.io.Files;
import cc.hyperium.installer.components.MotionPanel;
import cc.hyperium.installer.utils.DownloadTask;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Cubxity
 */
public class InstallerFrame extends JFrame implements PropertyChangeListener {
    /**
     * Width & height for installer GUI
     */
    private static final int WIDTH = 400;
    private static final int HEIGHT = 160;
    private JLabel display;
    private JLabel error;
    private JProgressBar progressBar;
    private JButton exit;
    private String mcDir;

    /**
     * Constructor
     * @param mcDir
     */
    InstallerFrame(String mcDir) {
        super.frameInit();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(WIDTH, HEIGHT);
        this.setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
        initComponents();
        setResizable(false);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setTitle("Hyperium Installer");

        setIconImage(new ImageIcon(getClass().getResource("/assets/hyperium/icons/icon-32x.png")).getImage());

        this.setVisible(true);
        this.setLayout(null);
        this.mcDir = mcDir;
        try {
            install();
        } catch (Throwable ex) {
            display.setText("EXCEPTION OCCURRED");
            error.setText(ex.getMessage());
            exit.setVisible(true);
        }
    }

    private static String toHex(byte[] bytes) {
        return DatatypeConverter.printHexBinary(bytes);
    }

    /**
     * Method to do everything
     */
    private void install() {
        File mc = getMinecraftDir();
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
        String channel = "latest-dev";
        JSONObject versionsJson;
        AtomicReference<JSONObject> version = new AtomicReference<>();
        File downloaded;

        try {
            versionsJson = new JSONObject(get(versions_url));
            JSONArray versionsArray = versionsJson.getJSONArray("versions");
            List<JSONObject> versionsObjects = new ArrayList<>();
            for (Object o : versionsArray)
                versionsObjects.add((JSONObject) o);

            versionsObjects.forEach(o -> {
                if (o.getString("name").equals(versionsJson.getString(channel)))
                    version.set(o);
            });
            File dl = new File(
                    new File(
                            mc,
                            "libraries"),
                    version.get().getString("path").replaceAll("/Hyperium-.+?\\..+?\\.jar", ""));
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
        } catch (IOException | InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
            display.setText("INSTALLATION FAILED");
            error.setText("FAILED TO GATHER REQUIRED FILES");
            exit.setVisible(true);
            return;
        }
        display.setText("VERIFYING FILE");
        String hash = toHex(checksum(downloaded, "SHA-256")).toLowerCase();
        System.out.println("SHA-256 Checksum = " + hash);
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
        File optifine = new File(InstallerMain.class.getResource("/mods/OptiFine_1.8.9_HD_U_I3.jar").getFile());
        progressBar.setValue(91);
        display.setText("COPYING FILES");
        target.mkdir();
        File targetJson = new File(target, "Hyperium 1.8.9.json");
        File targetJar = new File(target, "Hyperium 1.8.9.jar");
        try {
            Files.copy(originJson, targetJson);
            targetJar.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            display.setText("INSTALLATION FAILED");
            error.setText("FAILED TO COPY FILES FROM VERSION '1.8.9'");
            exit.setVisible(true);
            return;
        }
        progressBar.setValue(95);
        display.setText("PATCHING OPTIFINE");
        ProcessBuilder builder = new ProcessBuilder("java", "-cp", optifine.getAbsolutePath(), "optifine.Patcher", originJar.getAbsolutePath(), optifine.getAbsolutePath(), targetJar.getAbsolutePath());
        builder.inheritIO();
        builder.redirectErrorStream(true);
        Process proc;
        try {
             proc = builder.start();
            if(proc.waitFor()!=0)
                throw new IOException("Failed to install optifine");
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            display.setText("INSTALLATION FAILED");
            error.setText("FAILED TO PATCH OPTIFINE");
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
        lib.put("name", version.get().getString("artifact-name"));
        lib.put("downloads", new JSONObject().put("artifact", new JSONObject()
                .put("size", version.get().getLong("size"))
                .put("sha1", hash)
                .put("path", version.get().getString("path"))
                .put("url", version.get().getString("url"))
        ));
        JSONArray libs = json.getJSONArray("libraries");
        libs.put(lib);
        libs.put(new JSONObject().put("name", "net.minecraft:launchwrapper:1.7"));
        json.put("libraries", libs);
        json.put("id", "Hyperium 1.8.9");
        json.put("mainClass", "net.minecraft.launchwrapper.Launch");
        json.put("minecraftArguments", json.getString("minecraftArguments") + " --tweakClass=" + version.get().getString("tweak-class"));

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
                .put("lastVersionId", "Hyperium 1.8.9"));
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

    /**
     * @return default minecraft directory for every OS
     */
    private File getMinecraftDir() {
        if(mcDir!=null)
            return new File(mcDir);
        switch (OsCheck.getOperatingSystemType()) {
            case Linux:
                return new File(System.getProperty("user.home"), ".minecraft");
            case Windows:
                return new File(System.getenv("APPDATA"), ".minecraft");
            case MacOS:
                return new File(System.getProperty("user.home") + "/Library/Application Support", "minecraft");
            default:
                return new File(System.getProperty("user.home"), ".minecraft");
        }
    }

    /**
     * Initialize components for GUI
     */
    private void initComponents() {
        // Top Panel
        JPanel topPanel = new MotionPanel(this);
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
        exit.setBounds((WIDTH - 70) / 2, 130, 70, 20);
        exit.addActionListener(event -> System.exit(0));
        exit.setVisible(false);

        // Container
        Container contentPane = getContentPane();
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(30, 30, 30));

        // Add Items
        contentPane.add(topPanel);
        contentPane.add(progressBar);
        contentPane.add(display);
        contentPane.add(error);
        contentPane.add(exit);

        // Finalize
        setContentPane(contentPane);
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

    private String get(String url) throws IOException {
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
    static class OsCheck {
        // cached result of OS detection
        private static OSType detectedOS;

        /**
         * detect the operating system from the os.name System property and cache
         * the result
         *
         * @return - the operating system detected
         */
        static OSType getOperatingSystemType() {
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
        enum OSType {
            Windows, MacOS, Linux, Other
        }
    }
}
