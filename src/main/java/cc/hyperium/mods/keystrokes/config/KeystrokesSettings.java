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

package cc.hyperium.mods.keystrokes.config;

import cc.hyperium.Hyperium;
import cc.hyperium.mods.keystrokes.KeystrokesMod;
import cc.hyperium.mods.keystrokes.keys.impl.CustomKey;
import cc.hyperium.mods.keystrokes.render.CustomKeyWrapper;
import cc.hyperium.utils.BetterJsonObject;
import cc.hyperium.utils.JsonHolder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class KeystrokesSettings {

    private final KeystrokesMod theMod;
    private final File configFile;
    private int x;
    private int y;
    private boolean enabled = true;
    private boolean chroma;
    private boolean mouseButtons;
    private boolean showCPS;
    private boolean showCPSOnButtons;
    private boolean showSpacebar;
    private double scale = 1;
    private double fadeTime = 1;
    private int red = 255;
    private int green = 255;
    private int blue = 255;
    private int pressedRed;
    private int pressedGreen;
    private int pressedBlue;
    private boolean leftClick = true;
    private boolean showingSneak;
    private boolean showingFPS;
    private boolean keyBackground = true;
    private boolean showingWASD = true;
    private boolean literalKeys;
    private int keyBackgroundOpacity = 120;
    private int keyBackgroundRed;
    private int keyBackgroundGreen;
    private int keyBackgroundBlue;
    private boolean arrowKeys;
    private List<CustomKeyWrapper> configWrappers = new ArrayList<>();

    public KeystrokesSettings(KeystrokesMod mod, File directory) {
        if (!directory.exists()) directory.mkdirs();
        theMod = mod;
        configFile = new File(directory, "keystrokes.json");
    }

    public void load() {
        try {
            if (!configFile.getParentFile().exists() || !configFile.exists()) {
                save();
                return;
            }

            BufferedReader f = new BufferedReader(new FileReader(configFile));
            List<String> options = f.lines().collect(Collectors.toList());
            if (options.isEmpty()) return;
            String builder = String.join("", options);
            if (builder.trim().length() > 0) parseSettings(new BetterJsonObject(builder.trim()));
            f.close();
        } catch (Exception ex) {
            Hyperium.LOGGER.warn("Could not load config file! {}", configFile.getName());
            save();
        }
    }

    public void save() {
        try {
            if (!configFile.getParentFile().exists()) configFile.getParentFile().mkdirs();
            if (!configFile.exists() && !configFile.createNewFile()) return;

            BetterJsonObject object = new BetterJsonObject();
            object.addProperty("x", x);
            object.addProperty("y", y);
            object.addProperty("leftClick", leftClick);
            object.addProperty("red", red);
            object.addProperty("green", green);
            object.addProperty("blue", blue);
            object.addProperty("pressedRed", pressedRed);
            object.addProperty("pressedGreen", pressedGreen);
            object.addProperty("pressedBlue", pressedBlue);
            object.addProperty("scale", getScale());
            object.addProperty("fadeTime", getFadeTime());
            object.addProperty("enabled", enabled);
            object.addProperty("chroma", chroma);
            object.addProperty("mouseButtons", mouseButtons);
            object.addProperty("showCPS", showCPS);
            object.addProperty("showCPSOnButtons", showCPSOnButtons);
            object.addProperty("showSpacebar", showSpacebar);
            object.addProperty("showSneak", showingSneak);
            object.addProperty("showFps", showingFPS);
            object.addProperty("keyBackground", keyBackground);
            object.addProperty("showingWASD", showingWASD);
            object.addProperty("literalKeys", literalKeys);
            object.addProperty("keyBackgroundOpacity", keyBackgroundOpacity);
            object.addProperty("keyBackgroundRed", keyBackgroundRed);
            object.addProperty("keyBackgroundGreen", keyBackgroundGreen);
            object.addProperty("keyBackgroundBlue", keyBackgroundBlue);
            object.addProperty("arrowKeys", arrowKeys);
            JsonArray keys = new JsonArray();
            theMod.getRenderer().getCustomKeys().forEach(wrapper -> {
                JsonHolder holder = new JsonHolder();
                holder.put("key", wrapper.getKey().getKey());
                holder.put("type", wrapper.getKey().getType());
                holder.put("xOffset", wrapper.getxOffset());
                holder.put("yOffset", wrapper.getyOffset());
                keys.add(holder.getObject());
            });

            object.getData().add("custom", keys);
            object.writeToFile(configFile);
        } catch (Exception ex) {
            Hyperium.LOGGER.warn(String.format("Could not save config file! (\"%s\")", configFile.getName()));
        }
    }

    private void parseSettings(BetterJsonObject object) {
        x = object.optInt("x");
        y = object.optInt("y");
        red = object.optInt("red", 255);
        green = object.optInt("green", 255);
        blue = object.optInt("blue", 255);
        pressedRed = object.optInt("pressedRed");
        pressedGreen = object.optInt("pressedGreen");
        pressedBlue = object.optInt("pressedBlue");
        setScale(object.optDouble("scale", 1.0D));
        setFadeTime(object.optDouble("fadeTime", 1.0D));
        enabled = object.optBoolean("enabled", true);
        chroma = object.optBoolean("chroma");
        leftClick = object.optBoolean("leftClick", true);
        mouseButtons = object.optBoolean("mouseButtons");
        showCPS = object.optBoolean("showCPS");
        showCPSOnButtons = object.optBoolean("showCPSOnButtons");
        showSpacebar = object.optBoolean("showSpacebar");
        showingSneak = object.optBoolean("showSneak");
        showingFPS = object.optBoolean("showFps");
        keyBackground = object.optBoolean("keyBackground", true);
        showingWASD = object.optBoolean("showingWASD", true);
        literalKeys = object.optBoolean("literalKeys");
        keyBackgroundOpacity = object.optInt("keyBackgroundOpacity", 120);
        keyBackgroundRed = object.optInt("keyBackgroundRed");
        keyBackgroundGreen = object.optInt("keyBackgroundGreen");
        keyBackgroundBlue = object.optInt("keyBackgroundBlue");
        arrowKeys = object.optBoolean("arrowKeys");
        JsonObject data = object.getData();
        if (data.has("custom")) {
            JsonArray custom = data.getAsJsonArray("custom");
            for (JsonElement element : custom) {
                JsonHolder holder = new JsonHolder(element.getAsJsonObject());
                CustomKeyWrapper wrapper = new CustomKeyWrapper(new CustomKey(
                    theMod, holder.optInt("key"), holder.optInt("type")), holder.optInt("xOffset"), holder.optInt("yOffset"));
                configWrappers.add(wrapper);
            }
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getPressedRed() {
        return pressedRed;
    }

    public void setPressedRed(int red) {
        pressedRed = red;
    }

    public int getPressedGreen() {
        return pressedGreen;
    }

    public void setPressedGreen(int green) {
        pressedGreen = green;
    }

    public int getPressedBlue() {
        return pressedBlue;
    }

    public void setPressedBlue(int blue) {
        pressedBlue = blue;
    }

    public double getScale() {
        return capDouble(scale, 0.5F, 1.5F);
    }

    public void setScale(double scale) {
        this.scale = capDouble(scale, 0.5F, 1.5F);
    }

    public double getFadeTime() {
        return capDouble(fadeTime, 0.1F, 3.0F);
    }

    public void setFadeTime(double scale) {
        fadeTime = capDouble(scale, 0.1F, 3.0F);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isShowingMouseButtons() {
        return mouseButtons;
    }

    public void setShowingMouseButtons(boolean showingMouseButtons) {
        mouseButtons = showingMouseButtons;
    }

    public boolean isShowingSpacebar() {
        return showSpacebar;
    }

    public void setShowingSpacebar(boolean showSpacebar) {
        this.showSpacebar = showSpacebar;
    }

    public boolean isShowingCPS() {
        return showCPS;
    }

    public void setShowingCPS(boolean showingCPS) {
        showCPS = showingCPS;
    }

    public boolean isShowingCPSOnButtons() {
        return showCPSOnButtons;
    }

    public void setShowingCPSOnButtons(boolean showCPSOnButtons) {
        this.showCPSOnButtons = showCPSOnButtons;
    }

    public boolean isChroma() {
        return chroma;
    }

    public void setChroma(boolean showingChroma) {
        chroma = showingChroma;
    }

    public boolean isLeftClick() {
        return leftClick;
    }

    public void setLeftClick(boolean leftClick) {
        this.leftClick = leftClick;
    }

    public boolean isShowingFPS() {
        return showingFPS;
    }

    public void setShowingFPS(boolean showingFPS) {
        this.showingFPS = showingFPS;
    }

    public boolean isKeyBackgroundEnabled() {
        return keyBackground;
    }

    public void setKeyBackgroundEnabled(boolean keyBackground) {
        this.keyBackground = keyBackground;
    }

    public boolean isShowingWASD() {
        return showingWASD;
    }

    public void setShowingWASD(boolean showingWASD) {
        this.showingWASD = showingWASD;
    }

    public boolean isUsingLiteralKeys() {
        return literalKeys;
    }

    public void setUsingLiteralKeys(boolean literalKeys) {
        this.literalKeys = literalKeys;
    }

    public int getKeyBackgroundOpacity() {
        return keyBackgroundOpacity;
    }

    public void setKeyBackgroundOpacity(int keyBackgroundOpacity) {
        this.keyBackgroundOpacity = keyBackgroundOpacity;
    }

    public int getKeyBackgroundRed() {
        return keyBackgroundRed;
    }

    public void setKeyBackgroundRed(int keyBackgroundRed) {
        this.keyBackgroundRed = keyBackgroundRed;
    }

    public int getKeyBackgroundGreen() {
        return keyBackgroundGreen;
    }

    public void setKeyBackgroundGreen(int keyBackgroundGreen) {
        this.keyBackgroundGreen = keyBackgroundGreen;
    }

    public int getKeyBackgroundBlue() {
        return keyBackgroundBlue;
    }

    public void setKeyBackgroundBlue(int keyBackgroundBlue) {
        this.keyBackgroundBlue = keyBackgroundBlue;
    }

    public boolean isUsingArrowKeys() {
        return arrowKeys;
    }

    public void setUsingArrowKeys(boolean arrowKeys) {
        this.arrowKeys = arrowKeys;
    }

    // spaghetti code because it doesnt work otherwise ( why :-( )
    public int getHeight() {
        int height = 50;
        if (showCPS || showSpacebar || showingFPS) height += 24;
        if (mouseButtons) height += 24;
        if (showingWASD) height += 48;
        if (!showingFPS) height -= 18;
        if (!showingSneak) height -= 18;
        if (!showCPS) height -= 18;
        if (showCPSOnButtons) height -= 18;
        return height;
    }

    public int getWidth() {
        return 74; // Hardcoded value
    }

    public List<CustomKeyWrapper> getConfigWrappers() {
        return configWrappers;
    }

    public boolean isShowingSneak() {
        return showingSneak;
    }

    public void setShowingSneak(boolean showingSneak) {
        this.showingSneak = showingSneak;
    }

    public KeystrokesMod getMod() {
        return theMod;
    }

    private double capDouble(double valueIn, double minValue, double maxValue) {
        return (valueIn < minValue) ? minValue : (Math.min(valueIn, maxValue));
    }

    public int getRenderX() {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());

        int width = getWidth();

        //Commenting out set calls to only create local override
        int x = getX();
        if (x < 0) {
            x = 0;
        } else if (x * getScale() > res.getScaledWidth() - (width * getScale())) {
            x = (int) ((res.getScaledWidth() - (width * getScale())) / getScale());
        }
        return x;
    }

    public int getRenderY() {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        int height = getHeight();
        int y = getY();
        if (y < 0) {
            y = 0;
        } else if (y * getScale() > res.getScaledHeight() - (height * getScale())) {
            y = (int) ((res.getScaledHeight() - (height * getScale())) / getScale());
        }
        return y;
    }
}
