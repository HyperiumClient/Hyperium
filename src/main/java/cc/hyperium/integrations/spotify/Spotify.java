/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.integrations.spotify;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.integrations.os.OsHelper;
import cc.hyperium.integrations.spotify.impl.SpotifyInformation;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.SystemUtils;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * converted from https://github.com/onetune/spotify-web-helper/blob/master/index.js
 *
 * @author Cubxity
 */
public class Spotify {
    private static final String RETURN_ON = "\"login\",\"logout\",\"play\",\"pause\",\"error\",\"ap\"";
    private static final int START_HTTPS_PORT = 4370;
    private static final int END_HTTPS_PORT = 4379;
    private static final int START_HTTP_PORT = 4380;
    private static final int END_HTTP_PORT = 4389;
    public static Spotify instance;
    private static int localPort = START_HTTPS_PORT;
    private final ArrayList<SpotifyListener> listeners = new ArrayList<>();
    private final OkHttpClient client = new OkHttpClient();
    private SpotifyInformation status;

    private String token;
    private String csrfToken;

    private int reconnectAttempts = 0;

    public Spotify() throws Exception {
        if (getWebHelper() == null)
            throw new UnsupportedOperationException("Could not find WebHelper // OS not supported!");
        startWebHelper();
        detectPort();
        this.token = getOAuthToken();
        this.csrfToken = getCSRFToken();
        this.status = getStatus();
        instance = this;
    }

    public static void load() {

        if (Settings.SPOTIFY_FORCE_DISABLE) {
            return;
        }

        Spotify spotify = null;

        while (spotify == null) {
            try {
                spotify = new Spotify();
            } catch (Exception ignored) {
            }

            if (spotify == null) {
                try {
                    Thread.sleep(1000 * 5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            spotify.addListener(new Spotify.SpotifyListener() {
                @Override
                public void onPlay(SpotifyInformation info) {
                    if (Settings.SPOTIFY_NOTIFICATIONS) {
                        Hyperium.INSTANCE.getNotification()
                            .display("Spotify",
                                "Now playing " + info.getTrack().getTrackResource().getName(),
                                8
                            );
                    }
                }
            });

            spotify.start();
        } catch (Exception e) {
            Hyperium.LOGGER.warn("Failed to connect to spotify");
        }
    }

    /**
     * stats the listener
     */
    public void start() {
        Multithreading.schedule(() -> {
            if (Settings.SPOTIFY_FORCE_DISABLE) {
                return;
            }

            try {
                final SpotifyInformation information = getStatus();
                checkForError(information);
                if (information.isPlaying() != this.status.isPlaying()) {
                    if (information.isPlaying()) {
                        this.listeners.forEach(listener -> listener.onPlay(information));
                    } else {
                        this.listeners.forEach(SpotifyListener::onPause);
                    }
                }

                if (information.getTrack() != null && status.getTrack() != null) {
                    if (information.getTrack().getAlbumResource() != null && status.getTrack().getAlbumResource() != null) {
                        if (!information.getTrack().getAlbumResource().getName().equals(status.getTrack().getAlbumResource().getName())) {
                            this.listeners.forEach(listener -> listener.onPlay(information));
                        }
                    }
                }
                // this is now 3 seconds old :P
                this.status = information;
            } catch (Exception e) {
                System.out.println("[SPOTIFY] Exception occurred");
            }
        }, 3, 3, TimeUnit.SECONDS);


        // WTF IS THIS SHIT LOL
    }

    public void addListener(SpotifyListener listener) {
        this.listeners.add(listener);
    }

    public SpotifyInformation getCachedStatus() {
        return status;
    }

    /**
     * @param url url to get
     * @return JSONObject content
     * @throws IOException if exception occurs
     */
    public JsonObject get(String url, boolean keepalive) throws IOException {
        return call(build(url, keepalive).build());
    }

    /**
     * @param request request
     * @return JSONObject of response body
     * @throws IOException if exception occurs
     */
    private JsonObject call(Request request) throws IOException {
        Response response = client.newCall(request).execute();
        String res = response.body().string();
        return new JsonParser().parse(res).getAsJsonObject();

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
    private SpotifyInformation getStatus() throws IOException {
        JsonObject obj = get(genSpotifyUrl("/remote/status.json") + "?returnafter=1&returnon=" + RETURN_ON + "&oauth=" + this.token + "&csrf=" + this.csrfToken, true);
        return new Gson().fromJson(obj, SpotifyInformation.class);
    }

    public void pause(boolean pause) {
        try {
            get(
                genSpotifyUrl("/remote/pause.json") + "?oauth=" + this.token + "&csrf=" + this.csrfToken + "&pause=" + pause,
                false
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return oauth token
     * @throws IOException if exception occurs
     */
    private String getOAuthToken() throws IOException {
        return get("https://open.spotify.com/token", false).get("t").getAsString();
    }

    /**
     * @return csrf token
     * @throws IOException if exception occurs
     */
    private String getCSRFToken() throws IOException {
        return get(genSpotifyUrl("/simplecsrf/token.json"), false).get("token").getAsString();
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

    private void checkForError(SpotifyInformation status) throws Exception {
        if (status.getOpenStateGraph() == null)
            throw new Exception("No user logged in");
    }

    /**
     * Listener class
     */
    public interface SpotifyListener {

        default void onPlay(SpotifyInformation info) {
        }

        default void onPause() {
        }

        default void onSeek() {
        }

        default void onEnd() {
        }

        default void onTrackChange() {
        }

        default void onStatusChange() {
        }

    }
}
