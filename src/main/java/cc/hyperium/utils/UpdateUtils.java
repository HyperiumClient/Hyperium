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

import cc.hyperium.Metadata;
import com.google.common.base.Charsets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 * @author Cubxity
 */
public class UpdateUtils {

    public static UpdateUtils INSTANCE = new UpdateUtils();

    private static final HttpClient client = HttpClients.createDefault();
    private VersionAPIUtils apiUtils = new VersionAPIUtils();

    public static JsonHolder get(String url) {
        try {
            return new JsonHolder(getRaw(url));
        } catch (Exception var2) {
            var2.printStackTrace();
            return new JsonHolder();
        }
    }

    public static String getRaw(String url) throws IOException {
        return IOUtils.toString(client.execute(new HttpGet(url)).getEntity().getContent(), Charsets.UTF_8);
    }

    public boolean isAbsoluteLatest() {
        JsonObject json = apiUtils.getJson();
        int version = apiUtils.getVersion(json);

        return version <= Metadata.getVersionID();
    }

    public boolean isBeta() {
        for (JsonElement element : apiUtils.getJson().get("versions").getAsJsonArray()) {
            JsonHolder holder = new JsonHolder(element.getAsJsonObject());
            if (holder.optInt("id") == Metadata.getVersionID()) {
                return holder.optBoolean("beta");
            }
        }
        return false;
    }
}
