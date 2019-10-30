/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.utils;

import cc.hyperium.Hyperium;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.nio.charset.Charset;

public class VersionAPIUtils {
    private HttpClient httpClient;
    private String versionsUrl = "https://api.hyperium.cc/versions";

    public VersionAPIUtils() {
        httpClient = HttpClients.createDefault();
    }

    public String getDownloadLink(JsonObject json, String version) {
        JsonObject latest = json.getAsJsonObject(version);
        return latest.get("url").getAsString();
    }

    public JsonObject getJson() {
        JsonParser parser = new JsonParser();
        return parser.parse(getRaw(httpClient, versionsUrl)).getAsJsonObject();
    }

    public int getVersion(JsonObject json) {
        JsonObject latest = json.getAsJsonObject(Hyperium.IS_BETA ? "latest_beta" : "latest");
        return latest.get("id").getAsInt();
    }

    public String getRaw(HttpClient client, String url) {
        try {
            return IOUtils.toString(client.execute(new HttpGet(url)).getEntity().getContent(), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "error";
    }
}
