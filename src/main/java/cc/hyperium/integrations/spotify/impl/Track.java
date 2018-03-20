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
