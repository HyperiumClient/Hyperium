package cc.hyperium.cosmetics.wings;


import cc.hyperium.Hyperium;
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
    public void onRenderPlayer(final RenderPlayerEvent event) {
        final EntityPlayer player = event.getEntity();
        if (wingsCosmetic.isPurchasedBy(event.getEntity().getUniqueID()) || Hyperium.INSTANCE.isDevEnv() && player.equals((Object) this.mc.thePlayer) && !player.isInvisible()) {
            this.renderWings(player, event.getPartialTicks(),event.getX(),event.getY(),event.getZ());
        }
    }

    private void renderWings(final EntityPlayer player, final float partialTicks, double x, double y, double z) {
        HyperiumPurchase packageIfReady = PurchaseApi.getInstance().getPackageIfReady(player.getUniqueID());
        if (packageIfReady == null ) {
            return;
        }
        String s = packageIfReady.getPurchaseSettings().optJSONObject("wings").optString("type");
        ResourceLocation location = wingsCosmetic.getLocation(s);

        final double scale = packageIfReady.getPurchaseSettings().optJSONObject("wings").optDouble("scale", this.wingsCosmetic.scale) / 100.0;
        final double rotate = this.interpolate(player.prevRenderYawOffset, player.renderYawOffset, partialTicks);
        GL11.glPushMatrix();
        GlStateManager.translate(x,y,z);
        GL11.glScaled(-scale, -scale, scale);
        GL11.glRotated(180.0 + rotate, 0.0, 1.0, 0.0);
        GL11.glTranslated(0.0, -(this.playerUsesFullHeight ? 1.45 : 1.25) / scale, 0.0);
        GL11.glTranslated(0.0, 0.0, 0.1 / scale);
        if (player.isSneaking()) {
            GL11.glTranslated(0.0, 0.125 / scale, 0.0);
        }
        final float[] colors = this.wingsCosmetic.getColours();
        GL11.glColor3f(colors[0], colors[1], colors[2]);


        this.mc.getTextureManager().bindTexture(location);

        for (int j = 0; j < 2; ++j) {
            GL11.glEnable(2884);
            final float f11 = System.currentTimeMillis() % 1000L / 1000.0f * 3.1415927f * 2.0f;
            this.wing.rotateAngleX = (float) Math.toRadians(-80.0) - (float) Math.cos(f11) * 0.2f;
            this.wing.rotateAngleY = (float) Math.toRadians(20.0) + (float) Math.sin(f11) * 0.4f;
            this.wing.rotateAngleZ = (float) Math.toRadians(20.0);
            this.wingTip.rotateAngleZ = -(float) (Math.sin(f11 + 2.0f) + 0.5) * 0.75f;
            this.wing.render(0.0625f);
            GL11.glScalef(-1.0f, 1.0f, 1.0f);
            if (j == 0) {
                GL11.glCullFace(1028);
            }
        }
        GL11.glCullFace(1029);
        GL11.glDisable(2884);
        GL11.glColor3f(255.0f, 255.0f, 255.0f);
        GL11.glPopMatrix();
    }

    public float interpolate(final float yaw1, final float yaw2, final float percent) {
        float f = (yaw1 + (yaw2 - yaw1) * percent) % 360.0f;
        if (f < 0.0f) {
            f += 360.0f;
        }
        return f;
    }
}
