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
import cc.hyperium.internal.UpdateChecker;
import com.google.common.base.Charsets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.*;

/**
 * @author Cubxity
 */
public class UpdateUtils {

    private static UpdateUtils instance;
    private boolean asked;

    public UpdateUtils() {
        instance = this;
    }

    private static final HttpClient client = HttpClients.createDefault();
    private static final OkHttpClient okClient = new OkHttpClient();
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

    public void downloadLatestVersion() throws IOException {
        JsonObject json = apiUtils.getJson();
        String versionNum = String.valueOf(apiUtils.getVersion(json)); //I'm not fixing this.

        Request request = new Request.Builder()
            .url(apiUtils.getDownloadLink(json, versionNum))
            .build();

        Response response = okClient.newCall(request).execute();
        ResponseBody body = response.body();
        BufferedSource source = body.source();

        File destFile = File.createTempFile("hyperium-installer", ".jar");
        BufferedSink sink = Okio.buffer(Okio.sink(destFile));
        Buffer sinkBuffer = sink.buffer();

        int bufferSize = 8 * 1024;
        for (long bytesRead; (bytesRead = source.read(sinkBuffer, bufferSize)) != -1; ) {
            sink.emit();
        }
        sink.flush();
        sink.close();
        source.close();
    }
}
