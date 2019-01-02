package cc.hyperium.gui.playerrenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class GuiPlayerRenderer {

    public static final FakePlayerRendering PLAYER_RENDERER = new FakePlayerRendering(Minecraft.getMinecraft().getSession().getProfile());

    public static void renderPlayer(int x, int y) {

        GlStateManager.pushMatrix();
        PLAYER_RENDERER.renderPlayerModel(x + 80, y + 100, 55, 0);
        GlStateManager.popMatrix();

    }

    public static void renderPlayerWithRotation(int x, int y, float givenRotation) {

        GlStateManager.pushMatrix();
        PLAYER_RENDERER.renderPlayerModel(x + 80, y + 100, 55, givenRotation);
        GlStateManager.popMatrix();

    }

}
