package net.montoyo.mcef.setup;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

public class McLocationPrompt implements ActionListener, WindowListener {

    private JFrame parent;
    private JFrame frame;
    private GridLayout layout;
    private JPanel mainPane;
    private JTextField locationField;
    private JButton btnLocate;
    private JButton btnBack;
    private JButton btnOk;
    private String action;

    public McLocationPrompt(JFrame p, String action) {
        parent = p;
        this.action = action;

        //Setup
        frame = new JFrame("MCEF Setup - Minecraft location");
        frame.setMinimumSize(new Dimension(500, 1));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(this);

        mainPane = new JPanel();
        layout = new GridLayout(3, 1, 3, 3);
        mainPane.setBorder(new EmptyBorder(3, 3, 3, 3));
        mainPane.setLayout(layout);

        //First line: label
        mainPane.add(new JLabel("Please tell us where Minecraft is installed:"));

        //Second line: field location, locate button
        JPanel line = new JPanel(new GridBagLayout());
        line.setMinimumSize(new Dimension(1, 250));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(0, 0, 0, 3);
        line.add(locationField = new JTextField(), c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 1;
        c.weighty = 1.0;
        line.add(btnLocate = new JButton("..."), c);
        btnLocate.addActionListener(this);
        mainPane.add(line);

        //Third line: gap, back, ok
        line = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        line.add(Box.createHorizontalGlue(), c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.insets = new Insets(0, 0, 0, 3);
        line.add(btnBack = new JButton("Back"), c);
        btnBack.addActionListener(this);

        c = new GridBagConstraints();
        c.gridx = 2;
        line.add(btnOk = new JButton("Ok"), c);
        btnOk.addActionListener(this);
        mainPane.add(line);

        //Fill location field
        try {
            locationField.setText(autoLocateMinecraft());
        } catch(Throwable t) {
            System.err.println("Note: could not locate Minecraft:");
            t.printStackTrace();
        }

        //Display
        frame.setContentPane(mainPane);
        frame.pack();
        parent.setVisible(false);
        frame.setVisible(true);
    }

    private String autoLocateMinecraft() {
        File cDir = (new File(".")).getAbsoluteFile();
        if(cDir.getName().equals("mods")) {
            File pFile = cDir.getParentFile();
            File saves = new File(pFile, "saves");
            File rpacks = new File(pFile, "resourcepacks");

            if(saves.exists() && saves.isDirectory() && rpacks.exists() && rpacks.isDirectory())
                return pFile.getAbsolutePath();
        }

        File root = new File(System.getProperty("user.home", "."));
        String os = System.getProperty("os.name").toLowerCase();

        if(os.contains("win"))
            root = new File(System.getenv("APPDATA"));
        else if(os.contains("mac"))
            root = new File(new File(root, "Library"), "Application Support");

        root = new File(root, ".minecraft");
        return root.exists() ? root.getAbsolutePath() : "";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnLocate) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Where's Minecraft?");
            fc.setCurrentDirectory(new File(locationField.getText()));
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            if(fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
                locationField.setText(fc.getSelectedFile().getAbsolutePath());
        } else if(e.getSource() == btnBack) {
            parent.setVisible(true);
            frame.dispose();
        } else if(e.getSource() == btnOk) {
            File loc = new File(locationField.getText());
            if(!loc.exists() || !loc.isDirectory()) {
                JOptionPane.showMessageDialog(frame, "The selected directory does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            File saves = new File(loc, "saves");
            File rpacks = new File(loc, "resourcepacks");

            if(!saves.exists() || !saves.isDirectory() || !rpacks.exists() || !rpacks.isDirectory()) {
                if(JOptionPane.showConfirmDialog(frame, "The selected directory does not look like a valid Minecraft setup...\nWould you like to continue?", "Hmmm...", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION)
                    return;
            }

            if(action.equals("configure")) {
                File configDir = new File(loc, "config");
                if(!configDir.exists())
                    configDir.mkdirs();

                new ConfigForm(parent, new File(configDir, "MCEF.cfg"));
                frame.dispose();
                return;
            }

            try {
                if(((Boolean) Processes.class.getMethod(action, JFrame.class, File.class).invoke(null, frame, loc)).booleanValue()) {
                    parent.setVisible(true);
                    frame.dispose();
                }
            } catch(Throwable t) {
                System.err.println("Could not execute action \"" + action + "\":");
                t.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Could not execute action \"" + action + "\".\nThis shouldn't happen; please contact mod author.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        parent.setVisible(true);
        frame.dispose();
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

}
