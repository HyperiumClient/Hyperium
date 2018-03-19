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

package cc.hyperium.integrations.spotify.impl;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpotifyInformation {

    @SerializedName("server_time")
    @Expose
    private long serverTime;
    @SerializedName("playing_position")
    @Expose
    private double playingPosition;
    @SerializedName("prev_enabled")
    @Expose
    private boolean prevEnabled;
    @SerializedName("play_enabled")
    @Expose
    private boolean playEnabled;
    @SerializedName("next_enabled")
    @Expose
    private boolean nextEnabled;
    @SerializedName("open_graph_state")
    @Expose
    private OpenStateGraph openStateGraph;
    @SerializedName("version")
    @Expose
    private long version;
    @SerializedName("volume")
    @Expose
    private double volume;
    @SerializedName("running")
    @Expose
    private boolean running;
    @SerializedName("repeat")
    @Expose
    private boolean repeat;
    @SerializedName("online")
    @Expose
    private boolean online;
    @SerializedName("playing")
    @Expose
    private boolean playing;
    @SerializedName("client_version")
    @Expose
    private String clientVersion;
    @SerializedName("shuffle")
    @Expose
    private boolean shuffle;
    @SerializedName("track")
    @Expose
    private Track track;

    public long getServerTime() {
        return serverTime;
    }

    public double getPlayingPosition() {
        return playingPosition;
    }

    public boolean isPrevEnabled() {
        return prevEnabled;
    }

    public boolean isPlayEnabled() {
        return playEnabled;
    }

    public boolean isNextEnabled() {
        return nextEnabled;
    }

    public long getVersion() {
        return version;
    }

    public double getVolume() {
        return volume;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public boolean isOnline() {
        return online;
    }

    public boolean isPlaying() {
        return playing;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public Track getTrack() {
        return track;
    }

    public OpenStateGraph getOpenStateGraph() {
        return openStateGraph;
    }

}
