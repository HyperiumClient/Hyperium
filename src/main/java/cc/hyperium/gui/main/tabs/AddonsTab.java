package cc.hyperium.gui.main.tabs;

import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.main.HyperiumMainGui;
import cc.hyperium.gui.main.HyperiumOverlay;
import cc.hyperium.gui.main.components.AbstractTab;
import cc.hyperium.gui.main.components.SettingItem;
import cc.hyperium.internal.addons.AddonBootstrap;
import cc.hyperium.internal.addons.AddonManifest;
import cc.hyperium.utils.HyperiumFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

/*
 * Created by Cubxity on 29/05/2018
 */
public class AddonsTab extends AbstractTab {
    private static int offsetY = 0; // static so it saves the previous location
    private GuiBlock block;
    private int y, w;
    private static HyperiumFontRenderer hfr = new HyperiumFontRenderer("Arial", Font.PLAIN, 40);
    public String selectedMsg;
    public ArrayList<String> messages;

    public AddonsTab(int y, int w) {
        block = new GuiBlock(0, w, y, y + w);
        this.y = y;
        this.w = w;
        int yi = 0, xi = 0;
        for (AddonManifest a : AddonBootstrap.INSTANCE.getAddonManifests()) {
            items.add(new SettingItem(() -> {
                if (a.getOverlay() != null) {
                    // While loading it has been checked so we don't have to do that here
                    try {
                        Class<?> clazz = Class.forName(a.getOverlay());
                        HyperiumOverlay overlay = (HyperiumOverlay) clazz.newInstance();
                        HyperiumMainGui.INSTANCE.setOverlay(overlay);
                    } catch (Exception e) {
                        HyperiumMainGui.Alert alert = new HyperiumMainGui.Alert(Icons.ERROR.getResource(), () -> {
                        }, "Failed to load addon's config overlay");
                        HyperiumMainGui.getAlerts().add(alert);
                        e.printStackTrace(); // in case the check went wrong
                    }
                }
            }, Icons.EXTENSION.getResource(), a.getName(), "", "Configure addon", xi, yi));
            if (xi == 3) {
                xi = 0;
                yi++;
            } else
                xi++;
        }
    }

    @Override
    public void drawTabIcon() {
        Icons.EXTENSION.bind();
        Gui.drawScaledCustomSizeModalRect(5, y + 5, 0, 0, 144, 144, w - 10, w - 10, 144, 144);
    }

    @Override
    public GuiBlock getBlock() {
        return block;
    }

    @Override
    public void drawHighlight() {
        Gui.drawRect(0, y, 3, y + w, Color.WHITE.getRGB());
    }

    @Override
    public void draw(int mouseX, int mouseY, int topX, int topY, int containerWidth, int containerHeight) {
        //choose random line from text

        messages.add("Wow... so sad... you don't have any addons\n" +
                ":( so 2009! You don't have any addons\n" +
                "y u have no addon\n" +
                "add addons pls kthx\n" +
                "java.lang.ArrayIndexOutOfBoundsException");

        Random random = new Random();

        IntStream.range(0, 5).forEach(
                a -> selectedMsg = messages.get(random
                        .nextInt(messages.size())));

        Logger.getLogger("debug").log(Level.FINE, selectedMsg);

        super.draw(mouseX, mouseY, topX, topY, containerWidth, containerHeight);

        //if addon folder is empty display chosen text
        if (AddonBootstrap.INSTANCE.getAddonManifests().isEmpty()) {
            hfr.drawStringWithShadow(selectedMsg, topX + 5, topY + 5, new Color(255, 0, 0, 100).getRGB());
        } else {
            //do shit
        }
        {

        }
    }

    public static String choose(File f) throws FileNotFoundException {
        String result = null;
        Random rand = new Random();
        int n = 0;
        for (Scanner sc = new Scanner(f); sc.hasNext(); ) {
            ++n;
            String line = sc.nextLine();
            if (rand.nextInt(n) == 0)
                result = line;
        }

        return result;
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        if (HyperiumMainGui.INSTANCE.getOverlay() != null) return;
        int i = Mouse.getEventDWheel();
        if (i < 0)
            offsetY -= 5;
        else if (i > 0)
            offsetY += 5;
    }
}
