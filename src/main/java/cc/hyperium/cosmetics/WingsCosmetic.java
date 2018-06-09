package cc.hyperium.cosmetics;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderPlayerEvent;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.PurchaseApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * @author ConorTheDev
 */
public class WingsCosmetic extends AbstractCosmetic {

    private ResourceLocation angelwingsloc = new ResourceLocation("textures/cosmetics/angelwings.png");
    private ResourceLocation dragonwingsloc = new ResourceLocation("textures/cosmetics/dragonwings.png");
    private boolean isPurchased = false;
    private PurchaseApi api = PurchaseApi.getInstance();
    private EntityPlayerMP player;
    private ModelRenderer wing;
    private ModelRenderer wingTip;
    private boolean playerUsesFullHeight;
    private Minecraft mc;
    private AbstractCosmetic abstractCosmetic;


    public WingsCosmetic() {
        super(true, EnumPurchaseType.WING_COSMETIC, true);

        //wing mappings
        this.wing.setTextureSize(30, 30);
        this.wing.setRotationPoint(-2.0f, 0.0f, 0.0f);
        this.wing.addBox("bone", -10.0f, -1.0f, -1.0f, 10, 2, 2);
        this.wing.addBox("skin", -10.0f, 0.0f, 0.5f, 10, 0, 10);
        this.wingTip.setRotationPoint(-10.0f, 0.0f, 0.0f);
        this.wingTip.setTextureSize(30, 30);
        this.wingTip.addBox("skin", -10.0f, 0.0f, 0.5f, 10, 0, 10);
        this.wingTip.addBox("bone", -10.0f, -0.5f, -0.5f, 10, 1, 1);
        this.wing.addChild(this.wingTip);
        this.wing.setTextureOffset(0, 0);
        this.wing.setTextureOffset(-10, 8);
        this.wingTip.setTextureOffset(0, 5);
        this.wingTip.setTextureOffset(-10, 18);
    }



    private void renderWings(EntityPlayer player, Float partialTicks) {
        double wingsscale = 0.7;
        double wingsrotate = interpol(player.renderOffsetX, player.renderOffsetY, player.renderOffsetZ);

        GL11.glPushMatrix();
        GL11.glScaled(-wingsscale, -wingsscale, wingsscale);
        GL11.glRotated(180.0 + wingsrotate, 0.0, 1.0, 0.0);
        GL11.glTranslated(0.0, -(this.playerUsesFullHeight ? 1.45 : 1.25) / wingsscale, 0.0);
        GL11.glTranslated(0.0, 0.0, 0.2 / wingsscale);
        if (player.isSneaking()) {
            GL11.glTranslated(0.0, 0.125 / wingsscale, 0.0);
        }
        this.mc.getTextureManager().bindTexture(this.dragonwingsloc);
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

    private float interpol(float yaw, float yaw1, float percentage) {
        float f = (yaw + (yaw1 - yaw) * percentage) % 360.0f;
        if (f < 0.0f) {
            f += 360.0f;
        }
        return f;
    }

    @InvokeEvent
    public void onRenderPlayer(RenderPlayerEvent e) {
        final EntityPlayer player1 = e.getEntity();
        if (player1.equals((Object) this.mc.thePlayer) && !player.isInvisible()) {
            if (isPurchasedBy(player1.getUniqueID()))
                renderWings(player1, e.getPartialTicks());
        }
    }
}
