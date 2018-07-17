package cc.hyperium.addons.customcrosshair.utils;

import cc.hyperium.addons.customcrosshair.main.CustomCrosshairAddon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class SaveUtils {
    private static final String SAVE_DIRECTORY = "custom-crosshair-mod_save.txt";

    public static boolean readSaveFile(final CustomCrosshairAddon crosshairMod) {
        try {
            final FileReader fileReader = new FileReader("custom-crosshair-mod_save.txt");
            final BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.startsWith("//")) {
                    final String[] splitted = line.split(":");
                    if (splitted.length <= 1) {
                        continue;
                    }
                    final String attribute = splitted[0].toLowerCase().trim();
                    final String value = splitted[1].toLowerCase().trim();
                    if (attribute.equals("guikeybind")) {
                        crosshairMod.setGuiKeyBind(value.toUpperCase());
                    }
                    else if (attribute.equals("crosshairtype")) {
                        crosshairMod.getCrosshair().setCrosshairType(Integer.parseInt(value));
                    }
                    else if (attribute.equals("enabled")) {
                        crosshairMod.getCrosshair().setEnabled(Boolean.parseBoolean(value));
                    }
                    else if (attribute.equals("colour_red")) {
                        crosshairMod.getCrosshair().getColour().setRed(Integer.parseInt(value));
                    }
                    else if (attribute.equals("colour_green")) {
                        crosshairMod.getCrosshair().getColour().setGreen(Integer.parseInt(value));
                    }
                    else if (attribute.equals("colour_blue")) {
                        crosshairMod.getCrosshair().getColour().setBlue(Integer.parseInt(value));
                    }
                    else if (attribute.equals("colour_opacity")) {
                        crosshairMod.getCrosshair().getColour().setOpacity(Integer.parseInt(value));
                    }
                    else if (attribute.equals("visible_default")) {
                        crosshairMod.getCrosshair().setVisibleDefault(Boolean.parseBoolean(value));
                    }
                    else if (attribute.equals("visible_hiddengui")) {
                        crosshairMod.getCrosshair().setVisibleHiddenGui(Boolean.parseBoolean(value));
                    }
                    else if (attribute.equals("visible_debug")) {
                        crosshairMod.getCrosshair().setVisibleDebug(Boolean.parseBoolean(value));
                    }
                    else if (attribute.equals("visible_spectator")) {
                        crosshairMod.getCrosshair().setVisibleSpectator(Boolean.parseBoolean(value));
                    }
                    else if (attribute.equals("visible_thirdperson")) {
                        crosshairMod.getCrosshair().setVisibleThirdPerson(Boolean.parseBoolean(value));
                    }
                    else if (attribute.equals("outline")) {
                        crosshairMod.getCrosshair().setOutline(Boolean.parseBoolean(value));
                    }
                    else if (attribute.equals("outlinecolour_red")) {
                        crosshairMod.getCrosshair().getOutlineColour().setRed(Integer.parseInt(value));
                    }
                    else if (attribute.equals("outlinecolour_green")) {
                        crosshairMod.getCrosshair().getOutlineColour().setGreen(Integer.parseInt(value));
                    }
                    else if (attribute.equals("outlinecolour_blue")) {
                        crosshairMod.getCrosshair().getOutlineColour().setBlue(Integer.parseInt(value));
                    }
                    else if (attribute.equals("outlinecolour_opacity")) {
                        crosshairMod.getCrosshair().getOutlineColour().setOpacity(Integer.parseInt(value));
                    }
                    else if (attribute.equals("dot")) {
                        crosshairMod.getCrosshair().setDot(Boolean.parseBoolean(value));
                    }
                    else if (attribute.equals("dotcolour_red")) {
                        crosshairMod.getCrosshair().getDotColour().setRed(Integer.parseInt(value));
                    }
                    else if (attribute.equals("dotcolour_green")) {
                        crosshairMod.getCrosshair().getDotColour().setGreen(Integer.parseInt(value));
                    }
                    else if (attribute.equals("dotcolour_blue")) {
                        crosshairMod.getCrosshair().getDotColour().setBlue(Integer.parseInt(value));
                    }
                    else if (attribute.equals("dotcolour_opacity")) {
                        crosshairMod.getCrosshair().getDotColour().setOpacity(Integer.parseInt(value));
                    }
                    else if (attribute.equals("width")) {
                        crosshairMod.getCrosshair().setWidth(Integer.parseInt(value));
                    }
                    else if (attribute.equals("height")) {
                        crosshairMod.getCrosshair().setHeight(Integer.parseInt(value));
                    }
                    else if (attribute.equals("gap")) {
                        crosshairMod.getCrosshair().setGap(Integer.parseInt(value));
                    }
                    else if (attribute.equals("thickness")) {
                        crosshairMod.getCrosshair().setThickness(Integer.parseInt(value));
                    }
                    else {
                        if (!attribute.equals("dynamic_bow")) {
                            continue;
                        }
                        crosshairMod.getCrosshair().setDynamicBow(Boolean.parseBoolean(value));
                    }
                }
            }
            bufferedReader.close();
            System.out.println("No");
            CustomCrosshairAddon.showCredits = false;
            return true;
        }
        catch (Exception readingException) {
            System.out.println("Yes");
            return false;
        }
    }

    public static boolean writeSaveFile(final String guiKeyBind, final int crosshairType, final boolean enabled, final int colour_red, final int colour_green, final int colour_blue, final int colour_opacity, final boolean visibleDefault, final boolean visibleHiddenGui, final boolean visibleDebug, final boolean visibleSpectator, final boolean visibleThirdPerson, final boolean outline, final int outlineColour_red, final int outlineColour_green, final int outlineColour_blue, final int outlineColour_opacity, final boolean dot, final int dotColour_red, final int dotColour_green, final int dotColour_blue, final int dotColour_opacity, final int width, final int height, final int gap, final int thickness, final boolean dynamicBow) {
        try {
            final FileWriter fileWriter = new FileWriter("custom-crosshair-mod_save.txt");
            final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            final List<String> lines = new ArrayList<String>();
            lines.add("// Custom Crosshair Mod Save File - Made by Sparkless101");
            lines.add("// ---------------------------------------------------------------------");
            lines.add("// This file contains the styling for the crosshair.");
            lines.add("// You may change the contents of this file in order to change the style of the crosshair in-game.");
            lines.add("// Colours must be an number between 0 and 255.");
            lines.add("// Boolean values must be 'true' or 'false'.");
            lines.add("// ---------------------------------------------------------------------");
            lines.add("guiKeyBind:" + guiKeyBind);
            lines.add("crosshairType:" + crosshairType);
            lines.add("enabled:" + enabled);
            lines.add("colour_red:" + colour_red);
            lines.add("colour_green:" + colour_green);
            lines.add("colour_blue:" + colour_blue);
            lines.add("colour_opacity:" + colour_opacity);
            lines.add("visible_default:" + visibleDefault);
            lines.add("visible_hiddenGui:" + visibleHiddenGui);
            lines.add("visible_debug:" + visibleDebug);
            lines.add("visible_spectator:" + visibleSpectator);
            lines.add("visible_thirdPerson:" + visibleThirdPerson);
            lines.add("outline:" + outline);
            lines.add("outlineColour_red:" + outlineColour_red);
            lines.add("outlineColour_green:" + outlineColour_green);
            lines.add("outlineColour_blue:" + outlineColour_blue);
            lines.add("outlineColour_opacity:" + outlineColour_opacity);
            lines.add("dot:" + dot);
            lines.add("dotColour_red:" + dotColour_red);
            lines.add("dotColour_green:" + dotColour_green);
            lines.add("dotColour_blue:" + dotColour_blue);
            lines.add("dotColour_opacity:" + dotColour_opacity);
            lines.add("width:" + width);
            lines.add("height:" + height);
            lines.add("gap:" + gap);
            lines.add("thickness:" + thickness);
            lines.add("dynamic_bow:" + dynamicBow);
            for (int i = 0; i < lines.size(); ++i) {
                bufferedWriter.write(lines.get(i));
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
            return true;
        }
        catch (Exception exceptionWriting) {
            return false;
        }
    }

    public static boolean writeSaveFileDefault() {
        return writeSaveFile("G", 0, true, 255, 255, 255, 255, true, true, true, true, true, true, 0, 0, 0, 255, true, 255, 255, 255, 255, 5, 5, 3, 1, true);
    }

    public static void saveCurrentCrosshair(final CustomCrosshairAddon crosshairMod) {
        writeSaveFile(CustomCrosshairAddon.getCrosshairMod().getGuiKeyBind(), crosshairMod.getCrosshair().getCrosshairTypeID(), crosshairMod.getCrosshair().getEnabled(), crosshairMod.getCrosshair().getColour().getRed(), crosshairMod.getCrosshair().getColour().getGreen(), crosshairMod.getCrosshair().getColour().getBlue(), crosshairMod.getCrosshair().getColour().getOpacity(), crosshairMod.getCrosshair().getVisibleDefault(), crosshairMod.getCrosshair().getVisibleHiddenGui(), crosshairMod.getCrosshair().getVisibleDebug(), crosshairMod.getCrosshair().getVisibleSpectator(), crosshairMod.getCrosshair().getVisibleThirdPerson(), crosshairMod.getCrosshair().getOutline(), crosshairMod.getCrosshair().getOutlineColour().getRed(), crosshairMod.getCrosshair().getOutlineColour().getGreen(), crosshairMod.getCrosshair().getOutlineColour().getBlue(), crosshairMod.getCrosshair().getOutlineColour().getOpacity(), crosshairMod.getCrosshair().getDot(), crosshairMod.getCrosshair().getDotColour().getRed(), crosshairMod.getCrosshair().getDotColour().getGreen(), crosshairMod.getCrosshair().getDotColour().getBlue(), crosshairMod.getCrosshair().getDotColour().getOpacity(), crosshairMod.getCrosshair().getWidth(), crosshairMod.getCrosshair().getHeight(), crosshairMod.getCrosshair().getGap(), crosshairMod.getCrosshair().getThickness(), crosshairMod.getCrosshair().getDynamicBow());
    }
}
