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
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import com.wrapper.spotify.model_objects.specification.Track;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.SystemUtils;
import org.omg.CORBA.Current;

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
    private static final int START_HTTPS_PORT = 4370;
    private static final int START_HTTP_PORT = 4380;
    private static final int END_HTTP_PORT = 4389;
    public static Spotify instance;
    private static int localPort = START_HTTPS_PORT;
    private final ArrayList<SpotifyListener> listeners = new ArrayList<>();
    private final OkHttpClient client = new OkHttpClient();
    private CurrentlyPlayingContext status;
    private String token;

    public static SpotifyApi spotifyApi = new SpotifyApi.Builder()
        .setAccessToken(Settings.ACCESS_TOKEN)
        .build();

    public Spotify() throws Exception {
        this.token = getOAuthToken();
        this.status = getStatus();
        instance = this;
    }

    public static void load() {
        Hyperium.LOGGER.info("Starting Spotify");
        if (Settings.SPOTIFY_FORCE_DISABLE) {
            return;
        }

        Spotify spotify = null;

        while (spotify == null) {
            try {
                spotify = new Spotify();
            } catch (Exception e) {
                e.printStackTrace();
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
                public void onPlay(CurrentlyPlayingContext info) {
                    if (Settings.SPOTIFY_NOTIFICATIONS) {
                        Hyperium.INSTANCE.getNotification()
                            .display("Spotify",
                                "Now playing " + info.getItem().getName(),
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
                final CurrentlyPlayingContext information = getStatus();
                checkForError(information);
                if (information.getIs_playing() != this.status.getIs_playing()) {
                    if (information.getIs_playing()) {
                        this.listeners.forEach(listener -> listener.onPlay(information));
                    } else {
                        this.listeners.forEach(SpotifyListener::onPause);
                    }
                }

                if (information.getItem() != null && status.getItem() != null) {
                    if (information.getItem().getAlbum().getImages() != null && status.getItem().getAlbum() != null) {
                        if (!information.getItem().getName().equals(status.getItem().getName())) {
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
    }

    public void addListener(SpotifyListener listener) {
        this.listeners.add(listener);
    }

    public CurrentlyPlayingContext getCachedStatus() {
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
    private Track getTrack() {
        Track track = null;
        try {
            track = spotifyApi.getUsersCurrentlyPlayingTrack().build().execute().getItem();
        } catch (SpotifyWebApiException | IOException ignored) {
        }
        return track;
    }

    private CurrentlyPlayingContext getStatus() {
        try {
            return spotifyApi.getInformationAboutUsersCurrentPlayback().build().execute();
        } catch (IOException | SpotifyWebApiException ignored) {
            return null;
        }
    }

    public void pause(boolean pause) {
        try {
            if(pause) {
                spotifyApi.pauseUsersPlayback().build().execute();
            } else {
                spotifyApi.startResumeUsersPlayback().build().execute();
            }
        } catch (IOException | SpotifyWebApiException ignored) {
        }
    }

    /**
     * @return oauth token
     * @throws IOException if exception occurs
     */
    private String getOAuthToken() throws IOException {
        return Settings.ACCESS_TOKEN;
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

    private void checkForError(CurrentlyPlayingContext status) throws Exception {
        if (status == null)
            throw new Exception("No user logged in");
    }

    /**
     * Listener class
     */
    public interface SpotifyListener {

        default void onPlay(CurrentlyPlayingContext info) {
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
