/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
        this.crosshairMod = addon;
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
            final FileReader fileReader = new FileReader(this.saveFile);
            final BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (!line.startsWith("//") && line.contains(":")) {

                    final String[] splitted = line.split(":");

                    if (splitted.length <= 1) {
                        continue;
                    }

                    int red = this.crosshairMod.getCrosshair().getColour().getRed();
                    int green = this.crosshairMod.getCrosshair().getColour().getGreen();
                    int blue = this.crosshairMod.getCrosshair().getColour().getBlue();
                    int alpha = this.crosshairMod.getCrosshair().getColour().getAlpha();

                    int outlineRed = this.crosshairMod.getCrosshair().getOutlineColour().getRed();
                    int outlineGreen = this.crosshairMod.getCrosshair().getOutlineColour().getGreen();
                    int outlineBlue = this.crosshairMod.getCrosshair().getOutlineColour().getBlue();
                    int outlineAlpha = this.crosshairMod.getCrosshair().getOutlineColour().getAlpha();

                    int dotRed = this.crosshairMod.getCrosshair().getDotColour().getRed();
                    int dotGreen = this.crosshairMod.getCrosshair().getDotColour().getGreen();
                    int dotBlue = this.crosshairMod.getCrosshair().getDotColour().getBlue();
                    int dotAlpha = this.crosshairMod.getCrosshair().getDotColour().getAlpha();

                    final String attribute = splitted[0].toLowerCase().trim();
                    final String value = splitted[1].toLowerCase().trim();

                    switch (attribute) {
                        case "crosshairtype":
                            this.crosshairMod.getCrosshair().setCrosshairType(Integer.parseInt(value));
                            break;
                        case "enabled":
                            this.crosshairMod.getCrosshair().setEnabled(Boolean.parseBoolean(value));
                            break;
                        case "colour_red":
                            this.crosshairMod.getCrosshair()
                                .setColour(new Color(Integer.parseInt(value), green, blue, alpha));
                            break;
                        case "colour_green":
                            this.crosshairMod.getCrosshair()
                                .setColour(new Color(red, Integer.parseInt(value), blue, alpha));
                            break;
                        case "colour_blue":
                            this.crosshairMod.getCrosshair()
                                .setColour(new Color(red, green, Integer.parseInt(value), alpha));
                            break;
                        case "colour_opacity":
                            this.crosshairMod.getCrosshair()
                                .setColour(new Color(red, green, blue, Integer.parseInt(value)));
                            break;
                        case "visible_default":
                            this.crosshairMod.getCrosshair()
                                .setVisibleDefault(Boolean.parseBoolean(value));
                            break;
                        case "visible_hiddengui":
                            this.crosshairMod.getCrosshair()
                                .setVisibleHiddenGui(Boolean.parseBoolean(value));
                            break;
                        case "visible_debug":
                            this.crosshairMod.getCrosshair()
                                .setVisibleDebug(Boolean.parseBoolean(value));
                            break;
                        case "visible_spectator":
                            this.crosshairMod.getCrosshair()
                                .setVisibleSpectator(Boolean.parseBoolean(value));
                            break;
                        case "visible_thirdperson":
                            this.crosshairMod.getCrosshair()
                                .setVisibleThirdPerson(Boolean.parseBoolean(value));
                            break;
                        case "outline":
                            this.crosshairMod.getCrosshair().setOutline(Boolean.parseBoolean(value));
                            break;
                        case "outlinecolour_red":
                            this.crosshairMod.getCrosshair().setOutlineColour(
                                new Color(Integer.parseInt(value), outlineGreen, outlineBlue,
                                    outlineAlpha));
                            break;
                        case "outlinecolour_green":
                            this.crosshairMod.getCrosshair().setOutlineColour(
                                new Color(outlineRed, Integer.parseInt(value), outlineBlue,
                                    outlineAlpha));
                            break;
                        case "outlinecolour_blue":
                            this.crosshairMod.getCrosshair().setOutlineColour(
                                new Color(outlineRed, outlineGreen, Integer.parseInt(value),
                                    outlineAlpha));
                            break;
                        case "outlinecolour_opacity":
                            this.crosshairMod.getCrosshair().setOutlineColour(
                                new Color(outlineRed, outlineGreen, outlineBlue,
                                    Integer.parseInt(value)));
                            break;
                        case "dot":
                            this.crosshairMod.getCrosshair().setDot(Boolean.parseBoolean(value));
                            break;
                        case "dotcolour_red":
                            this.crosshairMod.getCrosshair().setDotColour(
                                new Color(Integer.parseInt(value), dotGreen, dotBlue, dotAlpha));
                            break;
                        case "dotcolour_green":
                            this.crosshairMod.getCrosshair().setDotColour(
                                new Color(dotRed, Integer.parseInt(value), dotBlue, dotAlpha));
                            break;
                        case "dotcolour_blue":
                            this.crosshairMod.getCrosshair().setDotColour(
                                new Color(dotRed, dotGreen, Integer.parseInt(value), dotAlpha));
                            break;
                        case "dotcolour_opacity":
                            this.crosshairMod.getCrosshair().setDotColour(
                                new Color(dotRed, dotGreen, dotBlue, Integer.parseInt(value)));
                            break;
                        case "width":
                            this.crosshairMod.getCrosshair().setWidth(Integer.parseInt(value));
                            break;
                        case "height":
                            this.crosshairMod.getCrosshair().setHeight(Integer.parseInt(value));
                            break;
                        case "gap":
                            this.crosshairMod.getCrosshair().setGap(Integer.parseInt(value));
                            break;
                        case "thickness":
                            this.crosshairMod.getCrosshair().setThickness(Integer.parseInt(value));
                            break;
                        case "rainbow":
                            this.crosshairMod.getCrosshair().setRainbowCrosshair(Boolean.valueOf(value));
                            break;
                        case "rainbow_speed":
                            this.crosshairMod.getCrosshair().setRainbowSpeed(Integer.valueOf(value));
                            break;
                        default:
                            if (!attribute.equals("dynamic_bow")) {
                                continue;
                            }
                            this.crosshairMod.getCrosshair().setDynamicBow(Boolean.parseBoolean(value));
                            break;
                    }
                }
            }
            bufferedReader.close();
            return true;
        } catch (Exception readingException) {
            readingException.printStackTrace();
            return false;
        }
    }

    public boolean writeSaveFile(final int crosshairType, final boolean enabled, final int colour_red, final int colour_green, final int colour_blue, final int colour_opacity, final boolean visibleDefault, final boolean visibleHiddenGui, final boolean visibleDebug, final boolean visibleSpectator, final boolean visibleThirdPerson, final boolean outline, final int outlineColour_red, final int outlineColour_green, final int outlineColour_blue, final int outlineColour_opacity, final boolean dot, final int dotColour_red, final int dotColour_green, final int dotColour_blue, final int dotColour_opacity, final int width, final int height, final int gap, final int thickness, final boolean dynamicBow, final boolean rainbow, final int rainbowspeed) {
        try {
            final FileWriter fileWriter = new FileWriter(this.saveFile);
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

    public boolean writeSaveFileDefault() {
        return writeSaveFile(0, true, 255, 255, 255, 255, true, true, true, true, true, true, 0, 0, 0, 255, true, 255, 255, 255, 255, 5, 5, 3, 1, true, false, 500);
    }

    public void saveCurrentCrosshair() {
        writeSaveFile(this.crosshairMod.getCrosshair().getCrosshairTypeID(), this.crosshairMod.getCrosshair().getEnabled(), this.crosshairMod.getCrosshair().getColour().getRed(), this.crosshairMod.getCrosshair().getColour().getGreen(), this.crosshairMod.getCrosshair().getColour().getBlue(), this.crosshairMod.getCrosshair().getColour().getAlpha(), this.crosshairMod.getCrosshair().getVisibleDefault(), this.crosshairMod.getCrosshair().getVisibleHiddenGui(), this.crosshairMod.getCrosshair().getVisibleDebug(), this.crosshairMod.getCrosshair().getVisibleSpectator(), this.crosshairMod.getCrosshair().getVisibleThirdPerson(), this.crosshairMod.getCrosshair().getOutline(), this.crosshairMod.getCrosshair().getOutlineColour().getRed(), this.crosshairMod.getCrosshair().getOutlineColour().getGreen(), this.crosshairMod.getCrosshair().getOutlineColour().getBlue(), this.crosshairMod.getCrosshair().getOutlineColour().getAlpha(), this.crosshairMod.getCrosshair().getDot(), this.crosshairMod.getCrosshair().getDotColour().getRed(), this.crosshairMod.getCrosshair().getDotColour().getGreen(), this.crosshairMod.getCrosshair().getDotColour().getBlue(), this.crosshairMod.getCrosshair().getDotColour().getAlpha(), this.crosshairMod.getCrosshair().getWidth(), this.crosshairMod.getCrosshair().getHeight(), this.crosshairMod.getCrosshair().getGap(), this.crosshairMod.getCrosshair().getThickness(), this.crosshairMod.getCrosshair().getDynamicBow(), this.crosshairMod.getCrosshair().getRainbowCrosshair(), this.crosshairMod.getCrosshair().getRainbowSpeed());
    }
}
