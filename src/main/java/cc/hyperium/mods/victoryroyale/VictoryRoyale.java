package cc.hyperium.mods.victoryroyale;

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderHUDEvent;
import cc.hyperium.mods.AbstractMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;

public class VictoryRoyale extends AbstractMod {
    private static VictoryRoyale INSTANCE;

    private ResourceLocation texture = new ResourceLocation("textures/material/victoryroyale/victory_royale.png");
    private ConcurrentLinkedQueue<WhiteLine> points = new ConcurrentLinkedQueue<>();

    private long start = 0;

    public static VictoryRoyale getInstance() {
        return INSTANCE;
    }

    @Override
    public AbstractMod init() {
        EventBus.INSTANCE.register(this);
        INSTANCE = this;
        return this;
    }

    @InvokeEvent
    public void onRender(RenderHUDEvent event) {
        if (Settings.VICTORY_ROYALE) {
            ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
            long in = System.currentTimeMillis() - start;

            if (in < 10000) {
                double out = 2000;
                double percent = in / out;
                double centerX = res.getScaledWidth_double() / 2;
                double centerY = res.getScaledHeight_double() / 2;
                double angle = Math.toDegrees(Math.atan(res.getScaledHeight_double() / res.getScaledWidth_double()));

                for (WhiteLine point : points) {
                    int trueX = (int) (point.xPercent * res.getScaledWidth_double());
                    int trueY = (int) (point.yPercent * res.getScaledHeight_double());

                    double deltaX = centerX - trueX;
                    double deltaY = centerY - trueY;

                    final int half = 180;
                    final int full = 360;

                    double radius = Math.toDegrees(MathHelper.atan2(-deltaY, deltaX)) + half;
                    int side;

                    if (radius > 0 && radius < angle || radius > full - angle) {
                        side = 1;
                    } else if (radius > angle && radius < half - angle) {
                        side = 0;
                    } else if (radius > half - angle && radius < half + angle) {
                        side = 3;
                    } else {
                        side = 2;
                    }

                    int px;
                    int py;
                    double slope = (centerY - trueY) / (centerX - trueX);

                    if (side == 1 || side == 3) {
                        int xpos = side == 1 ? res.getScaledWidth() : 0;
                        py = (int) (slope * (xpos - centerX) + centerY);
                        px = xpos;
                    } else {
                        py = side != 0 ? res.getScaledHeight() : 0;
                        px = (int) ((py - trueY + slope * trueX) / slope);
                    }

                    double deltaY2 = (trueY - py) - (trueY - py) * percent * .2D;
                    double deltaX2 = (trueX - px) - (trueX - px) * percent * .2D;

                    trueY = (int) (py + deltaY2);
                    trueX = (int) (px + deltaX2);

                    GlStateManager.enableTexture2D();
                    GlStateManager.enableBlend();
                    Tessellator renderer = Tessellator.getInstance();
                    WorldRenderer worldRenderer = renderer.getWorldRenderer();

                    float colorAlpha = percent < .5D ? 1F : (float) (1.0F - percent) * 2;

                    GlStateManager.enableAlpha();
                    GlStateManager.color(1, 1, 1, colorAlpha);
                    GlStateManager.disableTexture2D();
                    worldRenderer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);

                    if (side == 1) {
                        worldRenderer.pos((double) trueX, (double) trueY, 0.0D).endVertex(); // x
                        worldRenderer.pos((double) px + 0, (double) py + point.width, 0.0D).endVertex(); // y
                        worldRenderer.pos((double) px, (double) py, 0.0D).endVertex(); // z

                    } else if (side == 3) {
                        worldRenderer.pos((double) trueX, (double) trueY, 0.0D).endVertex();
                        worldRenderer.pos((double) px, (double) py, 0.0D).endVertex();
                        worldRenderer.pos((double) px + 10, (double) py, 0.0D).endVertex();

                    } else if (side == 2) {
                        worldRenderer.pos((double) trueX, (double) trueY, 0.0D).endVertex();
                        worldRenderer.pos((double) px, (double) py, 0.0D).endVertex();
                        worldRenderer.pos((double) px + 10, (double) py, 0.0D).endVertex();

                    } else {
                        worldRenderer.pos((double) trueX, (double) trueY, 0.0D).endVertex();
                        worldRenderer.pos((double) px + 10, (double) py, 0.0D).endVertex();
                        worldRenderer.pos((double) px, (double) py, 0.0D).endVertex();
                    }
                    renderer.draw();
                    GlStateManager.enableTexture2D();
                }
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            int imageWidth = res.getScaledWidth() / 4;
            Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            int time = 9000;
            float fadeOutAlpha = 1;

            if (in > time) {
                double color = (in - time) / 1000D;
                fadeOutAlpha = (float) (1 - color);
            }

            GlStateManager.color(1, 1, 1, in < 1000 ? (float) Math.pow(in / 1000D, 2) : fadeOutAlpha);
            GlStateManager.translate(imageWidth, 10, 0);

            Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, 1200, 675, imageWidth * 2, imageWidth, 1200, 675);
            GlStateManager.popMatrix();
        }
    }

    public void gameEnded() {
        if (Minecraft.getMinecraft().thePlayer == null) {
            return;
        }
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player.isInvisible() || player.isInvisibleToPlayer(player)) {
            return;
        }
        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (effect.getPotionID() == 14) {
                return;
            }
        }

        SoundHandler soundHandler = Minecraft.getMinecraft().getSoundHandler();
        if (soundHandler == null || Minecraft.getMinecraft().theWorld == null) {
            return;
        }
        if (Settings.VICTORY_ROYALE)
        {
            soundHandler.playSound(PositionedSoundRecord.create(new ResourceLocation("victory-royale"), (float) Minecraft.getMinecraft().thePlayer.posX, (float) Minecraft.getMinecraft().thePlayer.posY, (float) Minecraft.getMinecraft().thePlayer.posZ));
        }

        start = System.currentTimeMillis();
        points.clear();

        final int maxPoints = 15;

        for (int pointNumber = 0; pointNumber < maxPoints; pointNumber++) {
            ThreadLocalRandom current = ThreadLocalRandom.current();
            points.add(new WhiteLine(current.nextDouble(1.0), current.nextDouble(1.0), current.nextInt(5)));
        }
    }

    @Override
    public Metadata getModMetadata() {
        return new Metadata(this, "Victory Royale", "1.0", "Sk1er");
    }

    class WhiteLine {
        private double xPercent, yPercent;
        private int width;

        public WhiteLine(double xPercent, double yPercent, int width) {
            this.xPercent = xPercent;
            this.yPercent = yPercent;
            this.width = width;
        }
    }
}
