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

package cc.hyperium.mods.blockoverlay;

import cc.hyperium.Hyperium;
import cc.hyperium.utils.BetterJsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.stream.Collectors;

public class BlockOverlaySettings {
    private File configFile;
    private BlockOverlayMode overlayMode = BlockOverlayMode.DEFAULT;
    private boolean alwaysRender = true;
    private boolean isChroma;
    private float lineWidth = 1.0f;
    private float overlayRed = 1.0f;
    private float overlayGreen = 1.0f;
    private float overlayBlue = 1.0f;
    private float overlayAlpha = 1.0f;
    private int chromaSpeed = 5;

    public BlockOverlaySettings(File directory) {
        if (!directory.exists()) {
            directory.mkdirs();
        }

        configFile = new File(directory, "blockoverlay.json");
    }

    public void load() {
        try {
            if (configFile.getParentFile().exists() && configFile.exists()) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile));
                String stringBuilder = bufferedReader.lines().collect(Collectors.joining(""));
                BetterJsonObject json = new BetterJsonObject(stringBuilder);
                String overlayMode = json.optString("overlayMode");

                for (BlockOverlayMode mode : BlockOverlayMode.values()) {
                    if (mode.getName().equals(overlayMode)) {
                        this.overlayMode = mode;
                        break;
                    }
                }

                alwaysRender = json.optBoolean("alwaysRender");
                isChroma = json.optBoolean("isChroma");
                lineWidth = (float) json.optDouble("lineWidth");
                overlayRed = (float) json.optDouble("overlayRed");
                overlayGreen = (float) json.optDouble("overlayGreen");
                overlayBlue = (float) json.optDouble("overlayBlue");
                overlayAlpha = (float) json.optDouble("overlayAlpha");
                chromaSpeed = json.optInt("chromaSpeed");

                bufferedReader.close();
            }
        } catch (Exception exception) {
            Hyperium.LOGGER.error("Error occurred while loading BlockOverlay configuration!");
        }
    }

    public void save() {
        try {
            if (!configFile.getParentFile().exists()) {
                configFile.getParentFile().mkdirs();
            }
            if (!configFile.exists()) {
                configFile.createNewFile();
            }

            BetterJsonObject json = new BetterJsonObject();
            json.addProperty("overlayMode", overlayMode.getName());
            json.addProperty("alwaysRender", alwaysRender);
            json.addProperty("isChroma", isChroma);
            json.addProperty("lineWidth", lineWidth);
            json.addProperty("overlayRed", overlayRed);
            json.addProperty("overlayGreen", overlayGreen);
            json.addProperty("overlayBlue", overlayBlue);
            json.addProperty("overlayAlpha", overlayAlpha);
            json.addProperty("chromaSpeed", chromaSpeed);
            json.writeToFile(configFile);
        } catch (Exception exception) {
            Hyperium.LOGGER.error("Error occurred while saving BlockOverlay configuration!");
        }
    }

    public BlockOverlayMode getOverlayMode() {
        return overlayMode;
    }

    public void setOverlayMode(BlockOverlayMode overlayMode) {
        this.overlayMode = overlayMode;
    }

    public boolean isChroma() {
        return isChroma;
    }

    public void setChroma(boolean chroma) {
        isChroma = chroma;
    }

    public float getOverlayRed() {
        return overlayRed;
    }

    public void setOverlayRed(float overlayRed) {
        this.overlayRed = overlayRed;
    }

    public float getOverlayGreen() {
        return overlayGreen;
    }

    public void setOverlayGreen(float overlayGreen) {
        this.overlayGreen = overlayGreen;
    }

    public float getOverlayBlue() {
        return overlayBlue;
    }

    public void setOverlayBlue(float overlayBlue) {
        this.overlayBlue = overlayBlue;
    }

    public float getOverlayAlpha() {
        return overlayAlpha;
    }

    public void setOverlayAlpha(float overlayAlpha) {
        this.overlayAlpha = overlayAlpha;
    }

    public int getChromaSpeed() {
        return chromaSpeed;
    }

    public void setChromaSpeed(int chromaSpeed) {
        this.chromaSpeed = chromaSpeed;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }
}
