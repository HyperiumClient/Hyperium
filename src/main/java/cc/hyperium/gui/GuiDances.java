package cc.hyperium.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.handlers.HyperiumHandlers;
import cc.hyperium.handlers.handlers.animation.AbstractAnimationHandler;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.packets.serverbound.ServerCrossDataPacket;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.SimpleAnimValue;
import cc.hyperium.utils.UUIDUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class GuiDances extends HyperiumGui {

    String foc = null;
    private HashMap<String, Consumer<Boolean>> handlers = new HashMap<>();
    private HashMap<String, Runnable> cancel = new HashMap<>();
    private String lastFoc = null;
    private SimpleAnimValue simpleAnimValue;

    public GuiDances() {
        HyperiumHandlers handlers = Hyperium.INSTANCE.getHandlers();
        int seconds = 5;
        long delay = seconds * 1000L;
        this.handlers.put("Floss", (netty) -> {
            AbstractAnimationHandler abstractAnimationHandler = Hyperium.INSTANCE.getHandlers().getFlossDanceHandler();
            abstractAnimationHandler.get(Minecraft.getMinecraft().thePlayer.getUniqueID()).ensureAnimationFor(seconds);
            NettyClient client = NettyClient.getClient();
            if (client != null && netty) {
                client.write(ServerCrossDataPacket.build(new JsonHolder().put("type", "floss_update").put("flossing", true)));
                Multithreading.runAsync(() -> {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    client.write(ServerCrossDataPacket.build(new JsonHolder().put("type", "floss_update").put("flossing", false)));

                });
            }
        });
        this.handlers.put("Yeet", netty -> {
            Hyperium.INSTANCE.getHandlers().getYeetHandler().yeet(Minecraft.getMinecraft().thePlayer.getUniqueID());
            NettyClient client = NettyClient.getClient();
            if (client != null && netty) {
                client.write(ServerCrossDataPacket.build(new JsonHolder().put("type", "yeet").put("yeeting", true)));
            }
        });
        this.cancel.put("Yeet", () -> {

        });
        this.cancel.put("Floss", () -> {
            AbstractAnimationHandler abstractAnimationHandler = Hyperium.INSTANCE.getHandlers().getFlossDanceHandler();
            abstractAnimationHandler.get(Minecraft.getMinecraft().thePlayer.getUniqueID()).stopAnimation();
        });
        this.handlers.put("Dab", (netty) -> {
            AbstractAnimationHandler abstractAnimationHandler = Hyperium.INSTANCE.getHandlers().getDabHandler();
            abstractAnimationHandler.get(Minecraft.getMinecraft().thePlayer.getUniqueID()).ensureAnimationFor(seconds);
            NettyClient client = NettyClient.getClient();
            if (client != null && netty) {
                client.write(ServerCrossDataPacket.build(new JsonHolder().put("type", "dab_update").put("dabbing", true)));
                Multithreading.runAsync(() -> {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    client.write(ServerCrossDataPacket.build(new JsonHolder().put("type", "dab_update").put("dabbing", false)));

                });
            }
        });
        this.cancel.put("Dab", () -> {
            AbstractAnimationHandler abstractAnimationHandler = Hyperium.INSTANCE.getHandlers().getDabHandler();
            abstractAnimationHandler.get(Minecraft.getMinecraft().thePlayer.getUniqueID()).stopAnimation();
        });
        this.handlers.put("Twerk", (netty) -> {
            AbstractAnimationHandler abstractAnimationHandler = Hyperium.INSTANCE.getHandlers().getTwerkDance();
            abstractAnimationHandler.get(Minecraft.getMinecraft().thePlayer.getUniqueID()).ensureAnimationFor(60);
            Hyperium.INSTANCE.getHandlers().getTwerkDance().getStates().put(UUIDUtil.getClientUUID(), System.currentTimeMillis());
            NettyClient client = NettyClient.getClient();
            if (client != null && netty)
                client.write(ServerCrossDataPacket.build(new JsonHolder().put("type", "twerk_dance")));
        });
        this.cancel.put("Twerk", () -> {
            AbstractAnimationHandler abstractAnimationHandler = Hyperium.INSTANCE.getHandlers().getTwerkDance();
            abstractAnimationHandler.get(Minecraft.getMinecraft().thePlayer.getUniqueID()).stopAnimation();
        });

        this.handlers.put("Fortnite Default Dance", (netty) -> {
            AbstractAnimationHandler abstractAnimationHandler = Hyperium.INSTANCE.getHandlers().getFortniteDefaultDance();
            abstractAnimationHandler.get(Minecraft.getMinecraft().thePlayer.getUniqueID()).ensureAnimationFor(60);

            Hyperium.INSTANCE.getHandlers().getFortniteDefaultDance().getStates().put(UUIDUtil.getClientUUID(), System.currentTimeMillis());
            NettyClient client = NettyClient.getClient();
            if (client != null && netty)
                client.write(ServerCrossDataPacket.build(new JsonHolder().put("type", "fortnite_default_dance")));
        });
        this.cancel.put("Fortnite Default Dance", () -> {
            AbstractAnimationHandler abstractAnimationHandler = Hyperium.INSTANCE.getHandlers().getFortniteDefaultDance();
            abstractAnimationHandler.get(Minecraft.getMinecraft().thePlayer.getUniqueID()).stopAnimation();

//            Hyperium.INSTANCE.getHandlers().getFortniteDefaultDance().getStates().put(UUIDUtil.getClientUUID(), System.currentTimeMillis() * 2);

        });

        this.handlers.put("T-Pose", (netty) -> {
            AbstractAnimationHandler abstractAnimationHandler = Hyperium.INSTANCE.getHandlers().getTPoseHandler();
            abstractAnimationHandler.get(Minecraft.getMinecraft().thePlayer.getUniqueID()).ensureAnimationFor(seconds);
            NettyClient client = NettyClient.getClient();
            if (client != null && netty) {
                client.write(ServerCrossDataPacket.build(new JsonHolder().put("type", "tpose_update").put("posing", true)));
                Multithreading.runAsync(() -> {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    client.write(ServerCrossDataPacket.build(new JsonHolder().put("type", "tpose_update").put("posing", false)));

                });
            }
        });
        this.cancel.put("T-Pose", () -> {
            AbstractAnimationHandler abstractAnimationHandler = Hyperium.INSTANCE.getHandlers().getTPoseHandler();
            abstractAnimationHandler.get(Minecraft.getMinecraft().thePlayer.getUniqueID()).stopAnimation();
        });

        if (Hyperium.INSTANCE.getCosmetics().getFlipCosmetic().isSelfUnlocked()) {
            this.handlers.put("Flip", (netty) -> {
                int state = Settings.flipType;
                Hyperium.INSTANCE.getHandlers().getFlipHandler().state(UUIDUtil.getClientUUID(), state);
                NettyClient client = NettyClient.getClient();
                if (client != null && netty) {
                    client.write(ServerCrossDataPacket.build(new JsonHolder().put("type", "flip_update").put("flip_state", state)));
                }

                Multithreading.runAsync(() -> {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (client != null && netty) {
                        client.write(ServerCrossDataPacket.build(new JsonHolder().put("type", "flip_update").put("flip_state", 0)));
                    }
                    Hyperium.INSTANCE.getHandlers().getFlipHandler().state(UUIDUtil.getClientUUID(), 0);


                });
                Hyperium.INSTANCE.getHandlers().getFlipHandler().resetTick();
            });
            this.cancel.put("Flip", () -> {
                Hyperium.INSTANCE.getHandlers().getFlipHandler().state(UUIDUtil.getClientUUID(), 0);

            });
        }

    }

    @Override
    protected void pack() {

    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (foc != null) {
            mc.displayGuiScreen(null);
            handlers.get(foc).accept(true);
        }

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        foc = null;
        int count = handlers.size();
        ScaledResolution current = ResolutionUtil.current();
        drawScaledText("Hyperium Dances", current.getScaledWidth() / 2, 15, 2, Color.WHITE.getRGB(), true, true);
        float radius = current.getScaledHeight() * 2 / 5;
        int centerY = current.getScaledHeight() / 2;
        int centerX = current.getScaledWidth() / 2;
        float i = 0;

        for (String s : handlers.keySet()) {
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(2848);
            GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
            GL11.glBegin(6);
            GlStateManager.resetColor();
            float startTheta = (float) (i / (float) count * Math.PI * 2);
            float endTheta = (float) ((i + 1) / (float) count * Math.PI * 2);
            float diff = endTheta - startTheta;

            int mouseDeltaX = mouseX - centerX;
            int mouseDeltaY = mouseY - centerY;
            double sqrt = Math.sqrt(Math.pow(mouseDeltaX, 2) + Math.pow(mouseDeltaY, 2));
            boolean hovered = false;

            if (sqrt <= radius) {
                double mouseTheta = MathHelper.atan2(mouseDeltaX, mouseDeltaY);
                if (mouseTheta < 0)
                    mouseTheta += Math.PI * 2;
                if (mouseTheta > startTheta && mouseTheta < endTheta) {
                    foc = s;
                    hovered = true;
                }
            }
            Color tmp = new Color(97, 132, 249, hovered ? 255 : 200);
            GlStateManager.color(tmp.getRed() / 255F, tmp.getGreen() / 255F, tmp.getBlue() / 255F, tmp.getAlpha() / 255F);

            //Center
            GL11.glVertex3d(centerX, centerY, 0);

            for (float j = 0; j <= 50; j++) {
                float x = centerX + radius * MathHelper.sin(startTheta + (diff * j / 50F));
                float y = centerY + radius * MathHelper.cos(startTheta + (diff * j / 50F));
                GL11.glVertex2f(x, y);
            }
            GL11.glEnd();
            GL11.glEnable(3553);
            GL11.glDisable(3042);
            GL11.glDisable(2848);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            float middle = (startTheta + endTheta) / 2;
            List<String> strings = fontRendererObj.listFormattedStringToWidth(s, 50);
            int textCenterX = (int) (centerX + radius * MathHelper.sin(middle) / 3 * 2);
            int textCenterY = (int) (centerY + radius * MathHelper.cos(middle) / 3 * 2);
            textCenterY -= strings.size() * 15;
            for (String string : strings) {
                textCenterY += 15;
                drawScaledText(string, textCenterX, textCenterY, 1.5F, hovered ? Color.YELLOW.getRGB() : Color.GRAY.getRGB(), true, true);

            }
            GL11.glPopMatrix();
            i++;
        }

        if (lastFoc == null && foc != null) {
            handlers.get(foc).accept(false);
        } else if (lastFoc != null && foc == null) {
            cancel.get(lastFoc).run();
        } else if (lastFoc != null && !lastFoc.equalsIgnoreCase(foc)) {
            cancel.get(lastFoc).run();
            handlers.get(foc).accept(false);
        }
        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1);

        GlStateManager.translate(current.getScaledWidth() / 2, current.getScaledHeight() / 2, 5);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableAlpha();

        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();

        GlStateManager.translate(0, 50, 0);

        float v = 3000F;
        GlStateManager.rotate(System.currentTimeMillis() % (int) v / v * 360F, 0, 1.0F, 0);
        GlStateManager.translate(0, 0, -50);
        GuiInventory.drawEntityOnScreen(0, 0, 50, 0, 0, Minecraft.getMinecraft().thePlayer);
        GlStateManager.depthFunc(515);
        GlStateManager.resetColor();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableDepth();
        GlStateManager.popMatrix();
        lastFoc = foc;
    }
}
