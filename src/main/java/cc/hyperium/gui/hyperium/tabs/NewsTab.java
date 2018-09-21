package cc.hyperium.gui.hyperium.tabs;

import cc.hyperium.gui.hyperium.HyperiumMainGui;
import cc.hyperium.gui.hyperium.components.AbstractTab;
import cc.hyperium.gui.hyperium.components.CollapsibleTabComponent;
import cc.hyperium.gui.hyperium.components.LabelComponent;
import cc.hyperium.utils.JsonHolder;
import com.google.common.base.Charsets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.util.Arrays;
import java.util.Collections;

/*
 * Created by Cubxity on 30/08/2018
 */
public class NewsTab extends AbstractTab {
    public static JsonHolder information;
    static HttpClient hc = HttpClients.createDefault();

    public NewsTab(HyperiumMainGui gui) {
        super(gui, "News");
        try {
            HttpGet get = new HttpGet("https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/files/information.json");
            information = new JsonHolder(IOUtils.toString(hc.execute(get).getEntity().getContent(), Charsets.UTF_8));
            JsonHolder changelog = new JsonHolder(information.optJSONArray("changelogs").get(0).getAsJsonObject());
            JsonArray description = changelog.optJSONArray("description");
            CollapsibleTabComponent collapsibleTabComponent = new CollapsibleTabComponent(this, Arrays.asList("News", "Changelog"), changelog.optString("title"));
            collapsibleTabComponent.setCollapsed(false);
            for (JsonElement element : description) {
                collapsibleTabComponent.addChild(new LabelComponent(this, Collections.emptyList(), element.getAsString()));
            }
            components.add(collapsibleTabComponent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
