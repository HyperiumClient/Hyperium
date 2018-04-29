package cc.hyperium.installer;

import cc.hyperium.installer.components.MaterialRadioButton;
import cc.hyperium.installer.components.MotionPanel;
import com.google.common.io.Files;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import javax.swing.plaf.metal.MetalScrollBarUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Created by Cubxity on 12/04/2018
 */
class InstallerConfig extends JFrame {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 260;

    InstallerConfig() {
        super.frameInit();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(WIDTH, HEIGHT);
        this.setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
        initializeComponents();
        setResizable(false);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setTitle("Hyperium Installer");

        setIconImage(new ImageIcon(getClass().getResource("/assets/hyperium/icons/icon-32x.png")).getImage());

        this.setVisible(true);
        this.setLayout(null);
    }

    private void initializeComponents() {
        Font f;
        try {
            f = Font.createFont(Font.TRUETYPE_FONT, InstallerMain.class.getResourceAsStream("/assets/hyperium/fonts/Montserrat-Regular.ttf")).deriveFont(12.0F);
        } catch (FontFormatException | IOException e) {
            f = Font.getFont("Arial"); //backup
            e.printStackTrace();
        }
        JPanel topPanel = new MotionPanel(this);
        topPanel.setBackground(new Color(30, 30, 30));
        topPanel.setBounds(0, 0, WIDTH, 20);
        topPanel.setLayout(null);

        JButton install = new JButton("INSTALL");
        install.setBackground(new Color(255, 254, 254));
        install.setForeground(new Color(30, 30, 30));
        install.setFont(f);
        install.setBorderPainted(false);
        install.setFocusPainted(false);
        install.setBounds(WIDTH - 105, HEIGHT - 25, 100, 20);
        install.setEnabled(false);

        JButton exit = new JButton("EXIT");
        exit.setBackground(new Color(255, 254, 254));
        exit.setForeground(new Color(30, 30, 30));
        exit.setFont(f);
        exit.setBorderPainted(false);
        exit.setFocusPainted(false);
        exit.setBounds(WIDTH - 185, HEIGHT - 25, 70, 20);
        exit.addActionListener(event -> System.exit(0));

        JLabel dirTxt = new JLabel("Minecraft Installation");
        dirTxt.setLocation(5, 22);
        dirTxt.setSize(250, 10);
        dirTxt.setFont(f);
        dirTxt.setForeground(Color.WHITE);

        JTextField dir = new JTextField();
        dir.setText(getMinecraftDir().getAbsolutePath());
        dir.setBounds(200, 22, 395, 14);
        dir.setFont(f);
        dir.setBorder(BorderFactory.createEmptyBorder());
        dir.setBackground(new Color(25, 25, 25));
        dir.setForeground(Color.WHITE);

        JLabel wamTxt = new JLabel("Ram to allocate (1GB)");
        wamTxt.setLocation(5, 44);
        wamTxt.setSize(250, 10);
        wamTxt.setFont(f);
        wamTxt.setForeground(Color.WHITE);

        JSlider wam = new JSlider();
        wam.setMaximum(8);
        wam.setMinimum(1);
        wam.setValue(1);
        wam.setBounds(200, 42, 395, 14);
        wam.setBackground(new Color(30, 30, 30));
        wam.setForeground(Color.WHITE);
        wam.addChangeListener(e -> wamTxt.setText("Ram to allocate (" + wam.getValue() + "GB)"));
        wam.setUI(new BasicSliderUI(wam) {
            private final Stroke s = new BasicStroke(1f);

            @Override
            public void paintTrack(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                Stroke old = g2d.getStroke();
                g2d.setPaint(Color.WHITE);
                g2d.setStroke(s);
                g2d.drawLine(trackRect.x, trackRect.y + trackRect.height / 2, trackRect.x + trackRect.width, trackRect.y + trackRect.height / 2);
                g2d.setStroke(old);
            }

            @Override
            public void paintFocus(Graphics g) {
            }

            @Override
            public void paintThumb(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(Color.WHITE);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.fill(new Ellipse2D.Double(thumbRect.x, thumbRect.y + 4.5, thumbRect.width, thumbRect.height - 9));
            }
        });

        JRadioButton accept = new MaterialRadioButton("I accept the terms in License agreement and and Privacy Policy");
        accept.setFont(f);
        accept.setBounds(5, HEIGHT - 40, 500, 15);
        accept.addActionListener(e -> install.setEnabled(accept.isSelected()));

        JButton license = new JButton("LICENSE");
        license.setBackground(new Color(255, 254, 254));
        license.setForeground(new Color(30, 30, 30));
        license.setFont(f);
        license.setBorderPainted(false);
        license.setFocusPainted(false);
        license.setBounds(5, HEIGHT - 25, 100, 20);
        license.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URL("https://github.com/HyperiumClient/Hyperium/blob/master/LICENSE").toURI());
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }
        });

        JButton privacy = new JButton("PRIVACY");
        privacy.setBackground(new Color(255, 254, 254));
        privacy.setForeground(new Color(30, 30, 30));
        privacy.setFont(f);
        privacy.setBorderPainted(false);
        privacy.setFocusPainted(false);
        privacy.setBounds(115, HEIGHT - 25, 100, 20);
        privacy.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URL("https://hyperium.cc/#privacy").toURI());
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }
        });

        JScrollPane components = new JScrollPane();
        components.setBounds(5, 60, 250, 160);
        components.setBorder(BorderFactory.createEmptyBorder());
        components.getVerticalScrollBar().setBackground(new Color(28, 28, 28));
        UIManager.put("ScrollBar.width", 10);
        components.getVerticalScrollBar().setUI(new MetalScrollBarUI() {

            JButton zero() {
                JButton b = new JButton();
                Dimension d = new Dimension(0, 0);
                b.setPreferredSize(d);
                b.setMaximumSize(d);
                b.setMinimumSize(d);
                return b;
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            }

            @Override
            protected void paintDecreaseHighlight(Graphics g) {
            }

            @Override
            protected void paintIncreaseHighlight(Graphics g) {
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return zero();
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return zero();
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(Color.WHITE);
                g2d.fillRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);
            }
        });

        JPanel cView = new JPanel();
        cView.setLayout(new BoxLayout(cView, BoxLayout.Y_AXIS));
        cView.setBackground(new Color(28, 28, 28));

        components.setViewportView(cView);

        JLabel cLabel = new JLabel("Components");
        cLabel.setFont(f.deriveFont(14f));
        cLabel.setForeground(Color.WHITE);
        cLabel.setBounds(260, 60, 250, 17);

        JTextArea cDesc = new JTextArea("Select components you would like it to be installed");
        cDesc.setFont(f);
        cDesc.setForeground(new Color(250, 250, 250));
        cDesc.setBackground(new Color(30, 30, 30));
        cDesc.setBounds(260, 77, 350, 140);
        cDesc.setLineWrap(true);
        cDesc.setWrapStyleWord(true);

        JRadioButton optifine = new MaterialRadioButton("Optifine");
        optifine.setFont(f);
        optifine.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                cLabel.setText("Components");
                cDesc.setText("Select components you would like it to be installed");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                cLabel.setText("Optifine");
                cDesc.setText("Optifine is a Minecraft optimization mod.\n" +
                        "It allows Minecraft to run faster and look better with full support for HD textures and many configuration options.\n\n" +
                        "Read more about Optifine: http://www.minecraftforum.net/topic/249637-");
            }
        });
        optifine.setVerticalAlignment(SwingConstants.TOP);
        optifine.setHorizontalAlignment(SwingConstants.LEFT);
        optifine.setBackground(new Color(28, 28, 28));
        cView.add(optifine);

        try {
            JSONObject versions = new JSONObject(InstallerFrame.get("https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/installer/versions.json"));
            for (Object o : versions.getJSONArray("addons"))
                if (o instanceof JSONObject) {
                    JSONObject j = (JSONObject) o;
                    JRadioButton b = new MaterialRadioButton("Addon :: " + j.getString("name"));
                    b.setFont(f);
                    b.setEnabled(!j.getString("url").isEmpty());
                    b.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseExited(MouseEvent e) {
                            cLabel.setText("Components");
                            cDesc.setText("Select components you would like it to be installed");
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            cLabel.setText(j.getString("name") + (j.getBoolean("verified") ? " (Verified)" : ""));
                            cDesc.setText(j.getString("description") + "\n\nVersion: " + j.getString("version") + "\nAuthor: " + j.getString("author"));
                        }
                    });
                    b.setVerticalAlignment(SwingConstants.TOP);
                    b.setHorizontalAlignment(SwingConstants.LEFT);
                    b.setBackground(new Color(28, 28, 28));
                    cView.add(b);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Container contentPane = getContentPane();
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(30, 30, 30));

        contentPane.add(topPanel);
        contentPane.add(install);
        contentPane.add(exit);
        contentPane.add(dirTxt);
        contentPane.add(dir);
        contentPane.add(wamTxt);
        contentPane.add(wam);
        contentPane.add(accept);
        contentPane.add(license);
        contentPane.add(privacy);
        contentPane.add(components);
        contentPane.add(cLabel);
        contentPane.add(cDesc);

        install.addActionListener(e -> {
            System.out.println("Starting to install Hyperium...");
            Arrays.asList(contentPane.getComponents()).forEach(contentPane::remove);
            List<String> cs = Arrays.stream(((JPanel) components.getViewport().getComponent(0)).getComponents()).filter(c -> ((JRadioButton) c).isSelected()).map((c) -> ((JRadioButton) c).getText()).collect(Collectors.toList());

            // Save current installer state so next installation will start with same config first
            try {
                File stateFile = new File(System.getProperty("user.home"), "hinstaller-state.json");
                JSONObject state = new JSONObject();
                state.put("dir", dir.getText());
                state.put("wam", wam.getValue());
                JSONArray a = new JSONArray();
                cs.forEach(a::put);
                state.put("components", a);
                Files.write(state.toString(4), stateFile, Charset.defaultCharset());
            } catch (Exception ex) {
                ex.printStackTrace();
                System.err.println("Failed to save current installer state!");
            }

            new Thread(() -> new InstallerFrame(dir.getText(), wam.getValue(), cs, this), "Installer-Thread").start();
        });

        // Load last state if exists
        try {
            JSONObject state = getLastState();
            if (state != null) {
                dir.setText(state.getString("dir"));
                wam.setValue(state.getInt("wam"));
                List<String> cs = state.getJSONArray("components").toList().stream().map(o -> (String) o).collect(Collectors.toList());
                for (Component comp : ((JPanel) components.getViewport().getComponent(0)).getComponents()) {
                    JRadioButton b = (JRadioButton) comp;
                    if (cs.contains(b.getText()))
                        b.setSelected(true);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Failed to load last state");
        }
    }

    private JSONObject getLastState() {
        File stateFile = new File(System.getProperty("user.home"), "hinstaller-state.json");
        if (!stateFile.exists())
            return null;
        try {
            return new JSONObject(Files.toString(stateFile, Charset.defaultCharset()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File getMinecraftDir() {
        switch (InstallerFrame.OsCheck.getOperatingSystemType()) {
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
}
