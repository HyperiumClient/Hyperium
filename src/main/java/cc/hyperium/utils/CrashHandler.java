package cc.hyperium.utils;

import cc.hyperium.installer.components.HScrollBarUI;
import cc.hyperium.installer.components.VScrollBarUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/*
 * Created by Cubxity on 19/07/2018
 */
@SuppressWarnings("unused")
public class CrashHandler {
    // DON'T TOUCH THIS
    public static void handle(Exception ex) {
        StringBuilder err = new StringBuilder();
        PrintStream ps = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                err.append((char) b);
            }
        });
        ex.printStackTrace(ps);

        JDialog dialog = new JDialog();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = dim.width / 2;
        int h = dim.height / 3;
        dialog.setSize(w, h);
        dialog.setLocation(dim.width / 2 - w / 2, dim.height / 2 - h / 2);
        dialog.setAlwaysOnTop(true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        try {
            dialog.setIconImage(ImageIO.read(CrashHandler.class.getResourceAsStream("/assets/hyperium/icons/icon-32x.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Font f;
        try {
            f = Font.createFont(Font.TRUETYPE_FONT, CrashHandler.class.getResourceAsStream("/fonts/segoeuil.ttf")).deriveFont(14f);
        } catch (FontFormatException | IOException e) {
            f = new Font("Arial", Font.PLAIN, 14);
        }
        dialog.setVisible(true);

        Container c = dialog.getContentPane();
        c.setBackground(Colors.DARK);
        c.setLayout(null); // the superior

        JLabel title = new JLabel("Ahh! Unable to launch :(", JLabel.CENTER);
        title.setFont(f);
        title.setForeground(Color.WHITE);
        title.setBounds(0, 5, c.getWidth(), 20);
        c.add(title);

        JTextArea pane = new JTextArea();
        pane.setText(err.toString());
        pane.setEditable(false);
        pane.setFont(f);
        pane.setForeground(Color.WHITE);
        pane.setBackground(Colors.DARK.brighter());

        JScrollPane sp = new JScrollPane(pane, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        sp.setBackground(Colors.DARK.brighter());
        c.add(sp);
        sp.setBounds(c.getWidth() / 6, 40, c.getWidth() / 3 * 2, c.getHeight() - 80);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getVerticalScrollBar().setUI(new VScrollBarUI());
        sp.getVerticalScrollBar().setBackground(Colors.DARK.brighter());
        Rectangle b = sp.getVerticalScrollBar().getBounds();
        sp.getVerticalScrollBar().setBounds(b.x + (b.width - 5), b.y, 5, b.height);
        sp.getHorizontalScrollBar().setUI(new HScrollBarUI());
        sp.getHorizontalScrollBar().setBackground(Colors.DARK.brighter());
        b = sp.getHorizontalScrollBar().getBounds();
        sp.getHorizontalScrollBar().setBounds(b.x, b.y + (b.height - 5), b.width, 5);
        UIManager.put("ScrollBar.width", 5);

        JButton report = new JButton("REPORT BUG TO HYPERIUM DEV TEAM");
        report.setBackground(Color.WHITE);
        report.setForeground(Colors.DARK);
        report.setFocusPainted(false);
        report.setBorderPainted(false);
        report.setBounds(c.getWidth() / 2 - 150, c.getHeight() - 25, 300, 20);
        report.setFont(f);
        report.addActionListener(e -> {
            //TODO: Report
        });
        //c.add(report);
        //TODO: Uncomment this when backend is done

        dialog.repaint();
    }

    public static void main(String[] args) {
        handle(new Exception("ESSKEETIT")); // TEST
    }
}
