package cc.hyperium.installer.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

/*
 * Created by Cubxity on 19/04/2018
 */
public class MaterialRadioButton extends JRadioButton {

    private static final BufferedImage unselectedButtonImage;
    private static final BufferedImage selectedButtonImage;
    private static final BufferedImage disabledButtonImage;

    static {
        unselectedButtonImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = unselectedButtonImage.createGraphics();
        graphics.setPaint(Color.WHITE);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.draw(new Ellipse2D.Double(0, 0, 9, 9));

        selectedButtonImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        graphics = selectedButtonImage.createGraphics();
        graphics.setPaint(Color.WHITE);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.draw(new Ellipse2D.Double(0, 0, 9, 9));
        graphics.fill(new Ellipse2D.Double(2, 2, 6, 6));

        disabledButtonImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        graphics = disabledButtonImage.createGraphics();
        graphics.setPaint(Color.GRAY);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.draw(new Ellipse2D.Double(0, 0, 9, 9));
    }

    public MaterialRadioButton(String label) {
        super(label);
        setForeground(Color.WHITE);
        setBackground(new Color(30, 30, 30));
        setIcon(new ImageIcon(unselectedButtonImage));
        setSelectedIcon(new ImageIcon(selectedButtonImage));
        setDisabledIcon(new ImageIcon(disabledButtonImage));
        setFocusPainted(false);
    }
}
