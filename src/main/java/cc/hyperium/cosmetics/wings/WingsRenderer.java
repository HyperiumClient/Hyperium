package cc.hyperium.cosmetics.wings;


import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderPlayerEvent;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class WingsRenderer extends ModelBase {

  private Minecraft mc;
  private ModelRenderer wing;
  private ModelRenderer wingTip;
  private boolean playerUsesFullHeight;
  private WingsCosmetic wingsCosmetic;

  public WingsRenderer(WingsCosmetic cosmetic) {
    this.wingsCosmetic = cosmetic;
    this.mc = Minecraft.getMinecraft();
    this.setTextureOffset("wing.bone", 0, 0);
    this.setTextureOffset("wing.skin", -10, 8);
    this.setTextureOffset("wingtip.bone", 0, 5);
    this.setTextureOffset("wingtip.skin", -10, 18);
    (this.wing = new ModelRenderer((ModelBase) this, "wing")).setTextureSize(30, 30);
    this.wing.setRotationPoint(-2.0f, 0.0f, 0.0f);
    this.wing.addBox("bone", -10.0f, -1.0f, -1.0f, 10, 2, 2);
    this.wing.addBox("skin", -10.0f, 0.0f, 0.5f, 10, 0, 10);
    (this.wingTip = new ModelRenderer((ModelBase) this, "wingtip")).setTextureSize(30, 30);
    this.wingTip.setRotationPoint(-10.0f, 0.0f, 0.0f);
    this.wingTip.addBox("bone", -10.0f, -0.5f, -0.5f, 10, 1, 1);
    this.wingTip.addBox("skin", -10.0f, 0.0f, 0.5f, 10, 0, 10);
    this.wing.addChild(this.wingTip);
  }

  @InvokeEvent
  public void onRenderPlayer(RenderPlayerEvent event) {
    final EntityPlayer player = event.getEntity();
    if (wingsCosmetic.isPurchasedBy(event.getEntity().getUniqueID()) && !player.isInvisible()) {
      this.renderWings(player, event.getPartialTicks(), event.getX(), event.getY(), event.getZ());
    }
  }

  private void renderWings(EntityPlayer player, float partialTicks, double x, double y, double z) {
    HyperiumPurchase packageIfReady = PurchaseApi.getInstance()
        .getPackageIfReady(player.getUniqueID());
    if (packageIfReady == null) {
      return;
    }
    String s = packageIfReady.getPurchaseSettings().optJSONObject("wings").optString("type");
    ResourceLocation location = wingsCosmetic.getLocation(s);

    // Wings scale as defined in the settings.
    double v = packageIfReady.getPurchaseSettings().optJSONObject("wings")
        .optDouble("scale", Settings.WINGS_SCALE);
    double scale = v / 100.0;
    double rotate = this
        .interpolate(player.prevRenderYawOffset, player.renderYawOffset, partialTicks);

    GlStateManager.pushMatrix();
    GlStateManager.translate(x, y, z);


    GlStateManager.scale(-scale, -scale, scale);
    GlStateManager.rotate((float) (180.0F + rotate), 0.0F, 1.0F, 0.0F);

    float scaledHeight = (float) ((this.playerUsesFullHeight ? 1.45 : 1.25) / scale);

    GlStateManager.translate(0.0, -scaledHeight, 0.0);
    GlStateManager.translate(0.0, 0.0, 0.1 / scale);
    // Takes into account player flip cosmetic.
    int rotateState = Hyperium.INSTANCE.getHandlers().getFlipHandler().getSelf();

    if (player.isSneaking() && rotateState == 0) {
      GlStateManager.translate(0.0, 0.125 / scale, 0.0);
    } else if(player.isSneaking() && rotateState == 1){
      GlStateManager.translate(0.0, -0.125 / scale, 0.0);
    }


    if (rotateState == 2) {
      // Spinning rotate mode.
      GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
      double l = System.currentTimeMillis() % (360 * 1.75) / 1.75;
      GlStateManager.rotate((float) l, 0.1F, 0.0F, 0.0F);
      GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.translate(0.0F, - scaledHeight + 1, 0.0F);
    } else if (rotateState == 1) {
      // Flip rotate mode.
      GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.translate(0.0F,  -scaledHeight + 0.5, 0.0F);
    }

    GlStateManager.color(1.0F, 1.0F, 1.0F);

    this.mc.getTextureManager().bindTexture(location);

    for (int j = 0; j < 2; ++j) {
      GL11.glEnable(2884);
      float f11 = System.currentTimeMillis() % 1000L / 1000.0f * 3.1415927f * 2.0f;
      this.wing.rotateAngleX = (float) Math.toRadians(-80.0) - (float) Math.cos(f11) * 0.2f;
      this.wing.rotateAngleY = (float) Math.toRadians(20.0) + (float) Math.sin(f11) * 0.4f;
      this.wing.rotateAngleZ = (float) Math.toRadians(20.0);
      this.wingTip.rotateAngleZ = -(float) (Math.sin(f11 + 2.0f) + 0.5) * 0.75f;
      this.wing.render(0.0625f);
      GlStateManager.scale(-1.0f, 1.0f, 1.0f);
      if (j == 0) {
        GL11.glCullFace(1028);
      }
    }
    GL11.glCullFace(1029);
    GL11.glDisable(2884);
    GL11.glPopMatrix();
  }

  public float interpolate(float yaw1, float yaw2, final float percent) {
    float f = (yaw1 + (yaw2 - yaw1) * percent) % 360.0f;
    if (f < 0.0f) {
      f += 360.0f;
    }
    return f;
  }
}
