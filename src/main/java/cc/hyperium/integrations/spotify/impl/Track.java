package cc.hyperium.integrations.spotify.impl;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Track {

    @SerializedName("artist_resource")
    @Expose
    private ArtistResource artistResource;
    @SerializedName("album_resource")
    @Expose
    private AlbumResource albumResource;
    @SerializedName("length")
    @Expose
    private long length;
    @SerializedName("track_type")
    @Expose
    private String trackType;
    @SerializedName("track_resource")
    @Expose
    private TrackResource trackResource;

    public ArtistResource getArtistResource() {
        return artistResource;
    }

    public void setArtistResource(ArtistResource artistResource) {
        this.artistResource = artistResource;
    }

    public AlbumResource getAlbumResource() {
        return albumResource;
    }

    public void setAlbumResource(AlbumResource albumResource) {
        this.albumResource = albumResource;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getTrackType() {
        return trackType;
    }

    public void setTrackType(String trackType) {
        this.trackType = trackType;
    }

    public TrackResource getTrackResource() {
        return trackResource;
    }

    public void setTrackResource(TrackResource trackResource) {
        this.trackResource = trackResource;
    }

}
