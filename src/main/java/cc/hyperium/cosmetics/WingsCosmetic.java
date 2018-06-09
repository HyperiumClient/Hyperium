package cc.hyperium.cosmetics;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderPlayerEvent;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * @author ConorTheDev
 */
public class WingsCosmetic extends AbstractCosmetic {

    private ResourceLocation dragonwingsloc = new ResourceLocation("textures/cosmetics/dragonwings.png");
    private boolean isPurchased = false;
    private PurchaseApi api = PurchaseApi.getInstance();
    private EntityPlayerMP player;
    private ModelRenderer wing;
    private ModelRenderer wingTip;


    public WingsCosmetic(boolean selfOnly, EnumPurchaseType purchaseType, boolean purchaseable) {
        super(selfOnly, EnumPurchaseType.WING_COSMETIC, true);

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

    private void getWingPurchased() {
        HyperiumPurchase purchase = api.getPackageSync(player.getUniqueID());

        if (purchase.hasPurchased(EnumPurchaseType.WING_COSMETIC)) {
            isPurchased = true;
        }

    }

    private void renderWings(EntityPlayer player, Float partialTicks) {
        double wingsscale = 0.7;
        double wingsrotate = interpol(player.renderOffsetX, player.renderOffsetY, player.renderOffsetZ);

        GL11.glPushMatrix();
        GL11.glScaled(-wingsscale, -wingsscale, wingsscale);
        GL11.glRotated(180.0 + wingsrotate, 0.0, 1.0, 0.0);
        GL11.glTranslated(0.0, -1.25 / wingsrotate, 0.0);
        GL11.glTranslated(0.0, 0.0, 0.2 / wingsscale);
    }

    private float interpol(float yaw, float yaw1, float percentage){
        float f = (yaw + (yaw1 - yaw) * percentage) % 360.0f;
        if (f < 0.0f) {
            f += 360.0f;
        }
        return f;
    }

    @InvokeEvent
    private void onRenderPlayer(RenderPlayerEvent e) {

    }
}
