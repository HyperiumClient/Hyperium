package cc.hyperium.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.apache.commons.io.IOUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

public class GuiHyperiumCredits extends HyperiumGui {

    private static LinkedHashMap<String, DynamicTexture> contributors = new LinkedHashMap<>();
    private static HashMap<String, Integer> commits = new HashMap<>();
    private static String err = "";

    static {
        System.setProperty("http.agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        Multithreading.runAsync(() -> fetch(1));
    }

    private GuiScreen prevGui;
    private int offY = 0;

    public GuiHyperiumCredits(GuiScreen prevGui) {
        this.prevGui = prevGui;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        GlStateManager.scale(2f, 2f, 2f);
        drawCenteredString(fr, err.isEmpty() ? (contributors.isEmpty() ? "Loading contributors..." : "Contributors") : "Error: " + err, width / 4, 20 + offY / 2, 0xFFFFFF);
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        int x = width / 2 - 100;
        AtomicInteger y = new AtomicInteger(70 + offY);
        contributors.forEach((c, i) -> {
            GlStateManager.color(1f, 1f, 1f);
            GlStateManager.bindTexture(i.getGlTextureId());
            drawScaledCustomSizeModalRect(x, y.get(), 0, 0, 20, 20, 20, 20, 20, 20);
            drawString(fr, c, x + 25, y.get(), 0xE0E0E0);
            drawString(fr, commits.get(c) + " commits", x + 25, y.get() + 10, 0x757575);
            y.addAndGet(25);
        });
    }

    @Override
    protected void pack() {

    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i < 0)
            offY -= 10;
        else if (i > 0)
            offY += 10;

    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE)
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(prevGui);
        super.keyTyped(typedChar, keyCode);
    }

    private static void fetch(int tries) {
        try {
            String content = IOUtils.toString(URI.create("https://api.github.com/repos/HyperiumClient/Hyperium/stats/contributors"), Charset.forName("UTF-8"));
            JsonParser parser = new JsonParser();
            JsonArray a = parser.parse(content).getAsJsonArray();
            StreamSupport.stream(a.spliterator(), false).map(JsonElement::getAsJsonObject).filter(o -> o.has("total") & o.get("total").getAsInt() > 20).sorted(Comparator.comparingLong(o -> ((JsonObject) o).get("total").getAsInt()).reversed()).forEach(o -> {
                JsonObject con = o.get("author").getAsJsonObject();
                try {
                    BufferedImage bi = ImageIO.read(new URL(con.get("avatar_url").getAsString() + "&size=20"));
                    Minecraft.getMinecraft().addScheduledTask(() -> {
                        DynamicTexture tx = new DynamicTexture(bi);
                        try {
                            tx.loadTexture(Minecraft.getMinecraft().getResourceManager());
                            contributors.put(con.get("login").getAsString(), tx);
                            commits.put(con.get("login").getAsString(), o.get("total").getAsInt());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).get();
                } catch (IOException | InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            if (tries < 5)
                fetch(tries + 1);
            else
                err = e.getMessage();
        }
    }

}
