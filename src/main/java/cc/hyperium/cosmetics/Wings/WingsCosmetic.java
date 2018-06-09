package cc.hyperium.cosmetics.Wings;

import cc.hyperium.cosmetics.AbstractCosmetic;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderPlayerEvent;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.PurchaseApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author ConorTheDev
 */
public class WingsCosmetic extends AbstractCosmetic {

    public int scale = 100;

    public WingsCosmetic() {
        super(true, EnumPurchaseType.WING_COSMETIC, true);
    }

    //add custom colours soon
    public float[] getColours() {
        return new float[] { 1.0f, 1.0f, 1.0f };
    }
}
