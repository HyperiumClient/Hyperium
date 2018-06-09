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
import cc.hyperium.event.GuiClickEvent;
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
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;


/**
 * Panel for controlling spotify in-game
 *
 * @author FalseHonesty
 */
public class SpotifyControls extends AbstractMod {
    public static SpotifyControls instance;
    private final int width = 150;
    private final int height = 50;
    private final Color bg = new Color(30, 30, 30, 255);
    private final Color progress = new Color(54, 54, 54);
    private final Color highlight = new Color(149, 201, 144);
    private final Color white = Color.WHITE;
    private final Metadata metadata;
    private final File configFile;
    private int x = 0;
    private int y = 0;
    private long current = 0;
    private long cachedTime = 0;
    private long systemTime = 0;
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
            BetterJsonObject obj = new BetterJsonObject(FileUtils.readFileToString(configFile));
            this.x = obj.get("x").getAsInt();
            this.y = obj.get("y").getAsInt();
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

    public void save() {
        BetterJsonObject obj = new BetterJsonObject();
        obj.addProperty("x", this.x);
        obj.addProperty("y", this.y);
        obj.writeToFile(configFile);
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

    @InvokeEvent
    private void onClick(GuiClickEvent event) {
        if (!Settings.SPOTIFY_CONTROLS || Spotify.instance == null || !(event.getGui() instanceof GuiChat)) {
            return;
        }

        int mx = event.getMouseX(), my = event.getMouseY();

        // If the click is inside of our controls panel...
        if (mx > x && mx < x + width + 50 && my > y && my < y + height) {
            event.setCancelled(true);

            int playX = x + 175, playY = y + 5;
            if (mx > playX && mx < playX + 20 && my > playY && my < playY + 20) {
                Spotify.instance.pause(Spotify.instance.getCachedStatus().isPlaying());
            }
        }

        play.updateDynamicTexture();
    }

    public void renderControls() {
        if (Spotify.instance == null) {
            return;
        }

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

        int x = this.x, y = this.y;

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        this.x = (int) HyperiumGui.clamp(x, 0, sr.getScaledWidth() - 200);
        this.y = (int) HyperiumGui.clamp(y, 0, sr.getScaledHeight() - 50);

        if (imageToGenerate != null) {
            this.art = new DynamicTexture(imageToGenerate);
            imageToGenerate = null;
        }

        if (art != null) {
            drawImage(
                    art,
                    x,
                    y,
                    50
            );
        } else {
            Gui.drawRect(
                    x, y, x + 50, y + 50, progress.getRGB()
            );
        }

        x += 50;

        Gui.drawRect(x, y, x + width, y + height, bg.getRGB());

        GL11.glPushMatrix();
        GL11.glScalef(1.2f, 1.2f, 1);
        fontRenderer.drawString(name, (float) ((x + 5) / 1.2), (float) ((y + 5) / 1.2), white.getRGB(), false);
        GL11.glPopMatrix();

        fontRenderer.drawString(artist, x + 5, y + 18, white.getRGB());

        String currTimestamp = current / 60 + ":" + StringUtils.leftPad(String.valueOf(current % 60), 2, "0");
        int currTimestampWidth = fontRenderer.getStringWidth(currTimestamp);
        fontRenderer.drawString(currTimestamp, x + 5, y + 35, white.getRGB());

        float percentComplete = (float) current / (float) length;
        int barX = x + currTimestampWidth + 10;
        int barY = y + 35;

        Gui.drawRect(barX, barY, barX + 80, barY + 8, progress.getRGB());
        Gui.drawRect(barX, barY, (int) (barX + (80 * percentComplete)), barY + 8, highlight.getRGB());

        String endTimestamp = length / 60 + ":" + StringUtils.leftPad(String.valueOf(length % 60), 2, "0");
        fontRenderer.drawString(endTimestamp, barX + 80 + 5, barY, white.getRGB());

        if (pause != null && play != null) {
            drawImage(
                    paused ? pause : play,
                    x + 125,
                    y + 5,
                    20
            );
        }
    }

    private void drawImage(DynamicTexture texture, int x, int y, int imgSize) {
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

    @Override
    public Metadata getModMetadata() {
        return this.metadata;
    }

    @Override
    protected void finalize() {
        save();
    }
}
