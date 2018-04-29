package cc.hyperium.gui;

import cc.hyperium.mods.chromahud.ElementRenderer;
import com.google.common.collect.Lists;
import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWinGame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.Charsets;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GuiCredits extends GuiScreen {

    private List<String> textList;
    private GuiScreen prevGui;

    public GuiCredits(GuiScreen prevGui) {
        this.prevGui = prevGui;
    }

    @Override
    public void initGui() {
        textList = new ArrayList<>();
//        try {
//            String result = "";
//            URL url = new URL("https://api.github.com/repos/HyperiumClient/Hyperium/contributors");
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String line;
//            while ((line = rd.readLine()) != null) {
//                result += line;
//            }
//            rd.close();
//            JsonParser jsonParser = new JsonParser();
//            JsonElement jsonElement = jsonParser.parse(result);
//            JsonArray jsonArray = jsonElement.getAsJsonArray();
//            for (Iterator<JsonElement> it = jsonArray.iterator(); it.hasNext(); ) {
//                JsonElement element = it.next();
//                textList.add(element.getAsJsonObject().get("login").getAsString());
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (ProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        textList.add("<----Developers---->");
        textList.add("Kevin");
        textList.add("Sk1er");
        textList.add("BoomBoomPower");
        textList.add("Cube");
        textList.add("<----Contrubitors---->");
        textList.add("9Y0");
        textList.add("BugFroggy");
        textList.add("Disregard");
        textList.add("FalseHonesty");
        textList.add("KerbyBit");
        textList.add("KodingKing");
        textList.add("Vatuu Komalia");
        textList.add("<----Support Team---->");
        textList.add("Deactivation");
        textList.add("KenWay");
        textList.add("Zezzo");

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        int y = 80;
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        drawChromaString("Press \"esc\" to exit.", sr.getScaledWidth() / 4 - fr.getStringWidth("Contributor") / 2, 10);
        GlStateManager.scale(2, 2, 2);
        drawChromaString("Contributors", sr.getScaledWidth() / 4 - fr.getStringWidth("Contributor") / 2, 30);
        GlStateManager.scale(0.5, 0.5, 0.5);
        for (String line : textList) {
            drawChromaString(line, sr.getScaledWidth() / 2 - fr.getStringWidth(line) / 2, y);
            y += fr.FONT_HEIGHT + 1;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawChromaString(String text, int x, int y) {
        FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
        for (char c : text.toCharArray()) {
            long dif = (x * 10) - (y * 10);
            long l = System.currentTimeMillis() - dif;
            float ff = 2000.0F;
            int i = Color.HSBtoRGB((float) (l % (int) ff) / ff, 0.8F, 0.8F);
            String tmp = String.valueOf(c);
            renderer.drawString(tmp, (float) ((double) x / 1), (float) ((double) y / 1), i, true);
            x += (double) renderer.getCharWidth(c) * 1;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE)
            Minecraft.getMinecraft().displayGuiScreen(prevGui);
        super.keyTyped(typedChar, keyCode);
    }
}
