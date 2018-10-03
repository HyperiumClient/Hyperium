package cc.hyperium.gui.main.tabs;

import cc.hyperium.Metadata;
import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.main.OldHyperiumMainGui;
import cc.hyperium.gui.main.components.AbstractTab;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.HyperiumFontRenderer;
import cc.hyperium.utils.JsonHolder;
import com.google.common.base.Charsets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.gui.Gui;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.lwjgl.input.Mouse;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
 * Created by Cubxity on 20/05/2018
 */
public class HomeTab extends AbstractTab {
    private static final List<String> items = new ArrayList<>();
    private static final HyperiumFontRenderer title = new HyperiumFontRenderer("Arial", Font.PLAIN, 40);
    public static JsonHolder information;
    static HttpClient hc = HttpClients.createDefault();
    private static String strTitle;

    static {
        Multithreading.schedule(() -> {
            try {
                HttpGet get = new HttpGet("https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/files/information.json");
                information = new JsonHolder(IOUtils.toString(hc.execute(get).getEntity().getContent(), Charsets.UTF_8));
                System.out.println(information.toString());
                synchronized (title) {
                    JsonHolder changelog = new JsonHolder(information.optJSONArray("changelogs").get(0).getAsJsonObject());
                    JsonArray description = changelog.optJSONArray("description");
                    for (JsonElement element : description) {
                        items.add(element.getAsString());
                    }
                    strTitle = changelog.optString("title");
                }
                for (JsonElement e : information.optJSONArray("alerts")) {
                    JsonHolder alert = new JsonHolder(e.getAsJsonObject());
                    if (!OldHyperiumMainGui.INSTANCE.getLoadedAlerts().contains(alert.optString("title")) && !alert.optString("title").equals("ALERT FORMAT - THIS WILL BE IGNORED") && alert.optJSONArray("target").contains(new JsonPrimitive(Metadata.getVersion()))) {
                        OldHyperiumMainGui.INSTANCE.getAlerts().add(new OldHyperiumMainGui.Alert(Icons.valueOf(alert.optString("icon")).getResource(), () -> {
                            if (alert.has("click")) {
                                try {
                                    Desktop.getDesktop().browse(new URL(alert.optString("click")).toURI());
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }, alert.optString("title")));
                        OldHyperiumMainGui.INSTANCE.getLoadedAlerts().add(alert.optString("title"));
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }, 0, 10, TimeUnit.MINUTES);
    }

    private GuiBlock block;
    private int y, w;

    public HomeTab(int y, int w) {
        block = new GuiBlock(0, w, y, y + w);
        this.y = y;
        this.w = w;
    }

    @Override
    public void drawTabIcon() {
        Icons.HOME.bind();
        Gui.drawScaledCustomSizeModalRect(5, y + 5, 0, 0, 144, 144, w - 10, w - 10, 144, 144);
    }

    @Override
    public GuiBlock getBlock() {
        return block;
    }

    @Override
    public void drawHighlight(float s) {
        Gui.drawRect(0, (int) (y + s * (s * w / 2)), 3, (int) (y + w - s * (w / 2)), Color.WHITE.getRGB());
    }

    @Override
    public void draw(int mouseX, int mouseY, int topX, int topY, int containerWidth, int containerHeight) {
        synchronized (title) {
            if (information != null && strTitle != null) {
                HomeTab.title.drawString(strTitle, topX + 5, topY + 5, 0xffffff);
                int i = 25;
                for (String item : items) {
                    boolean second = false;
                    for (String s : fr.splitString(item, containerWidth - 10)) {
                        if (topY + i + offsetY + 9 < topY + containerHeight && topY + 25 < topY + i + offsetY) {
                            fr.drawStringWithShadow(s, topX + 5 + (second ? 20 : 0), topY + i + offsetY, new Color(168, 0, 189).getRGB());
                        }
                        i += 11;
                        second = true;
                    }
                }

            } else {
                title.drawString("Loading...", topX + 5, topY + 5, 0xffffff);
            }
        }
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        int i = Mouse.getDWheel();
        if (i > 0)
            offsetY += 10;
        else if (i < 0)
            offsetY -= 10;

       if(offsetY > 10)
           offsetY = 10;
       if(offsetY < -10*(items.size()-1)) {
           offsetY= -10*(items.size()-1);
       }
    }

    @Override
    public String getTitle() {
        return "Home";
    }
}
