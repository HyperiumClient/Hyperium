package cc.hyperium.gui;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.mods.levelhead.Levelhead;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMainMenu;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ParticleOverlay {

    private static ParticleOverlay overlay;
    private List<Particle> particles = new ArrayList<>();
    private long last;

    public ParticleOverlay() {
        int max = 200;
        for (int i = 0; i < max; i++) {
            particles.add(new Particle());
        }
    }

    public static ParticleOverlay getOverlay() {
        if (overlay == null) {
            overlay = new ParticleOverlay();
            EventBus.INSTANCE.register(overlay);
        }
        return overlay;
    }

    public void render(int mouseX, int mouseY) {
        last = System.currentTimeMillis();
        for (Particle particle : particles) {
            double w = 1;

            float v1 = ((float) ResolutionUtil.current().getScaledWidth_double()) * particle.x;
            float v2 = ((float) ResolutionUtil.current().getScaledHeight_double()) * particle.y;
            double mouseDis = Math.pow(v1 - mouseX, 2) + Math.pow(v2 - mouseY, 2);
            if (mouseDis < Math.pow(ResolutionUtil.current().getScaledWidth() / 8, 2)) {
                float moveFac = ResolutionUtil.current().getScaledWidth() / 8;
                float xVec = Math.min(500F, moveFac / (mouseX - v1));
                float yVec = Math.min(500F, moveFac / (mouseY - v2));
                v1 -= xVec;
                v2 -= yVec;
                particle.regenerateVector();
            }

            particle.x = v1 / (float) ResolutionUtil.current().getScaledWidth_double();
            particle.y = v2 / (float) ResolutionUtil.current().getScaledHeight_double();
            int potentialMax = Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu ? 255 : 150;
            for (Particle particle1 : particles) {
                double v = particle.distSqTo(particle1);
                //Threshold for line
                if (v < 0.02) {
                    double lineStrength = Math.min(10000.0D, 1.0D / v) / 100D;
                    float x2 = ((float) ResolutionUtil.current().getScaledWidth_double()) * particle1.x;
                    float y2 = ((float) ResolutionUtil.current().getScaledHeight_double()) * particle1.y;

                    float min = (float) Math.min(potentialMax, lineStrength * 15);
                    int rgb = new Color(255, 40, 234, ((int) min)).getRGB();
                    RenderUtils.drawLine(v1, v2, x2, y2, (float) lineStrength, Levelhead.getRGBColor());
                    w += lineStrength;
                }
            }
            w = Math.sqrt(w) / 10D;
            Gui.drawRect((int) v1, (int) v2, (int) (v1 + w), (int) (v2 + w), Color.RED.getRGB());
        }
    }

    @InvokeEvent
    public void tick(TickEvent event) {
        if (System.currentTimeMillis() - last < 1000)
            for (Particle particle : particles) {
                particle.update();
            }

    }

    class Particle {
        float x;
        float y;
        float xVec;
        float yVec;

        public Particle() {
            x = (float) ThreadLocalRandom.current().nextDouble(0, 1);
            y = (float) ThreadLocalRandom.current().nextDouble(0, 1);
            regenerateVector();
        }

        public void regenerateVector() {
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

        public double distSqTo(Particle other) {
            return Math.pow(other.x - this.x, 2) + Math.pow(other.y - this.y, 2);
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }
    }
}
