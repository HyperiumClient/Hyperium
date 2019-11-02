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

package cc.hyperium.gui;

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.client.TickEvent;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ParticleOverlay {
    private static ParticleOverlay overlay;
    private final List<Particle> particles = new ArrayList<>();
    private float h = 0.1F;
    private long last;

    private ParticleOverlay() {
        int max = Settings.MAX_PARTICLES;
        for (int i = 0; i < max; i++) {
            Particle particle = new Particle();
            particles.add(particle);
        }
    }

    public static ParticleOverlay getOverlay() {
        if (overlay == null) {
            overlay = new ParticleOverlay();
            EventBus.INSTANCE.register(overlay);
        }

        return overlay;
    }

    public boolean purchased() {
        HyperiumPurchase self = PurchaseApi.getInstance().getSelf();
        return self != null && self.hasPurchased(EnumPurchaseType.PARTICLE_BACKGROUND);
    }

    public void render(int mouseX, int mouseY, int guiLeft, int guiTop, int guiRight, int guiBottom) {
        if (!purchased()) return;
        try {
            float step = (float) (0.01 * (Settings.MAX_PARTICLES / 100));
            Mode m = getMode();
            if (m == Mode.OFF) return;
            if (!Settings.PARTICLES_INV && Minecraft.getMinecraft().currentScreen instanceof GuiInventory) {
                return;
            }

            last = System.currentTimeMillis();

            for (Particle particle : particles) {
                double w = 1;
                float v1 = ((float) ResolutionUtil.current().getScaledWidth_double()) * particle.x;
                float v2 = ((float) ResolutionUtil.current().getScaledHeight_double()) * particle.y;
                double mouseDis = Math.pow(v1 - mouseX, 2) + Math.pow(v2 - mouseY, 2);
                int i = ResolutionUtil.current().getScaledWidth() / 12;

                if (mouseDis < Math.pow(i, 2)) {
                    float xVec = Math.min(500F, (float) i / (mouseX - v1));
                    float yVec = Math.min(500F, (float) i / (mouseY - v2));
                    v1 -= xVec;
                    v2 -= yVec;
                    particle.regenerateVector();
                }

                particle.x = v1 / (float) ResolutionUtil.current().getScaledWidth_double();
                particle.y = v2 / (float) ResolutionUtil.current().getScaledHeight_double();

                for (Particle particle1 : particles) {
                    double v = particle.distSqTo(particle1);
                    //Threshold for line
                    if (v < 0.02) {
                        double lineStrength = Math.min(10000.0D, 1.0D / v) / 100D;
                        float x2 = ((float) ResolutionUtil.current().getScaledWidth_double()) * particle1.x;
                        float y2 = ((float) ResolutionUtil.current().getScaledHeight_double()) * particle1.y;
                        double alpha = 100 + ((0.02 / 155) * v);

                        boolean flag = false;
                        if (((v1 >= guiLeft && v1 <= guiRight) || (x2 >= guiLeft && x2 <= guiRight))
                            && ((v2 >= guiTop && v2 <= guiBottom) || (y2 >= guiTop && y2 <= guiBottom))) {
                            if (!Settings.PARTICLES_INV) continue;
                            alpha /= 4;
                            flag = true;
                        }


                        int color = Color.HSBtoRGB(h, 0.8F, 0.8F);
                        Color eee = new Color(color);
                        eee = new Color(eee.getRed(), eee.getBlue(), eee.getGreen(), flag ? 255 / 4 : 255);

                        switch (m) {
                            case PLAIN_1:
                                RenderUtils.drawLine(v1, v2, x2, y2, (float) lineStrength, new Color(255, 255, 255, (int) alpha).getRGB());
                                break;
                            case PLAIN_2:
                                RenderUtils.drawLine(v1, v2, x2, y2, 1F, new Color(255, 255, 255, (int) alpha).getRGB());
                                break;
                            case CHROMA_1:
                                RenderUtils.drawLine(v1, v2, x2, y2, (float) lineStrength, eee.getRGB());
                                break;
                            case CHROMA_2:
                                RenderUtils.drawLine(v1, v2, x2, y2, 1F, eee.getRGB());
                                break;
                        }

                        w += lineStrength;
                    }
                }

                if (h >= 1.0F) h = 0.0F;
                h += step;
                if (!Settings.PARTICLES_INV) continue;
                w = Math.sqrt(w) / 10D;
                Gui.drawRect((int) v1, (int) v2, (int) (v1 + w), (int) (v2 + w), Color.WHITE.getRGB());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @InvokeEvent
    public void tick(TickEvent event) {
        if (!purchased()) return;
        if (System.currentTimeMillis() - last < 1000) particles.forEach(Particle::update);
    }

    public Mode getMode() {
        try {
            return Mode.valueOf(Settings.PARTICLE_MODE.replace(" ", "_"));
        } catch (Exception e) {
            return Mode.OFF;
        }
    }

    public enum Mode {
        OFF,
        PLAIN_1,
        PLAIN_2,
        CHROMA_1,
        CHROMA_2
    }

    static class Particle {
        float x;
        float y;
        float xVec;
        float yVec;

        Particle() {
            x = (float) ThreadLocalRandom.current().nextDouble(0, 1);
            y = (float) ThreadLocalRandom.current().nextDouble(0, 1);
            regenerateVector();
        }

        void regenerateVector() {
            xVec = (float) ThreadLocalRandom.current().nextDouble(-.003, .003);
            yVec = (float) ThreadLocalRandom.current().nextDouble(-.003, .003);

        }

        public void update() {
            x += xVec;
            if (x > 1.0)
                x = x - 1.0F;
            if (x < 0)
                x = 1.0F - x;
            y += yVec;
            if (y > 1.0)
                y = y - 1.0F;
            if (y < 0)
                y = 1.0F - y;

        }

        double distSqTo(Particle other) {
            return Math.pow(other.x - x, 2) + Math.pow(other.y - y, 2);
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }
    }
}
