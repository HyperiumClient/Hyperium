package cc.hyperium.handlers.handlers.cloud;

import cc.hyperium.config.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;

/**
 * This was all done by @Zaggy1024 on Github (https://github.com/Zaggy1024)
 * <p>
 * It was originally created for Forge 1.12.x, but I (asbyth) decided to port it to Hyperium for
 * performance reasons. All credit is due to them for this
 * <p>
 * Link to the PR adding this in Forge (https://github.com/MinecraftForge/MinecraftForge/pull/4143)
 */
public class CloudRenderer implements IResourceManagerReloadListener {

  private static final float PX_SIZE = 1 / 256F;
  private static final VertexFormat FORMAT = DefaultVertexFormats.POSITION_TEX_COLOR;
  private static final int TOP_SECTIONS = 12;
  private static final int HEIGHT = 4;
  private static final float INSET = 0.001F;
  private static final float ALPHA = 0.8F;

  public static boolean WIREFRAME = false;

  private final Minecraft mc = Minecraft.getMinecraft();
  private final ResourceLocation texture = new ResourceLocation("textures/environment/clouds.png");

  private int displayList = -1;
  private VertexBuffer vbo;
  private int cloudMode = -1;
  private int renderDistance = -1;

  private DynamicTexture COLOR_TEX = null;

  private int texW;
  private int texH;

  public CloudRenderer() {
    ((IReloadableResourceManager) mc.getResourceManager()).registerReloadListener(this);
  }

  private int getScale() {
    return cloudMode == 2 ? 12 : 8;
  }

  private float ceilToScale(float value) {
    float scale = getScale();
    return MathHelper.ceiling_float_int(value / scale) * scale;
  }

  private void vertices(WorldRenderer buffer) {
    boolean fancy = cloudMode == 2;
    float scale = getScale();
    float CULL_DIST = 2 * scale;
    float bCol = fancy ? 0.7F : 1.0F;
    float sectEnd = ceilToScale((renderDistance << 1) << 4);
    float sectStart = -sectEnd;
    float sectStep = ceilToScale(sectEnd * 2 / TOP_SECTIONS);
    float sectPx = PX_SIZE / scale;

    buffer.begin(GL11.GL_QUADS, FORMAT);

    float sectX0 = sectStart;
    float sectX1 = sectX0;

    while (sectX1 < sectEnd) {
      sectX1 += sectStep;

      if (sectX1 > sectEnd) {
        sectX1 = sectEnd;
      }

      float sectZ0 = sectStart;
      float sectZ1 = sectZ0;

      while (sectZ1 < sectEnd) {
        sectZ1 += sectStep;

        if (sectZ1 > sectEnd) {
          sectZ1 = sectEnd;
        }

        float u0 = sectX0 * sectPx;
        float u1 = sectX1 * sectPx;
        float v0 = sectZ0 * sectPx;
        float v1 = sectZ1 * sectPx;

        buffer.pos(sectX0, 0, sectZ0).tex(u0, v0).color(bCol, bCol, bCol, ALPHA).endVertex();
        buffer.pos(sectX1, 0, sectZ0).tex(u1, v0).color(bCol, bCol, bCol, ALPHA).endVertex();
        buffer.pos(sectX1, 0, sectZ1).tex(u1, v1).color(bCol, bCol, bCol, ALPHA).endVertex();
        buffer.pos(sectX0, 0, sectZ1).tex(u0, v1).color(bCol, bCol, bCol, ALPHA).endVertex();

        if (fancy) {
          buffer.pos(sectX0, HEIGHT, sectZ0).tex(u0, v0).color(1, 1, 1, ALPHA).endVertex();
          buffer.pos(sectX0, HEIGHT, sectZ1).tex(u0, v1).color(1, 1, 1, ALPHA).endVertex();
          buffer.pos(sectX1, HEIGHT, sectZ1).tex(u1, v1).color(1, 1, 1, ALPHA).endVertex();
          buffer.pos(sectX1, HEIGHT, sectZ0).tex(u1, v0).color(1, 1, 1, ALPHA).endVertex();

          float slice;
          float sliceCoord0;
          float sliceCoord1;

          for (slice = sectX0; slice < sectX1; ) {
            sliceCoord0 = slice * sectPx;
            sliceCoord1 = sliceCoord0 + PX_SIZE;

            // X sides
            if (slice > -CULL_DIST) {
              slice += INSET;
              buffer.pos(slice, 0, sectZ1).tex(sliceCoord0, v1).color(0.9F, 0.9F, 0.9F, ALPHA)
                  .endVertex();
              buffer.pos(slice, HEIGHT, sectZ1).tex(sliceCoord1, v1).color(0.9F, 0.9F, 0.9F, ALPHA)
                  .endVertex();
              buffer.pos(slice, HEIGHT, sectZ0).tex(sliceCoord1, v0).color(0.9F, 0.9F, 0.9F, ALPHA)
                  .endVertex();
              buffer.pos(slice, 0, sectZ0).tex(sliceCoord0, v0).color(0.9F, 0.9F, 0.9F, ALPHA)
                  .endVertex();
              slice -= INSET;
            }

            slice += scale;

            if (slice <= CULL_DIST) {
              slice -= INSET;
              buffer.pos(slice, 0, sectZ0).tex(sliceCoord0, v0).color(0.9F, 0.9F, 0.9F, ALPHA)
                  .endVertex();
              buffer.pos(slice, HEIGHT, sectZ0).tex(sliceCoord1, v0).color(0.9F, 0.9F, 0.9F, ALPHA)
                  .endVertex();
              buffer.pos(slice, HEIGHT, sectZ1).tex(sliceCoord1, v1).color(0.9F, 0.9F, 0.9F, ALPHA)
                  .endVertex();
              buffer.pos(slice, 0, sectZ1).tex(sliceCoord0, v1).color(0.9F, 0.9F, 0.9F, ALPHA)
                  .endVertex();
              slice += INSET;
            }
          }

          for (slice = sectZ0; slice < sectZ1; ) {
            sliceCoord0 = slice * sectPx;
            sliceCoord1 = sliceCoord0 + PX_SIZE;

            // Z sides
            if (slice > -CULL_DIST) {
              slice += INSET;
              buffer.pos(sectX0, 0, slice).tex(u0, sliceCoord0).color(0.8F, 0.8F, 0.8F, ALPHA)
                  .endVertex();
              buffer.pos(sectX0, HEIGHT, slice).tex(u0, sliceCoord1).color(0.8F, 0.8F, 0.8F, ALPHA)
                  .endVertex();
              buffer.pos(sectX1, HEIGHT, slice).tex(u1, sliceCoord1).color(0.8F, 0.8F, 0.8F, ALPHA)
                  .endVertex();
              buffer.pos(sectX1, 0, slice).tex(u1, sliceCoord0).color(0.8F, 0.8F, 0.8F, ALPHA)
                  .endVertex();
              slice -= INSET;
            }

            slice += scale;

            if (slice <= CULL_DIST) {
              slice -= INSET;
              buffer.pos(sectX1, 0, slice).tex(u1, sliceCoord0).color(0.8F, 0.8F, 0.8F, ALPHA)
                  .endVertex();
              buffer.pos(sectX1, HEIGHT, slice).tex(u1, sliceCoord1).color(0.8F, 0.8F, 0.8F, ALPHA)
                  .endVertex();
              buffer.pos(sectX0, HEIGHT, slice).tex(u0, sliceCoord1).color(0.8F, 0.8F, 0.8F, ALPHA)
                  .endVertex();
              buffer.pos(sectX0, 0, slice).tex(u0, sliceCoord0).color(0.8F, 0.8F, 0.8F, ALPHA)
                  .endVertex();
              slice += INSET;
            }
          }
        }

        sectZ0 = sectZ1;
      }

      sectX0 = sectX1;
    }
  }

