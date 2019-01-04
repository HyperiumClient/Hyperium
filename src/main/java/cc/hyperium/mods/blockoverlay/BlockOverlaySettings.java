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

package cc.hyperium.mods.blockoverlay;

import cc.hyperium.utils.BetterJsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.stream.Collectors;

public class BlockOverlaySettings {
    private File configFile;
    private BlockOverlayMode overlayMode = BlockOverlayMode.OUTLINE;
    private boolean alwaysRender = true;
    private boolean isChroma = false;
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
        this.configFile = new File(directory, "blockoverlay.json");
    }

    public void load() {
        try {
            if (this.configFile.getParentFile().exists() && this.configFile.exists()) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(this.configFile));
                StringBuilder stringBuilder = new StringBuilder();
                for (String line : bufferedReader.lines().collect(Collectors.toList())) {
                    stringBuilder.append(line);
                }

                BetterJsonObject json = new BetterJsonObject(stringBuilder.toString());
                String overlayMode = json.optString("overlayMode");
                for (BlockOverlayMode mode : BlockOverlayMode.values()) {
                    if (mode.getName().equals(overlayMode)) {
                        this.overlayMode = mode;
                        break;
                    }
                }
                this.alwaysRender = json.optBoolean("alwaysRender");
                this.isChroma = json.optBoolean("isChroma");
                this.lineWidth = (float) json.optDouble("lineWidth");
                this.overlayRed = (float) json.optDouble("overlayRed");
                this.overlayGreen = (float) json.optDouble("overlayGreen");
                this.overlayBlue = (float) json.optDouble("overlayBlue");
                this.overlayAlpha = (float) json.optDouble("overlayAlpha");
                this.chromaSpeed = json.optInt("chromaSpeed");
            }
        } catch (Exception exception) {
            System.out.println("[BlockOverlay] Error occurred while loading configuration!");
        }
    }

    public void save() {
        try {
            if (!this.configFile.getParentFile().exists()) {
                this.configFile.getParentFile().mkdirs();
            }
            if (!this.configFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                this.configFile.createNewFile();
            }

            BetterJsonObject json = new BetterJsonObject();
            json.addProperty("overlayMode", this.overlayMode.getName());
            json.addProperty("alwaysRender", this.alwaysRender);
            json.addProperty("isChroma", this.isChroma);
            json.addProperty("lineWidth", this.lineWidth);
            json.addProperty("overlayRed", this.overlayRed);
            json.addProperty("overlayGreen", this.overlayGreen);
            json.addProperty("overlayBlue", this.overlayBlue);
            json.addProperty("overlayAlpha", this.overlayAlpha);
            json.addProperty("chromaSpeed", this.chromaSpeed);
            json.writeToFile(this.configFile);
        } catch (Exception exception) {
            System.out.println("[BlockOverlay] Error occurred while saving configuration!");
        }
    }

    public BlockOverlayMode getOverlayMode() {
        return this.overlayMode;
    }

    public void setOverlayMode(BlockOverlayMode overlayMode) {
        this.overlayMode = overlayMode;
    }

    public boolean isChroma() {
        return this.isChroma;
    }

    public void setChroma(boolean chroma) {
        this.isChroma = chroma;
    }

    public float getOverlayRed() {
        return this.overlayRed;
    }

    public void setOverlayRed(float overlayRed) {
        this.overlayRed = overlayRed;
    }

    public float getOverlayGreen() {
        return this.overlayGreen;
    }

    public void setOverlayGreen(float overlayGreen) {
        this.overlayGreen = overlayGreen;
    }

    public float getOverlayBlue() {
        return this.overlayBlue;
    }

    public void setOverlayBlue(float overlayBlue) {
        this.overlayBlue = overlayBlue;
    }

    public float getOverlayAlpha() {
        return this.overlayAlpha;
    }

    public void setOverlayAlpha(float overlayAlpha) {
        this.overlayAlpha = overlayAlpha;
    }

    public int getChromaSpeed() {
        return this.chromaSpeed;
    }

    public void setChromaSpeed(int chromaSpeed) {
        this.chromaSpeed = chromaSpeed;
    }

    public float getLineWidth() {
        return this.lineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }
}
