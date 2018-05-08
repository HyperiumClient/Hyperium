package cc.hyperium.handlers.handlers.animation;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.SpawnpointChangeEvent;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.MathHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CapeHandler {

    private ConcurrentHashMap<UUID, HyperiumCape> capes = new ConcurrentHashMap<>();
    private TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
    private ModelRenderer cape;

    private int getCapeRenderID(UUID uuid) {

        return capes.computeIfAbsent(uuid, uuid1 -> {
            loadCapesAsync(uuid);
            return new HyperiumCape(-1);
        }).get(0);


    }

    @InvokeEvent
    public void swap(SpawnpointChangeEvent event) {
        capes.clear();
    }

    public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale, RenderPlayer playerRenderer) {
        if (entitylivingbaseIn.hasPlayerInfo() && !entitylivingbaseIn.isInvisible() && entitylivingbaseIn.isWearing(EnumPlayerModelParts.CAPE) && entitylivingbaseIn.getLocationCape() != null) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            playerRenderer.bindTexture(entitylivingbaseIn.getLocationCape());
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 0.0F, 0.125F);
            double d0 = entitylivingbaseIn.prevChasingPosX + (entitylivingbaseIn.chasingPosX - entitylivingbaseIn.prevChasingPosX) * (double) partialTicks - (entitylivingbaseIn.prevPosX + (entitylivingbaseIn.posX - entitylivingbaseIn.prevPosX) * (double) partialTicks);
            double d1 = entitylivingbaseIn.prevChasingPosY + (entitylivingbaseIn.chasingPosY - entitylivingbaseIn.prevChasingPosY) * (double) partialTicks - (entitylivingbaseIn.prevPosY + (entitylivingbaseIn.posY - entitylivingbaseIn.prevPosY) * (double) partialTicks);
            double d2 = entitylivingbaseIn.prevChasingPosZ + (entitylivingbaseIn.chasingPosZ - entitylivingbaseIn.prevChasingPosZ) * (double) partialTicks - (entitylivingbaseIn.prevPosZ + (entitylivingbaseIn.posZ - entitylivingbaseIn.prevPosZ) * (double) partialTicks);
            float f = entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks;
            double d3 = (double) MathHelper.sin(f * (float) Math.PI / 180.0F);
            double d4 = (double) (-MathHelper.cos(f * (float) Math.PI / 180.0F));
            float f1 = (float) d1 * 10.0F;
            f1 = MathHelper.clamp_float(f1, -6.0F, 32.0F);
            float f2 = (float) (d0 * d3 + d2 * d4) * 100.0F;
            float f3 = (float) (d0 * d4 - d2 * d3) * 100.0F;

            if (f2 < 0.0F) {
                f2 = 0.0F;
            }  if (f2 > 170)
                f2 = 170;


            float f4 = entitylivingbaseIn.prevCameraYaw + (entitylivingbaseIn.cameraYaw - entitylivingbaseIn.prevCameraYaw) * partialTicks;
            f1 = f1 + MathHelper.sin((entitylivingbaseIn.prevDistanceWalkedModified + (entitylivingbaseIn.distanceWalkedModified - entitylivingbaseIn.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * f4;

            if (entitylivingbaseIn.isSneaking()) {
                f1 += 25.0F;
            }

            GlStateManager.rotate(6.0F + f2 / 2.0F + f1, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(f3 / 2.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(-f3 / 2.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            playerRenderer.getMainModel().renderCape(0.0625F);
            GlStateManager.popMatrix();
        }


    }

    private void loadCapesAsync(UUID uuid) {
        Multithreading.runAsync(() -> {
            URL url = null;
            try {
                url = new URL("https://i.imgur.com/HHYKBYq.png");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setUseCaches(true);
                connection.addRequestProperty("User-Agent", "Mozilla/4.76 Hyperium ");
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);
                connection.setDoOutput(true);
                InputStream is = connection.getInputStream();


                BufferedImage img = ImageIO.read(ImageIO.createImageInputStream(is));

                capes.put(uuid, new HyperiumCape(img));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    class HyperiumCape {
        int cape;
        int index;
        BufferedImage image;

        public HyperiumCape(int cape) {
            this.cape = cape;
        }

        public HyperiumCape(BufferedImage image) {
            this.image = image;
        }

        public int get(int index) {
            if (image != null) {
                DynamicTexture dynamicTexture = new DynamicTexture(image);
                this.cape = dynamicTexture.getGlTextureId();
            }
            return cape;
        }
    }
}
