/*
 * Hyperium Client, Free client with huds and popular mod
 *     Copyright (C) 2018  Hyperium Dev Team
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

package cc.hyperium.integrations.spotify;

import cc.hyperium.integrations.os.OsHelper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.SystemUtils;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * converted from https://github.com/onetune/spotify-web-helper/blob/master/index.js
 *
 * @author Cubxity
 */
public class Spotify {
    private OkHttpClient client = new OkHttpClient();
    private static final String RETURN_ON = "\"login\",\"logout\",\"play\",\"pause\",\"error\",\"ap\"";
    private static final int START_HTTPS_PORT = 4370;
    private static final int END_HTTPS_PORT = 4379;
    private static final int START_HTTP_PORT = 4380;
    private static final int END_HTTP_PORT = 4389;
    private static int localPort = START_HTTPS_PORT;
    private List<SpotifyListener> listeners = new ArrayList<>();
    private Thread listenerThread;
    private JSONObject status;

    public Spotify() throws Exception {
        if (getWebHelper() == null)
            throw new UnsupportedOperationException("Could not find WebHelper // OS not supported!");
        startWebHelper();
        detectPort();
        getOAuthToken();
        getCSRFToken();
        getStatus();
        startListener();
    }

    /**
     * stats the listener
     */
    private void startListener() {
        listenerThread = new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    JSONObject s = getStatus();
                    checkForError(s);
                    // Call listeners
                    if (s.getBoolean("playing") != this.status.getBoolean("playing")) {
                        System.out.println("Spotify: playing status changed");
                        if (s.getBoolean("playing")) {
                            listeners.parallelStream().forEach(SpotifyListener::onPlay);
                        } else {
                            if (Math.abs(s.getLong("playing_position") - s.getJSONObject("track").getLong("length")) <= 1)
                                listeners.parallelStream().forEach(SpotifyListener::onEnd);
                            listeners.parallelStream().forEach(SpotifyListener::onPause);
                        }
                    }
                    this.status = s;
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        });
        listenerThread.start();
    }

    public void addListener(SpotifyListener listener) {
        listeners.add(listener);
    }

    public JSONObject getCachedStatus() {
        return status;
    }

    /**
     * stops the listener
     */
    public void stop() {
        listenerThread.interrupt();
    }

    /**
     * @param url url to get
     * @return JSONObject content
     * @throws IOException if exception occurs
     */
    private JSONObject get(String url, boolean keepalive) throws IOException {
        return call(build(url, keepalive).build());
    }

    /**
     * @param request request
     * @return JSONObject of response body
     * @throws IOException if exception occurs
     */
    private JSONObject call(Request request) throws IOException {
        Response response = client.newCall(request).execute();
        String res = response.body().string();
        //System.out.println("fetched "+request.url().toString()+" response: "+res);
        return new JSONObject(res);
    }

    /**
     * @param url       destination url
     * @param keepalive keep connection alive
     * @return default builder
     */
    private Request.Builder build(String url, boolean keepalive) {
        Request.Builder b = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                .addHeader("Origin", "https://open.spotify.com");
        if (keepalive)
            b.addHeader("Connection", "keep-alive");
        return b;
    }

    /**
     * @return status
     * @throws IOException if exception occurs
     */
    private JSONObject getStatus() throws IOException {
        //noinspection SpellCheckingInspection
        return this.status = get(genSpotifyUrl("/remote/status.json") + "?returnafter=1&returnon=" + RETURN_ON + "&oauth=" + getOAuthToken() + "&csrf=" + getCSRFToken(), true);
    }

    /**
     * @return oauth token
     * @throws IOException if exception occurs
     */
    private String getOAuthToken() throws IOException {
        return get("https://open.spotify.com/token", false).getString("t");
    }

    /**
     * @return csrf token
     * @throws IOException if exception occurs
     */
    private String getCSRFToken() throws IOException {
        return get(genSpotifyUrl("/simplecsrf/token.json"), false).getString("token");
    }

    /**
     * @return webHelper port
     * @throws IOException if exception occurs
     */
    private int detectPort() throws IOException {
        try {
            get(genSpotifyUrl("/service/version.json?service=remote"), false);
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

    /**
     * @param path path to generate
     * @return full url
     */
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

    /**
     * @return true if webHelper is running
     */
    private boolean isWebHelperRunning() {
        try {
            return OsHelper.isProcessRunning("SpotifyWebHelper");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void checkForError(JSONObject status) throws Exception {
        if (!status.has("open_graph_state"))
            throw new Exception("No user logged in");
        if (status.has("error"))
            throw new Exception(status.getJSONObject("error").getString("message"));
    }

    /**
     * Listener class
     */
    public static class SpotifyListener {
        public void onPlay() {

        }

        public void onPause() {

        }

        public void onSeek() {

        }

        public void onEnd() {

        }

        public void onTrackChange() {

        }

        public void onStatusChange() {

        }
    }
}
