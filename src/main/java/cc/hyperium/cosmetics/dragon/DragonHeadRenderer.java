package cc.hyperium.cosmetics.dragon;


import cc.hyperium.Hyperium;
import cc.hyperium.cosmetics.DragonCosmetic;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderPlayerEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class DragonHeadRenderer extends ModelBase {
    private Minecraft mc;
    private ModelRenderer jaw;
    private ModelRenderer head;
    private boolean playerUsesFullHeight;
    private DragonCosmetic dragonCosmetic;
    private ResourceLocation selectedLoc;
    private EntityPlayer player;

    public DragonHeadRenderer(DragonCosmetic cosmetic) {
        this.dragonCosmetic = cosmetic;
        selectedLoc = new ResourceLocation("textures/entity/enderdragon/dragon.png");
        this.mc = Minecraft.getMinecraft();
        float f = -16.0F;
        this.textureWidth = 256;
        this.textureHeight = 256;
        this.setTextureOffset("body.body", 0, 0);
        this.setTextureOffset("wing.skin", -56, 88);
        this.setTextureOffset("wingtip.skin", -56, 144);
        this.setTextureOffset("rearleg.main", 0, 0);
        this.setTextureOffset("rearfoot.main", 112, 0);
        this.setTextureOffset("rearlegtip.main", 196, 0);
        this.setTextureOffset("head.upperhead", 112, 30);
        this.setTextureOffset("wing.bone", 112, 88);
        this.setTextureOffset("head.upperlip", 176, 44);
        this.setTextureOffset("jaw.jaw", 176, 65);
        this.setTextureOffset("frontleg.main", 112, 104);
        this.setTextureOffset("wingtip.bone", 112, 136);
        this.setTextureOffset("frontfoot.main", 144, 104);
        this.setTextureOffset("neck.box", 192, 104);
        this.setTextureOffset("frontlegtip.main", 226, 138);
        this.setTextureOffset("body.scale", 220, 53);
        this.setTextureOffset("head.scale", 0, 0);
        this.setTextureOffset("neck.scale", 48, 0);
        this.setTextureOffset("head.nostril", 112, 0);
        this.head = new ModelRenderer(this, "head");
        this.head.addBox("upperlip", -6.0F, -1.0F, -8.0F + f, 12, 5, 16);
        this.head.addBox("upperhead", -8.0F, -8.0F, 6.0F + f, 16, 16, 16);
        this.head.mirror = true;
        this.head.addBox("scale", -5.0F, -12.0F, 12.0F + f, 2, 4, 6);
        this.head.addBox("nostril", -5.0F, -3.0F, -6.0F + f, 2, 2, 4);
        this.head.mirror = false;
        this.head.addBox("scale", 3.0F, -12.0F, 12.0F + f, 2, 4, 6);
        this.head.addBox("nostril", 3.0F, -3.0F, -6.0F + f, 2, 2, 4);
        this.jaw = new ModelRenderer(this, "jaw");
        this.jaw.setRotationPoint(0.0F, 4.0F, 8.0F + f);
        this.jaw.addBox("jaw", -6.0F, 0.0F, -16.0F, 12, 4, 16);
        this.head.addChild(this.jaw);

    }

    @InvokeEvent
    private void onRenderPlayer(final RenderPlayerEvent event) {
        player = event.getEntity();
        if (dragonCosmetic.isPurchasedBy(event.getEntity().getUniqueID()) || Hyperium.INSTANCE.isDevEnv() && player.equals((Object) this.mc.thePlayer) && !player.isInvisible()) {
            this.renderHead(player, event.getPartialTicks());
        }
    }
    @InvokeEvent
    private void onTick(final TickEvent event) {
        if(player != null) {
            if(player.isAirBorne) {
                jaw.rotationPointY = jaw.rotationPointY + 50;
            }
            jaw.rotationPointY = jaw.rotationPointY - 50;
        }
    }


    private void renderHead(final EntityPlayer player, final float partialTicks) {
        final double scale = 1.0F;
        final double rotate = this.interpolate(player.rotationYawHead, player.prevRotationYaw, partialTicks);
        final double rotate1 = this.interpolate(player.prevRotationPitch, player.rotationPitch, partialTicks);

        GL11.glPushMatrix();
        GL11.glScaled(-scale, -scale, scale);
        GL11.glRotated(180.0 + rotate, 0.0, 1.0, 0.0);

        GL11.glTranslated(0.0, -(player.height - .4) / scale, 0.0);
        GlStateManager.translate(0.0D, 0.0D, .05 / scale);
        GL11.glRotated(rotate1, 1.0D, 0.0D, 0.0D);
        GL11.glTranslated(0.0, -0.3 / scale, .06);
        if (player.isSneaking()) {
            GL11.glTranslated(0.0, 0.125 / scale, 0.0);
        }
        final float[] colors = new float[]{1.0f, 1.0f, 1.0f};


        GL11.glColor3f(colors[0], colors[1], colors[2]);
        this.mc.getTextureManager().bindTexture(this.selectedLoc);
        GL11.glScaled(.4, .4, .4);
        this.head.render(.1F);

        GL11.glCullFace(1029);
        GL11.glDisable(2884);
        GL11.glColor3f(255.0f, 255.0f, 255.0f);
        GL11.glPopMatrix();
    }

    private float interpolate(final float yaw1, final float yaw2, final float percent) {
        float f = (yaw1 + (yaw2 - yaw1) * percent) % 360.0f;
        if (f < 0.0f) {
            f += 360.0f;
        }
        return f;
    }
}
