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

package cc.hyperium.mods.spotify;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderHUDEvent;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.integrations.spotify.Spotify;
import cc.hyperium.integrations.spotify.impl.SpotifyInformation;
import cc.hyperium.integrations.spotify.impl.Track;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.BetterJsonObject;
import cc.hyperium.utils.ChatColor;

import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;


/**
 * Panel for controlling spotify in-game
 *
 * @author FalseHonesty
 */
public class SpotifyControls extends AbstractMod {

    public static SpotifyControls instance;

    private double x = 0;
    private double y = 0;
    private double scale = 1;

    private final int width = 150;
    private final int height = 50;
    private final Color bg = new Color(30, 30, 30, 255);
    private final Color progress = new Color(54, 54, 54);
    private final Color highlight = new Color(149, 201, 144);
    private final Color white = Color.WHITE;
    private final Metadata metadata;
    private final File configFile;

    private long current = 0;
    private long cachedTime = 0;
    private long systemTime = 0;
    public int concatNameCount = 0;

    private DynamicTexture pause, play, art;
    private String currentURI = "";
    private BufferedImage imageToGenerate;

    public SpotifyControls() {
        instance = this;

        metadata = new Metadata(this, "Spotify Controls", "1.0", "FalseHonesty");
        metadata.setDisplayName(ChatColor.GOLD + "Spotify Controls");
        systemTime = Minecraft.getSystemTime();

        try {
            BufferedImage playImg = ImageIO.read(
                getClass().getResourceAsStream("/assets/hyperium/icons/play.png")
            );

            BufferedImage pauseImg = ImageIO.read(
                getClass().getResourceAsStream("/assets/hyperium/icons/pause.png")
            );

            this.play = new DynamicTexture(playImg);
            this.pause = new DynamicTexture(pauseImg);
        } catch (IOException e) {
            System.out.println("Can't load Spotify icons!");
            e.printStackTrace();
        }

        this.configFile = new File(Hyperium.folder, "spotifycontrols.json");

        if (!configFile.exists()) {
            save();
        }

        try {
            BetterJsonObject obj = new BetterJsonObject(FileUtils.readFileToString(configFile, Charset.defaultCharset()));
            this.x = obj.optDouble("x");
            this.y = obj.optDouble("y");
            this.scale = obj.optDouble("scale", 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AbstractMod init() {
        EventBus.INSTANCE.register(this);
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new SpotifyCommand());
        return this;
    }

    @InvokeEvent
    public void onRender(RenderHUDEvent e) {
        while (Minecraft.getSystemTime() > this.systemTime + 1000) {
            this.systemTime += 1000;

            if (Spotify.instance == null
                || !Spotify.instance.getCachedStatus().isPlaying()
                || Spotify.instance.getCachedStatus().getTrack().getLength() <= current) {
                continue;
            }

            current++;
        }

        if (Settings.SPOTIFY_CONTROLS) {
            GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;

            if (currentScreen == null || currentScreen instanceof GuiChat) {
                renderControls();
            }
        }
    }

    public void renderControls() {
        if (Spotify.instance == null) {
            return;
        }

        Double realScale = this.scale;

        GlStateManager.pushMatrix();
        GlStateManager.scale(realScale, realScale, 1);

        SpotifyInformation info = Spotify.instance.getCachedStatus();
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

        boolean paused = info.isPlaying();
        String name = "Not Playing", artist = "", uri = currentURI;

        Track track = info.getTrack();
        if (track != null) {
            if (track.getTrackResource() != null) {
                name = track.getTrackResource().getName();
                uri = track.getTrackResource().getUri();
            }

            if (track.getArtistResource() != null) {
                artist = track.getArtistResource().getName();
            }
        }

        final String finalURI = uri;

        if (!uri.equals(currentURI)) {
            Multithreading.runAsync(() -> {
                try {
                    JsonObject obj = Spotify.instance.get(
                        "https://embed.spotify.com/oembed?url=" + finalURI,
                        false
                    );

                    imageToGenerate = ImageIO.read(
                        new URL(obj.get("thumbnail_url").getAsString())
                    );
                } catch (IOException e) {
                    art = null;
                    e.printStackTrace();
                }
            });

            currentURI = uri;
        }

        if ((long) info.getPlayingPosition() != cachedTime) {
            cachedTime = (long) info.getPlayingPosition();
            current = cachedTime;
        }

        long length = 1;
        if (track != null)
            length = track.getLength();

        name = fontRenderer.trimStringToWidth(name, (int) ((width - 30) * 0.8));
        artist = fontRenderer.trimStringToWidth(artist, width - 30);

        double x = (this.x / realScale);
        double y = (this.y / realScale);

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        this.x = (int) HyperiumGui.clamp((float) (x * realScale), 0, (float) (sr.getScaledWidth() - getWidthWithScale()) + 1);
        this.y = (int) HyperiumGui.clamp((float) (y * realScale), 0, (float) (sr.getScaledHeight() - getHeightWithScale()) + 1);

        if (imageToGenerate != null) {
            this.art = new DynamicTexture(imageToGenerate);
            imageToGenerate = null;
        }

        if (art != null) {
            drawImage(art, x, y, 50
            );
        } else {
            drawRectDouble(x, y, x + 50, y + 50, progress.getRGB());
        }

        x += 50;

        drawRectDouble(x, y, x + width, y + height, bg.getRGB());

        GlStateManager.pushMatrix();
        GlStateManager.scale(1.2f, 1.2f, 1);
        if (name.length() > 16) {
            int concatNameCount2 = concatNameCount + 16;
            String name2 = track.getTrackResource().getName();
            String concatName = name2 + "    " + name2;
            Minecraft.getMinecraft().fontRendererObj.drawString(concatName.substring(concatNameCount, concatNameCount + 16), (float) ((x + 5) / 1.2), (float) ((y + 5) / 1.2), white.getRGB(), false);
            if (System.currentTimeMillis() % 100 == 0) {
                concatNameCount++;
                concatNameCount2 = concatNameCount + 16;
            }
            if (concatNameCount2 == concatName.length())
                concatNameCount = 0;
        } else {
            fontRenderer.drawString(name, (float) ((x + 5) / 1.2), (float) ((y + 5) / 1.2), white.getRGB(), false);
        }
        //fontRenderer.drawString(name, (float) ((x + 5) / 1.2), (float) ((y + 5) / 1.2), white.getRGB(), false);
        GlStateManager.popMatrix();

        fontRenderer.drawString(artist, (float) (x + 5), (float) (y + 18), white.getRGB(), false);

        String currTimestamp = current / 60 + ":" + StringUtils.leftPad(String.valueOf(current % 60), 2, "0");
        int currTimestampWidth = fontRenderer.getStringWidth(currTimestamp);
        fontRenderer.drawString(currTimestamp, (float) (x + 5), (float) (y + 35), white.getRGB(), false);

        float percentComplete = (float) current / (float) length;
        double barX = x + currTimestampWidth + 10;
        double barY = y + 35;

        drawRectDouble(barX, barY, barX + 80, barY + 8, progress.getRGB());
        drawRectDouble(barX, barY, (int) (barX + (80 * percentComplete)), barY + 8, highlight.getRGB());

        String endTimestamp = length / 60 + ":" + StringUtils.leftPad(String.valueOf(length % 60), 2, "0");
        fontRenderer.drawString(endTimestamp, (float) (barX + 80 + 5), (float) (barY), white.getRGB(), false);

        if (pause != null && play != null) {
            drawImage(
                paused ? pause : play,
                x + 125,
                y + 5,
                20
            );
        }

        GlStateManager.popMatrix();
    }

    /* It was broken so I've temporarily disabled it
    @InvokeEvent
    private void onClick(GuiClickEvent event) {
        if (!Settings.SPOTIFY_CONTROLS || Spotify.instance == null || !(event.getGui() instanceof GuiChat)) {
            return;
        }

        double mx = event.getMouseX();
        double my = event.getMouseY();

        // If the click is inside of our controls panel...
        if (mx > x && mx < x + width + 50 && my > y && my < y + height) {
            event.setCancelled(true);

            double playX = x + 175;
            double playY = y + 5;

            if (mx > playX && mx < playX + 20 && my > playY && my < playY + 20) {
                Spotify.instance.pause(Spotify.instance.getCachedStatus().isPlaying());
            }
        }

        play.updateDynamicTexture();
    }*/

    private void drawImage(DynamicTexture texture, double x, double y, int imgSize) {
        float imgScale = (float) imgSize / (float) 256;

        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.scale(imgScale, imgScale, imgScale);
        GlStateManager.bindTexture(texture.getGlTextureId());
        GlStateManager.enableTexture2D();

        float f = 256 * 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x / imgScale, y / imgScale + 256, 0)
            .tex(0, f)
            .endVertex();
        worldrenderer.pos(x / imgScale + 256, y / imgScale + 256, 0)
            .tex(f, f)
            .endVertex();
        worldrenderer.pos(x / imgScale + 256, y / imgScale, 0)
            .tex(f, 0)
            .endVertex();
        worldrenderer.pos(x / imgScale, y / imgScale, 0)
            .tex(0, 0)
            .endVertex();
        tessellator.draw();

        GlStateManager.popMatrix();
    }

    public void drawRectDouble(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, top, 0.0D).endVertex();
        worldrenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    @Override
    public Metadata getModMetadata() {
        return this.metadata;
    }

    @Override
    protected void finalize() {
        save();
    }

    public void setScale(double scale) {
        if (scale < 0.5) {
            scale = 0.5;
        } else if (scale > 1.5) {
            scale = 1.5;
        }

        this.scale = scale;
    }

    public double getScale() {
        return this.scale;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Multiplies the width by the scale
     * <p>
     * 1.335 is a magic value that makes the scale more accurate
     *
     * @return the visible width of the controls
     */
    public double getWidthWithScale() {
        return (width * scale) * 1.335;
    }

    /**
     * Multiplies the height by the scale
     *
     * @return the visible height of the controls
     */
    public double getHeightWithScale() {
        return height * scale;
    }

    public void save() {
        BetterJsonObject obj = new BetterJsonObject();
        obj.addProperty("x", this.x);
        obj.addProperty("y", this.y);
        obj.addProperty("scale", this.scale);
        obj.writeToFile(configFile);
    }
}
