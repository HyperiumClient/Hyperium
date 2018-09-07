package net.montoyo.mcef.setup;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class SetupUI implements ActionListener, WindowListener, MouseListener {

    public static SetupUI INSTANCE = null;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Throwable t) {
            t.printStackTrace();
        }

        INSTANCE = new SetupUI();
    }

    private File selfDestruct = null;
    private JFrame frame;
    private GridLayout layout;
    private JPanel mainPane;
    private JButton btnInstall;
    private JButton btnConfigure;
    private JButton btnUninstall;
    private JButton btnExit;
    private JLabel aboutLabel;

    public SetupUI() {
        //Setup
        frame = new JFrame("MCEF Setup");
        frame.setMinimumSize(new Dimension(300, 100));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(this);

        //Layout & content
        btnInstall = new JButton("Install");
        btnConfigure = new JButton("Configure");
        btnUninstall = new JButton("Uninstall");
        btnExit = new JButton("Exit");

        btnInstall.addActionListener(this);
        btnConfigure.addActionListener(this);
        btnUninstall.addActionListener(this);
        btnExit.addActionListener(this);

        JPanel labelPane = new JPanel();
        labelPane.setLayout(new BoxLayout(labelPane, BoxLayout.PAGE_AXIS));
        labelPane.add(new JLabel("Welcome to the MCEF Setup Wizard."));
        labelPane.add(new JLabel("What do you like to do?"));

        mainPane = new JPanel();
        layout = new GridLayout(6, 1, 3, 3);
        mainPane.setBorder(new EmptyBorder(3, 3, 3, 3));
        mainPane.setLayout(layout);
        mainPane.add(labelPane);
        mainPane.add(btnInstall);
        mainPane.add(btnConfigure);
        mainPane.add(btnUninstall);
        mainPane.add(btnExit);

        aboutLabel = new JLabel("<html><i>MCEF was written by <u><font color=\"#000099\">montoyo</font></u></i>&nbsp;&nbsp;</html>");
        aboutLabel.setHorizontalAlignment(JLabel.RIGHT);
        aboutLabel.addMouseListener(this);
        mainPane.add(aboutLabel);

        //Display
        frame.setContentPane(mainPane);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnExit)
            windowClosing(null);
        else if(e.getSource() == btnInstall)
            new McLocationPrompt(frame, "install");
        else if(e.getSource() == btnConfigure)
            new McLocationPrompt(frame, "configure");
        else if(e.getSource() == btnUninstall)
            new McLocationPrompt(frame, "uninstall");
    }

    void initiateSelfDestruct(File f) {
        selfDestruct = f;
    }

    void abortSelfDestruct() {
        selfDestruct = null;
    }

    private void runSelfDestructionUnsafe() throws Throwable {
        File tmp = File.createTempFile("mcef-deleter", ".jar");
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tmp));
        InputStream is = SetupUI.class.getResourceAsStream("/net/montoyo/mcef/setup/Deleter.class");
        byte[] buf = new byte[8192];
        int read;

        zos.putNextEntry(new ZipEntry("net/montoyo/mcef/setup/Deleter.class"));
        while((read = is.read(buf)) > 0)
            zos.write(buf, 0, read);

        try {
            zos.closeEntry();
        } catch(Throwable t) {}

        SetupUtil.silentClose(zos);
        SetupUtil.silentClose(is);

        String java = "\"" + System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        if(System.getProperty("os.name").toLowerCase().contains("win"))
            java += "w.exe";

        java += "\" -classpath \"";
        java += tmp.getAbsolutePath();
        java += "\" net.montoyo.mcef.setup.Deleter \"";
        java += selfDestruct.getAbsolutePath();
        java += "\"";

        System.out.println("Running auto-deleter:");
        System.out.println(java);
        Runtime.getRuntime().exec(java);
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        frame.dispose();

        if(selfDestruct != null) {
            try {
                runSelfDestructionUnsafe();
            } catch(Throwable t) {
                System.err.println("Failed to destruct myself:");
                t.printStackTrace();
            }
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        try {
            Desktop.getDesktop().browse(new URI("https://montoyo.net"));
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
