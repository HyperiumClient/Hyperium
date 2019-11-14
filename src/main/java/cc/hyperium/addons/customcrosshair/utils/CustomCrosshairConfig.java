/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.addons.customcrosshair.utils;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomCrosshairConfig {

    private CustomCrosshairAddon crosshairMod;

    private File saveFile = new File(Hyperium.folder, "custom-crosshair-mod_save.txt");

    public CustomCrosshairConfig(CustomCrosshairAddon addon) {
        crosshairMod = addon;
        if (!saveFile.exists()) {
            try {
                saveFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean readSaveFile() {
        try {
            final FileReader fileReader = new FileReader(saveFile);
            final BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (!line.startsWith("//") && line.contains(":")) {

                    final String[] splitted = line.split(":");

                    if (splitted.length <= 1) {
                        continue;
                    }

                    int red = crosshairMod.getCrosshair().getColour().getRed();
                    int green = crosshairMod.getCrosshair().getColour().getGreen();
                    int blue = crosshairMod.getCrosshair().getColour().getBlue();
                    int alpha = crosshairMod.getCrosshair().getColour().getAlpha();

                    int outlineRed = crosshairMod.getCrosshair().getOutlineColour().getRed();
                    int outlineGreen = crosshairMod.getCrosshair().getOutlineColour().getGreen();
                    int outlineBlue = crosshairMod.getCrosshair().getOutlineColour().getBlue();
                    int outlineAlpha = crosshairMod.getCrosshair().getOutlineColour().getAlpha();

                    int dotRed = crosshairMod.getCrosshair().getDotColour().getRed();
                    int dotGreen = crosshairMod.getCrosshair().getDotColour().getGreen();
                    int dotBlue = crosshairMod.getCrosshair().getDotColour().getBlue();
                    int dotAlpha = crosshairMod.getCrosshair().getDotColour().getAlpha();

                    final String attribute = splitted[0].toLowerCase().trim();
                    final String value = splitted[1].toLowerCase().trim();

                    switch (attribute) {
                        case "crosshairtype":
                            crosshairMod.getCrosshair().setCrosshairType(Integer.parseInt(value));
                            break;
                        case "enabled":
                            crosshairMod.getCrosshair().setEnabled(Boolean.parseBoolean(value));
                            break;
                        case "colour_red":
                            crosshairMod.getCrosshair()
                                .setColour(new Color(Integer.parseInt(value), green, blue, alpha));
                            break;
                        case "colour_green":
                            crosshairMod.getCrosshair()
                                .setColour(new Color(red, Integer.parseInt(value), blue, alpha));
                            break;
                        case "colour_blue":
                            crosshairMod.getCrosshair()
                                .setColour(new Color(red, green, Integer.parseInt(value), alpha));
                            break;
                        case "colour_opacity":
                            crosshairMod.getCrosshair()
                                .setColour(new Color(red, green, blue, Integer.parseInt(value)));
                            break;
                        case "visible_default":
                            crosshairMod.getCrosshair()
                                .setVisibleDefault(Boolean.parseBoolean(value));
                            break;
                        case "visible_hiddengui":
                            crosshairMod.getCrosshair()
                                .setVisibleHiddenGui(Boolean.parseBoolean(value));
                            break;
                        case "visible_debug":
                            crosshairMod.getCrosshair()
                                .setVisibleDebug(Boolean.parseBoolean(value));
                            break;
                        case "visible_spectator":
                            crosshairMod.getCrosshair()
                                .setVisibleSpectator(Boolean.parseBoolean(value));
                            break;
                        case "visible_thirdperson":
                            crosshairMod.getCrosshair()
                                .setVisibleThirdPerson(Boolean.parseBoolean(value));
                            break;
                        case "outline":
                            crosshairMod.getCrosshair().setOutline(Boolean.parseBoolean(value));
                            break;
                        case "outlinecolour_red":
                            crosshairMod.getCrosshair().setOutlineColour(
                                new Color(Integer.parseInt(value), outlineGreen, outlineBlue,
                                    outlineAlpha));
                            break;
                        case "outlinecolour_green":
                            crosshairMod.getCrosshair().setOutlineColour(
                                new Color(outlineRed, Integer.parseInt(value), outlineBlue,
                                    outlineAlpha));
                            break;
                        case "outlinecolour_blue":
                            crosshairMod.getCrosshair().setOutlineColour(
                                new Color(outlineRed, outlineGreen, Integer.parseInt(value),
                                    outlineAlpha));
                            break;
                        case "outlinecolour_opacity":
                            crosshairMod.getCrosshair().setOutlineColour(
                                new Color(outlineRed, outlineGreen, outlineBlue,
                                    Integer.parseInt(value)));
                            break;
                        case "dot":
                            crosshairMod.getCrosshair().setDot(Boolean.parseBoolean(value));
                            break;
                        case "dotcolour_red":
                            crosshairMod.getCrosshair().setDotColour(
                                new Color(Integer.parseInt(value), dotGreen, dotBlue, dotAlpha));
                            break;
                        case "dotcolour_green":
                            crosshairMod.getCrosshair().setDotColour(
                                new Color(dotRed, Integer.parseInt(value), dotBlue, dotAlpha));
                            break;
                        case "dotcolour_blue":
                            crosshairMod.getCrosshair().setDotColour(
                                new Color(dotRed, dotGreen, Integer.parseInt(value), dotAlpha));
                            break;
                        case "dotcolour_opacity":
                            crosshairMod.getCrosshair().setDotColour(
                                new Color(dotRed, dotGreen, dotBlue, Integer.parseInt(value)));
                            break;
                        case "width":
                            crosshairMod.getCrosshair().setWidth(Integer.parseInt(value));
                            break;
                        case "height":
                            crosshairMod.getCrosshair().setHeight(Integer.parseInt(value));
                            break;
                        case "gap":
                            crosshairMod.getCrosshair().setGap(Integer.parseInt(value));
                            break;
                        case "thickness":
                            crosshairMod.getCrosshair().setThickness(Integer.parseInt(value));
                            break;
                        case "rainbow":
                            crosshairMod.getCrosshair().setRainbowCrosshair(Boolean.valueOf(value));
                            break;
                        case "rainbow_speed":
                            crosshairMod.getCrosshair().setRainbowSpeed(Integer.valueOf(value));
                            break;
                        default:
                            if (!attribute.equals("dynamic_bow")) {
                                continue;
                            }
                            crosshairMod.getCrosshair().setDynamicBow(Boolean.parseBoolean(value));
                            break;
                    }
                }
            }

            fileReader.close();
            bufferedReader.close();
            return true;
        } catch (Exception readingException) {
            readingException.printStackTrace();
            return false;
        }
    }

    public boolean writeSaveFile(final int crosshairType, final boolean enabled, final int colour_red, final int colour_green, final int colour_blue, final int colour_opacity, final boolean visibleDefault, final boolean visibleHiddenGui, final boolean visibleDebug, final boolean visibleSpectator, final boolean visibleThirdPerson, final boolean outline, final int outlineColour_red, final int outlineColour_green, final int outlineColour_blue, final int outlineColour_opacity, final boolean dot, final int dotColour_red, final int dotColour_green, final int dotColour_blue, final int dotColour_opacity, final int width, final int height, final int gap, final int thickness, final boolean dynamicBow, final boolean rainbow, final int rainbowspeed) {
        try {
            final FileWriter fileWriter = new FileWriter(saveFile);
            final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            final List<String> lines = new ArrayList<>();
            lines.add("// Custom Crosshair Mod Save File - Made by Sparkless101");
            lines.add("// ---------------------------------------------------------------------");
            lines.add("// This file contains the styling for the crosshair.");
            lines.add("// You may change the contents of this file in order to change the style of the crosshair in-game.");
            lines.add("// Colours must be an number between 0 and 255.");
            lines.add("// Boolean values must be 'true' or 'false'.");
            lines.add("// ---------------------------------------------------------------------");
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
            lines.add("rainbow:" + rainbow);
            lines.add("rainbow_speed:" + rainbowspeed);
            for (String line : lines) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
            return true;
        } catch (Exception exceptionWriting) {
            exceptionWriting.printStackTrace();
            return false;
        }
    }

    public void writeSaveFileDefault() {
        writeSaveFile(0, true, 255, 255, 255, 255, true, true, true, true, true, true, 0, 0, 0, 255, true, 255, 255, 255, 255, 5, 5, 3, 1, true, false, 500);
    }

    public void saveCurrentCrosshair() {
        writeSaveFile(crosshairMod.getCrosshair().getCrosshairTypeID(), crosshairMod.getCrosshair().getEnabled(), crosshairMod.getCrosshair().getColour().getRed(), crosshairMod.getCrosshair().getColour().getGreen(), crosshairMod.getCrosshair().getColour().getBlue(), crosshairMod.getCrosshair().getColour().getAlpha(), crosshairMod.getCrosshair().getVisibleDefault(), crosshairMod.getCrosshair().getVisibleHiddenGui(), crosshairMod.getCrosshair().getVisibleDebug(), crosshairMod.getCrosshair().getVisibleSpectator(), crosshairMod.getCrosshair().getVisibleThirdPerson(), crosshairMod.getCrosshair().getOutline(), crosshairMod.getCrosshair().getOutlineColour().getRed(), crosshairMod.getCrosshair().getOutlineColour().getGreen(), crosshairMod.getCrosshair().getOutlineColour().getBlue(), crosshairMod.getCrosshair().getOutlineColour().getAlpha(), crosshairMod.getCrosshair().getDot(), crosshairMod.getCrosshair().getDotColour().getRed(), crosshairMod.getCrosshair().getDotColour().getGreen(), crosshairMod.getCrosshair().getDotColour().getBlue(), crosshairMod.getCrosshair().getDotColour().getAlpha(), crosshairMod.getCrosshair().getWidth(), crosshairMod.getCrosshair().getHeight(), crosshairMod.getCrosshair().getGap(), crosshairMod.getCrosshair().getThickness(), crosshairMod.getCrosshair().getDynamicBow(), crosshairMod.getCrosshair().getRainbowCrosshair(), crosshairMod.getCrosshair().getRainbowSpeed());
    }
}
