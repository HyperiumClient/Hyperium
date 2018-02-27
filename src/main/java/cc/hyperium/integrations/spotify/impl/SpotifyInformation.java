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
