package cc.hyperium.gui.main.tabs;

import cc.hyperium.Metadata;
import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.main.HyperiumMainGui;
import cc.hyperium.gui.main.components.AbstractTab;
import cc.hyperium.gui.main.components.OverlayButton;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.HyperiumFontRenderer;
import cc.hyperium.utils.JsonHolder;
import com.google.common.base.Charsets;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.awt.*;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/*
 * Created by Cubxity on 20/05/2018
 */
public class HomeTab extends AbstractTab {
    public static JsonHolder information;
    static HttpClient hc = HttpClients.createDefault();
    private static HyperiumFontRenderer title = new HyperiumFontRenderer("Arial", Font.PLAIN, 40);
    private GuiBlock block;
    private int y, w;

    static {
        Multithreading.schedule(() -> {
            try {
                HttpGet get = new HttpGet("https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/files/information.json");
                information = new JsonHolder(IOUtils.toString(hc.execute(get).getEntity().getContent(), Charsets.UTF_8));
                System.out.println(information.toString());
                for (JsonElement e : information.optJSONArray("alerts")) {
                    JsonHolder alert = new JsonHolder(e.getAsJsonObject());
                    if (!HyperiumMainGui.getLoadedAlerts().contains(alert.optString("title")) && !alert.optString("title").equals("ALERT FORMAT - THIS WILL BE IGNORED") && alert.optJSONArray("target").contains(new JsonPrimitive(Metadata.getVersion()))) {
                        HyperiumMainGui.getAlerts().add(new HyperiumMainGui.Alert(Icons.valueOf(alert.optString("icon")).getResource(), () -> {
                            if(alert.has("click")) {
                                try {
                                    Desktop.getDesktop().browse(new URL(alert.optString("click")).toURI());
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }, alert.optString("title")));
                        HyperiumMainGui.getLoadedAlerts().add(alert.optString("title"));
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }, 0, 10, TimeUnit.MINUTES);
    }

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
        if (information != null) {
            JsonHolder changelog = new JsonHolder(information.optJSONArray("changelogs").get(0).getAsJsonObject());

            title.drawString(changelog.optString("title"), topX + 5, topY + 5, 0xffffff);
            int i = 25;
            for (JsonElement e : changelog.optJSONArray("description")) {
                fr.drawString(e.getAsString(), topX + 5, topY + i, Color.LIGHT_GRAY.getRGB());
                i += 11;
            }
        } else {
            title.drawString("Loading...", topX + 5, topY + 5, 0xffffff);
        }
    }
}