  private void dispose() {
    if (vbo != null) {
      vbo.deleteGlBuffers();
      vbo = null;
    }

    if (displayList >= 0) {
      GLAllocation.deleteDisplayLists(displayList);
      displayList = -1;
    }
  }

  private void build() {
    Tessellator tess = Tessellator.getInstance();
    WorldRenderer buffer = tess.getWorldRenderer();

    if (OpenGlHelper.useVbo()) {
      vbo = new VertexBuffer(FORMAT);
    } else {
      GL11.glNewList(displayList = GLAllocation.generateDisplayLists(1), GL11.GL_COMPILE);
    }

    vertices(buffer);

    if (OpenGlHelper.useVbo()) {
      buffer.finishDrawing();
      buffer.reset();
      vbo.bufferData(buffer.getByteBuffer());
    } else {
      tess.draw();
      GL11.glEndList();
    }
  }

  private int fullCoord(double coord, int scale) {
    return ((int) coord / scale) - (coord < 0 ? 1 : 0);
  }

  private boolean isBuilt() {
    return OpenGlHelper.useVbo() ? vbo != null : displayList >= 0;
  }

  public void checkSettings() {
    boolean newEnabled = Settings.GPU_CLOUD_RENDERER
        && mc.gameSettings.shouldRenderClouds() != 0
        && mc.theWorld != null
        && mc.theWorld.provider.isSurfaceWorld();

    if (isBuilt() && (!newEnabled || mc.gameSettings.shouldRenderClouds() != cloudMode
        || mc.gameSettings.renderDistanceChunks != renderDistance)) {
      dispose();
    }

    cloudMode = mc.gameSettings.shouldRenderClouds();
    renderDistance = mc.gameSettings.renderDistanceChunks;

    if (newEnabled && !isBuilt()) {
      build();
    }
  }

