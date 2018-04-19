package cc.hyperium.installer.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

/*
 * Created by Cubxity on 19/04/2018
 */
public class MaterialRadioButton extends JRadioButton {
    private static BufferedImage b;
    private static BufferedImage sb;
    static{
         b = new BufferedImage(
                10, 10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D bg = b.createGraphics();
        bg.setPaint(Color.WHITE);
        bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        bg.draw(new Ellipse2D.Double(0, 0, 9, 9));
        sb = new BufferedImage(
                10, 10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D sbg = sb.createGraphics();
        sbg.setPaint(Color.WHITE);
        sbg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        sbg.draw(new Ellipse2D.Double(0, 0, 9, 9));
        sbg.fill(new Ellipse2D.Double(2, 2, 6, 6));
    }
    public MaterialRadioButton(String label) {
        super(label);
        setForeground(Color.WHITE);
        setBackground(new Color(30, 30, 30));
        setIcon(new ImageIcon(b));
        setSelectedIcon(new ImageIcon(sb));
        setFocusPainted(false);
    }
}
