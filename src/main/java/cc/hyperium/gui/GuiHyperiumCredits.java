package cc.hyperium.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GuiHyperiumCredits extends GuiScreen {

    private ArrayList<String> devList;
    private ArrayList<String> contribList;
    private ArrayList<String> supportList;
    private GuiScreen prevGui;

    public GuiHyperiumCredits(GuiScreen prevGui) {
        this.prevGui = prevGui;
    }

    @Override
    public void initGui() {
        devList = new ArrayList<>();
        contribList = new ArrayList<>();
        supportList = new ArrayList<>();
        devList.add("Kevin");
        devList.add("Sk1er");
        devList.add("BoomBoomPower");
        devList.add("Cube");
        devList.add("CoalOres");

        contribList.add("9Y0");
        contribList.add("BugFroggy");
        contribList.add("Disregard");
        contribList.add("FalseHonesty");
        contribList.add("KerbyBit");
        contribList.add("KodingKing");
        contribList.add("Vatuu Komalia");

        supportList.add("Deactivation");
        supportList.add("KenWay");
        supportList.add("Zezzo");

        Collections.sort(devList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.length() - o1.length();
            }
        });
        Collections.sort(contribList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.length() - o1.length();
            }
        });
        Collections.sort(supportList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.length() - o1.length();
            }
        });

        Method loadShaderMethod = null;
        try {
            loadShaderMethod = EntityRenderer.class.getDeclaredMethod("loadShader", ResourceLocation.class);
        } catch (NoSuchMethodException e) {
            try {
                loadShaderMethod = EntityRenderer.class.getDeclaredMethod("a", ResourceLocation.class);
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            }
        }

        if (loadShaderMethod != null) {
            loadShaderMethod.setAccessible(true);
            try {
                loadShaderMethod.invoke(Minecraft.getMinecraft().entityRenderer, new ResourceLocation("shaders/hyperium_extra_blur.json"));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        int yStart = 60;
        int dY = yStart;
        int cY = yStart;
        int sY = yStart;
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0, 0, 0, 170).getRGB());
//        drawChromaString("Press \"esc\" to exit.", sr.getScaledWidth() / 2 - fr.getStringWidth("Press \"esc\" to exit.") / 2, 25);

        GlStateManager.scale(2, 2, 2);
        drawChromaString("Developers", (int) (sr.getScaledWidth() / 4 - fr.getStringWidth("Developers")) / 2, 20);
        drawChromaString("Contributors", (int) ((sr.getScaledWidth() / 4 - fr.getStringWidth("Contributor")) + sr.getScaledWidth() / 4) / 2, 20);
        drawChromaString("Support Team", (int) (((sr.getScaledWidth() / 4 - fr.getStringWidth("Support Team")) + sr.getScaledWidth() / 2)) / 2, 20);
        GlStateManager.scale(0.5, 0.5, 0.5);

        for (String line : devList) {
            drawChromaString(line, (int) ((sr.getScaledWidth() / 2 - fr.getStringWidth(line) / 2) - sr.getScaledWidth() / 4), dY);
            dY += fr.FONT_HEIGHT + 1;
        }
        for (String line : contribList) {
            drawChromaString(line, sr.getScaledWidth() / 2 - fr.getStringWidth(line) / 2, cY);
            cY += fr.FONT_HEIGHT + 1;
        }
        for (String line : supportList) {
            drawChromaString(line, (int) ((sr.getScaledWidth() / 2 - fr.getStringWidth(line) / 2) + sr.getScaledWidth() / 4), sY);
            sY += fr.FONT_HEIGHT + 1;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawChromaString(String text, int x, int y) {
        int xInc = x;
        FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
        for (char c : text.toCharArray()) {
            long dif = (xInc * 10) - (y * 10);
            long l = System.currentTimeMillis() - dif;
            float ff = 2000.0F;
            int i = Color.HSBtoRGB((float) (l % (int) ff) / ff, 0.8F, 0.8F);
            String tmp = String.valueOf(c);
            renderer.drawString(tmp, (float) ((double) xInc / 1), (float) ((double) y / 1), i, true);
            xInc += (double) renderer.getCharWidth(c) * 1;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE)
            Minecraft.getMinecraft().displayGuiScreen(prevGui);
        super.keyTyped(typedChar, keyCode);
    }
}