  public boolean render(int cloudTicks, float partialTicks) {
    if (!isBuilt()) {
      return false;
    }

    Entity entity = mc.getRenderViewEntity();

    double totalOffset = cloudTicks + partialTicks;

    double x =
        entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks + totalOffset * 0.03;
    double y = mc.theWorld.provider.getCloudHeight() - (entity.lastTickPosY
        + (entity.posY - entity.lastTickPosY) * partialTicks) + 0.33;
    double z = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;

    int scale = getScale();

    if (cloudMode == 2) {
      z += 0.33 * scale;
    }

    int offU = fullCoord(x, scale);
    int offV = fullCoord(z, scale);

    GlStateManager.pushMatrix();
    GlStateManager.translate((offU * scale) - x, y, (offV * scale) - z);
    offU = offU % texW;
    offV = offV % texH;

    GlStateManager.matrixMode(GL11.GL_TEXTURE);
    GlStateManager.translate(offU * PX_SIZE, offV * PX_SIZE, 0);
    GlStateManager.matrixMode(GL11.GL_MODELVIEW);

    GlStateManager.disableCull();

    GlStateManager.enableBlend();
    GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE,
        GL11.GL_ZERO);
    Vec3 color = mc.theWorld.getCloudColour(partialTicks);

    float r = (float) color.xCoord;
    float g = (float) color.yCoord;
    float b = (float) color.zCoord;

    if (mc.gameSettings.anaglyph) {
      float tempR = r * 0.3F + g * 0.59F + b * 0.11F;
      float tempG = r * 0.3F + g * 0.7F;
      float tempB = r * 0.3F + b * 0.7F;
      r = tempR;
      g = tempG;
      b = tempB;
    }

    if (COLOR_TEX == null) {
      COLOR_TEX = new DynamicTexture(1, 1);
    }

    COLOR_TEX.getTextureData()[0] = 255 << 24
        | ((int) (r * 255)) << 16
        | ((int) (g * 255)) << 8
        | (int) (b * 255);
    COLOR_TEX.updateDynamicTexture();

    GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
    GlStateManager.bindTexture(COLOR_TEX.getGlTextureId());
    GlStateManager.enableTexture2D();

    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    mc.getTextureManager().bindTexture(texture);

    ByteBuffer buffer = Tessellator.getInstance().getWorldRenderer().getByteBuffer();

    if (OpenGlHelper.useVbo()) {
      vbo.bindBuffer();

      int stride = FORMAT.getNextOffset();
      GL11.glVertexPointer(3, GL11.GL_FLOAT, stride, 0);
      GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
      GL11.glTexCoordPointer(2, GL11.GL_FLOAT, stride, 12);
      GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
      GL11.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, stride, 20);
      GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
    } else {
      buffer.limit(FORMAT.getNextOffset());
      for (int i = 0; i < FORMAT.getElementCount(); i++) {
        FORMAT.getElements().get(i).getUsage().preDraw(FORMAT, i, FORMAT.getNextOffset(), buffer);
      }

      buffer.position(0);
    }

    GlStateManager.colorMask(false, false, false, false);
    if (OpenGlHelper.useVbo()) {
      vbo.drawArrays(GL11.GL_QUADS);
    } else {
      GlStateManager.callList(displayList);
    }

    if (!mc.gameSettings.anaglyph) {
      GlStateManager.colorMask(true, true, true, true);
    } else {
      switch (EntityRenderer.anaglyphField) {
        case 0:
          GlStateManager.colorMask(false, true, true, true);
          break;
        case 1:
          GlStateManager.colorMask(true, false, false, true);
          break;
      }
    }

    if (WIREFRAME) {
      GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
      GL11.glLineWidth(2.0F);
      GlStateManager.disableTexture2D();
      GlStateManager.depthMask(false);
      GlStateManager.disableFog();
      if (OpenGlHelper.useVbo()) {
        vbo.drawArrays(GL11.GL_QUADS);
      } else {
        GlStateManager.callList(displayList);
      }
      GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
      GlStateManager.depthMask(true);
      GlStateManager.enableTexture2D();
      GlStateManager.enableFog();
    }

    if (OpenGlHelper.useVbo()) {
      vbo.drawArrays(GL11.GL_QUADS);
      vbo.unbindBuffer();
    } else {
      GlStateManager.callList(displayList);
    }

    buffer.limit(0);
    for (int i = 0; i < FORMAT.getElementCount(); i++) {
      FORMAT.getElements().get(i).getUsage().postDraw(FORMAT, i, FORMAT.getNextOffset(), buffer);
    }

    buffer.position(0);

    GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
    GlStateManager.disableTexture2D();
    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

    GlStateManager.matrixMode(GL11.GL_TEXTURE);
    GlStateManager.loadIdentity();
    GlStateManager.matrixMode(GL11.GL_MODELVIEW);

    GlStateManager.disableBlend();
    GlStateManager.enableCull();

    GlStateManager.popMatrix();

    return true;
  }

  private void reloadTextures() {
    if (mc.getTextureManager() != null) {
      mc.getTextureManager().bindTexture(texture);
      texW = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
      texH = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
    }
  }

  @Override
  public void onResourceManagerReload(IResourceManager resourceManager) {
    reloadTextures();
  }
}
