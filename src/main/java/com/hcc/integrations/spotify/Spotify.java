/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hcc.integrations.spotify;

import com.hcc.integrations.os.OsHelper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.SystemUtils;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Spotify {
    private OkHttpClient client = new OkHttpClient();
    private final int START_HTTPS_PORT = 4370;
    private final int END_HTTPS_PORT = 4379;
    private final int START_HTTP_PORT = 4380;
    private final int END_HTTP_PORT = 4389;
    private int localPort = START_HTTPS_PORT;

    public Spotify() throws Exception {
        if (getWebHelper() == null)
            throw new UnsupportedOperationException("Could not find WebHelper // OS not supported!");
        startWebHelper();
    }

    /**
     * @param url url to get
     * @return JSONObject content
     * @throws IOException if exception occurs
     */
    private JSONObject get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                .addHeader("Origin", "https://open.spotify.com")
                .build();
        Response response = client.newCall(request).execute();
        return new JSONObject(Objects.requireNonNull(response.body()).string());
    }

    private int detectPort() throws IOException {
        try {
            get(genSpotifyUrl("/service/version.json?service=remote"));
        } catch (IOException e) {
            if (localPort == END_HTTP_PORT) {
                throw e;
            } else if (localPort == END_HTTPS_PORT) {
                localPort = START_HTTP_PORT;
            } else {
                ++localPort;
            }
            return this.detectPort();
        }
        return localPort;
    }

    private String genSpotifyUrl(String path) {
        String protocol = "https://";
        if (localPort >= START_HTTP_PORT && localPort <= END_HTTP_PORT) {
            protocol = "http://";
        }
        return String.format("%s%s:%d%s", protocol, "127.0.0.1", localPort, path);
    }

    /**
     * starts spotify's webHelper
     */
    private void startWebHelper() {
        if (isWebHelperRunning()) return;
        try {
            Desktop.getDesktop().open(Objects.requireNonNull(getWebHelper()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return web helper executable path
     */
    private File getWebHelper() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return new File(System.getProperty("user.home"), "\\AppData\\Roaming\\Spotify\\SpotifyWebHelper.exe");
        } else if (SystemUtils.IS_OS_MAC) {
            return new File(System.getProperty("user.home"), "/Library/Application Support/Spotify/SpotifyWebHelper");
        } else {
            return null;
        }
    }

    private boolean isWebHelperRunning() {
        try {
            return OsHelper.isProcessRunning("SpotifyWebHelper");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
