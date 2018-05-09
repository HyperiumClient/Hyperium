package cc.hyperium.gui;

import cc.hyperium.Metadata;
import cc.hyperium.installer.InstallerMain;
import cc.hyperium.internal.addons.AddonBootstrap;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.packets.serverbound.ServerCrossDataPacket;
import cc.hyperium.utils.JsonHolder;
import com.google.gson.JsonParser;
import net.minecraft.crash.CrashReport;
import net.minecraft.init.Bootstrap;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

/*
 * Created by Cubxity on 04/05/2018
 */
public class CrashReportGUI extends JDialog {
    private CrashReport report;
    private int handle = 0; // 0 - Force stop, 1 - Soft shutdown, 2 - Restart

    private CrashReportGUI(CrashReport report) {
        super();
        this.report = report;

        setModal(true);
        setTitle("Hyperium crash report");
        setSize(200, 300);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(d.width / 2 - getWidth() / 2, d.height / 2 - getHeight() / 2);

        initComponents();

        setAlwaysOnTop(true);
    }

    private void initComponents() {
        Container c = getContentPane();
        c.setBackground(new Color(30, 30, 30));
        c.setLayout(null);

        Font f;
        try {
            f = Font.createFont(Font.TRUETYPE_FONT, InstallerMain.class.getResourceAsStream("/assets/hyperium/fonts/Montserrat-Regular.ttf")).deriveFont(12.0F);
        } catch (FontFormatException | IOException e) {
            f = Font.getFont("Arial"); //backup
            e.printStackTrace();
        }
        JLabel error = null;
        try {
            error = new JLabel(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/assets/hyperium/icons/error.png")).getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
            error.setHorizontalAlignment(SwingConstants.CENTER);
            error.setBounds(50, 50, 100, 100);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel crash = new JLabel("Game crash");
        crash.setHorizontalAlignment(SwingConstants.CENTER);
        crash.setBounds(0, 2, 200, 40);
        crash.setBackground(new Color(30, 30, 30));
        crash.setForeground(Color.WHITE);
        crash.setFont(f);
        JButton discord = null;
        try {
            discord = new JButton(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/assets/hyperium/icons/discord.png")).getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
            discord.setBounds(40, 13, 20, 20);
            discord.setBackground(new Color(30, 30, 30));
            discord.setBorderPainted(false);
            discord.setFocusPainted(false);
            discord.addActionListener(e -> {
                try {
                    Desktop.getDesktop().browse(new URL("https://discord.gg/txWB832").toURI());
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        String t = report == null ? "Crash unknown" : report.getDescription();
        JLabel desc = new JLabel(t);
        desc.setHorizontalAlignment(SwingConstants.CENTER);
        desc.setBounds(0, 175, 200, 20);
        desc.setBackground(new Color(30, 30, 30));
        desc.setForeground(Color.WHITE);
        desc.setFont(resize(t, 190, f, desc));

        JButton report = new JButton("REPORT");
        report.setBackground(new Color(255, 254, 254));
        report.setForeground(new Color(30, 30, 30));
        report.setFont(f);
        report.setBorderPainted(false);
        report.setFocusPainted(false);
        report.setBounds(2, 208, 196, 20);
        report.addActionListener(e -> {
            report.setEnabled(false);
            report.setText("REPORTING...");
            if (sendReport()) {
                report.setEnabled(false);
                report.setText("REPORTED");
            } else if (copyReport()) {
                report.setEnabled(false);
                report.setText("COPIED TO CLIPBOARD");
            } else {
                report.setText("FAILED TO REPORT");
            }
        });

        JButton restart = new JButton("RESTART");
        restart.setBackground(new Color(255, 254, 254));
        restart.setForeground(new Color(30, 30, 30));
        restart.setFont(f);
        restart.setBorderPainted(false);
        restart.setFocusPainted(false);
        restart.setBounds(2, 230, 196, 20);
        restart.addActionListener(a -> {
            handle = 2;
            restart.setText("RESTARTING...");
            dispose();
        });

        JButton exit = new JButton("EXIT");
        exit.setBackground(new Color(255, 254, 254));
        exit.setForeground(new Color(30, 30, 30));
        exit.setFont(f);
        exit.setBorderPainted(false);
        exit.setFocusPainted(false);
        exit.setBounds(2, 252, 196, 20);
        exit.addActionListener(a -> {
            handle = 1;
            dispose();
        });

        c.add(crash);
        c.add(desc);
        c.add(report);
        c.add(restart);
        c.add(exit);
        if (error != null)
            c.add(error);
        if (discord != null)
            c.add(discord);
    }

    private boolean sendReport() {
        try {
            AtomicReference<String> addons = new AtomicReference<>("");
            try {
                AddonBootstrap.INSTANCE.getAddonManifests().forEach(m -> addons.getAndUpdate(a -> a + m.getName() + ", "));
            } catch (Exception ex) {
                ex.printStackTrace();
                // Addons bootstrap might not be initialized
            }
            if (addons.get().isEmpty())
                addons.set("none");
            String hurl = null;

            if (report != null)
                hurl = haste(report.getCompleteReport());

            if (report != null && hurl == null)
                return false;

            NettyClient.getClient().write(ServerCrossDataPacket.build(new JsonHolder().put("crash_report", true).put("internal", true).put("crash",
                    new JsonHolder()
                            .put("crash-full", report == null ? "unavailable" : hurl)
                            .put("hyperium", Metadata.getVersion())
                            .put("addons", addons.toString())
            )));
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private boolean copyReport() {
        try {
            AtomicReference<String> addons = new AtomicReference<>("");
            try {
                AddonBootstrap.INSTANCE.getAddonManifests().forEach(m -> addons.getAndUpdate(a -> a + m.getName() + ", "));
            } catch (Exception ex) {
                ex.printStackTrace();
                // Addons bootstrap might not be initialized
            }
            if (addons.get().isEmpty())
                addons.set("none");
            String hurl = null;
            if (report != null)
                hurl = haste(report.getCompleteReport());

            if (report != null && hurl == null)
                return false;
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection((hurl == null ? "Report unavailable" : hurl) + "\n\nHyperium: " + Metadata.getVersion() + "\nAddons: " + addons.get()), null);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static int handle(CrashReport report) {
        try {
            CrashReportGUI crg = new CrashReportGUI(report);
            crg.setVisible(true);
            return crg.handle;
        } catch (Exception ex) {
            ex.printStackTrace();
            Bootstrap.printToSYSOUT("## FAILED TO HANDLE CRASH WITH HYPERIUM CRASH REPORT GUI ##");
        }
        return 0;
    }

    private Font resize(String s, int width, Font f, JLabel l) {
        if (l.getFontMetrics(f).stringWidth(s) > width)
            return resize(s, width, f.deriveFont(f.getSize() - 1f), l);
        return f;
    }

    private String haste(String txt) {
        try {
            HttpClient hc = HttpClients.createDefault();
            HttpPost post = new HttpPost("https://hastebin.com/documents");
            post.setEntity(new StringEntity(txt));

            HttpResponse response = hc.execute(post);

            String result = EntityUtils.toString(response.getEntity());
            return "http://hastebin.com/" + new JsonParser().parse(result).getAsJsonObject().get("key").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        // For testing
        handle(null);
    }
}
