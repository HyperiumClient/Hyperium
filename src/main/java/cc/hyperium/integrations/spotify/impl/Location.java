
package cc.hyperium.integrations.spotify.impl;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("og")
    @Expose
    private String og;

    public String getOg() {
        return og;
    }

}
