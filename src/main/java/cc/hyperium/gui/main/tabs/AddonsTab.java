package cc.hyperium.gui.main.tabs;

import cc.hyperium.Hyperium;
import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.main.HyperiumMainGui;
import cc.hyperium.gui.main.HyperiumOverlay;
import cc.hyperium.gui.main.components.AbstractTab;
import cc.hyperium.gui.main.components.SettingItem;
import cc.hyperium.internal.addons.AddonBootstrap;
import cc.hyperium.internal.addons.AddonManifest;
import cc.hyperium.internal.addons.AddonMinecraftBootstrap;
import cc.hyperium.utils.HyperiumFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Created by Cubxity on 29/05/2018
 */
public class AddonsTab extends AbstractTab {
    private static int offsetY = 0; // static so it saves the previous location
    private GuiBlock block;
    private int y, w;
    private static HyperiumFontRenderer hfr = new HyperiumFontRenderer("Arial", Font.PLAIN, 30);
    public String selectedMsg;
    public ArrayList<String> messages;
    private static HyperiumOverlay downloadAddons = new HyperiumOverlay();

    public AddonsTab(int y, int w) {
        messages = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("noaddonmsgs.txt")).getInputStream(), Charsets.UTF_8));

            String currentMsg;
            while((currentMsg = reader.readLine()) != null) {
                currentMsg = currentMsg.trim();
                if (!currentMsg.isEmpty()) {
                    messages.add(currentMsg);
                }
            }
            if (Hyperium.INSTANCE.isDevEnv() && !AddonMinecraftBootstrap.getAddonLoadErrors().isEmpty()) {
                messages.clear();
                messages.add("Uh oh, looks like your addon didn't load correctly!");
            }
        } catch (Throwable e) {
            messages.add(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        selectedMsg = messages.get(ThreadLocalRandom.current().nextInt(messages.size()));
        block = new GuiBlock(0, w, y, y + w);
        this.y = y;
        this.w = w;
        int yi = 0, xi = 0;
        AddonManifest addonManifest = new AddonManifest();
        if(addonManifest.getDesc() != null) {
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
                }, Icons.EXTENSION.getResource(), a.getName(), a.getDesc(), "Configure addon", xi, yi));
                if (xi == 2) {
                    xi = 0;
                    yi++;
                } else
                    xi++;
            }
        } else {
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
                }, Icons.EXTENSION.getResource(), a.getName(), "Addon Description", "Configure addon", xi, yi));
                if (xi == 3) {
                    xi = 0;
                    yi++;
                } else
                    xi++;
            }
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
    public void drawHighlight(float s) {
        Gui.drawRect(0, (int) (y + s * (s * w / 2)), 3, (int) (y + w - s * (w / 2)), Color.WHITE.getRGB());
    }

    @Override
    public void draw(int mouseX, int mouseY, int topX, int topY, int containerWidth, int containerHeight) {

        Logger.getLogger("debug").log(Level.FINE, selectedMsg);

        super.draw(mouseX, mouseY, topX, topY, containerWidth, containerHeight);

        //if addon folder is empty display chosen text
        if (AddonBootstrap.INSTANCE.getAddonManifests().isEmpty()) {
            GlStateManager.scale(1.0, 1.0, 1.0);
            hfr.drawStringWithShadow(selectedMsg, topX, topY, new Color(255, 0, 0, 100).getRGB());

            if (Hyperium.INSTANCE.isDevEnv() && !AddonMinecraftBootstrap.getAddonLoadErrors().isEmpty()) {
                String stacktrace = ExceptionUtils.getStackTrace(AddonMinecraftBootstrap.getAddonLoadErrors().get(0)); // get stacktrace
                String[] parts = stacktrace.split("\n");
                int index = 0;
                for (String part : parts) {
                    hfr.drawStringWithShadow(part, topX + 5, (topY + 5) + (++index * (hfr.FONT_HEIGHT + 10)), new Color(255, 0, 0, 100).getRGB());
                }
            }
        } else {

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
