package net.montoyo.mcef.setup;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

public class ConfigForm implements ActionListener, WindowListener {

    private CfgParser config;
    private JFrame parent;
    private JFrame frame;
    private JPanel contentPane;

    //Main
    private JPanel cMainPane;
    private JTextField cMainForcedMirror;
    private JCheckBox cMainSkipUpdates;
    private JCheckBox cMainForgeSplash;
    private JCheckBox cMainWarnUpdates;

    //Browser
    private JPanel cBrowserPane;
    private JCheckBox cBrowserEnable;
    private JTextField cBrowserHome;

    //Buttons
    private JPanel btnPane;
    private JButton btnOk;
    private JButton btnBack;
    private JButton btnApply;

    public ConfigForm(JFrame p, File cfgFile) {
        GridBagConstraints c;
        config = new CfgParser(cfgFile);
        parent = p;

        config.load();

        //Frame and content pane
        frame = new JFrame("MCEF Setup - Configuration");
        frame.setMinimumSize(new Dimension(500, 1));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(this);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(3, 3, 3, 3));
        contentPane.setLayout(new GridBagLayout());

        //Main
        cMainPane = new JPanel();
        cMainPane.setBorder(new TitledBorder("Main"));
        cMainPane.setLayout(new GridBagLayout());

        cMainForcedMirror = new JTextField();
        cMainSkipUpdates = new JCheckBox();
        cMainForgeSplash = new JCheckBox();
        cMainWarnUpdates = new JCheckBox();
        addFormComponent(cMainPane, 0, "Forced mirror", cMainForcedMirror);
        addFormComponent(cMainPane, 1, "Skip updates", cMainSkipUpdates);
        addFormComponent(cMainPane, 2, "Use forge splash", cMainForgeSplash);
        addFormComponent(cMainPane, 3, "Warn updates", cMainWarnUpdates);

        cMainForcedMirror.setText(config.getStringValue("main", "forcedMirror", ""));
        cMainSkipUpdates.setSelected(config.getBooleanValue("main", "skipUpdates", false));
        cMainForgeSplash.setSelected(config.getBooleanValue("main", "useForgeSplash", true));
        cMainWarnUpdates.setSelected(config.getBooleanValue("main", "warnUpdates", true));

        c = new GridBagConstraints();
        c.gridy = 4;
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1.0;
        cMainPane.add(Box.createVerticalGlue(), c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 0.5;
        contentPane.add(cMainPane, c);

        //Browser
        cBrowserPane = new JPanel();
        cBrowserPane.setBorder(new TitledBorder("Browser"));
        cBrowserPane.setLayout(new GridBagLayout());

        cBrowserEnable = new JCheckBox();
        cBrowserHome = new JTextField();
        addFormComponent(cBrowserPane, 0, "Enable", cBrowserEnable);
        addFormComponent(cBrowserPane, 1, "Home page", cBrowserHome);

        cBrowserEnable.setSelected(config.getBooleanValue("examplebrowser", "enable", true));
        cBrowserHome.setText(config.getStringValue("examplebrowser", "home", "mod://mcef/home.html"));

        c = new GridBagConstraints();
        c.gridy = 2;
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1.0;
        cBrowserPane.add(Box.createVerticalGlue(), c);

        c = new GridBagConstraints();
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 0.5;
        contentPane.add(cBrowserPane, c);

        //Buttons
        btnPane = new JPanel();
        btnPane.setLayout(new GridBagLayout());

        btnOk = new JButton("Ok");
        btnBack = new JButton("Back");
        btnApply = new JButton("Apply");
        btnOk.addActionListener(this);
        btnBack.addActionListener(this);
        btnApply.addActionListener(this);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        btnPane.add(Box.createHorizontalGlue(), c);

        addFormButton(1, btnOk);
        addFormButton(2, btnBack);
        addFormButton(3, btnApply);

        c = new GridBagConstraints();
        c.gridy = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        contentPane.add(btnPane, c);

        //Display
        frame.setContentPane(contentPane);
        frame.pack();
        parent.setVisible(false);
        frame.setVisible(true);
    }

    private void addFormComponent(JPanel pane, int line, String label, Component comp) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 3, 3, 3);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = line;
        pane.add(new JLabel(label), c);

        c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 3, 3);
        c.gridx = 1;
        c.gridy = line;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        pane.add(comp, c);
    }

    private void addFormButton(int x, JButton btn) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 0, 3, 3);
        c.gridx = x;
        btnPane.add(btn, c);
    }

    private void saveChanges() {
        config.setStringValue("main", "forcedMirror", cMainForcedMirror.getText());
        config.setBooleanValue("main", "skipUpdates", cMainSkipUpdates.isSelected());
        config.setBooleanValue("main", "useForgeSplash", cMainForgeSplash.isSelected());
        config.setBooleanValue("main", "warnUpdates", cMainWarnUpdates.isSelected());
        config.setBooleanValue("examplebrowser", "enable", cBrowserEnable.isSelected());
        config.setStringValue("examplebrowser", "home", cBrowserHome.getText());

        if(!config.save())
            JOptionPane.showMessageDialog(frame, "Could not save configuration file.\nMake sure you have the permissions to write in the config folder.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnBack)
            windowClosing(null);
        else if(e.getSource() == btnApply)
            saveChanges();
        else if(e.getSource() == btnOk) {
            saveChanges();
            windowClosing(null);
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
