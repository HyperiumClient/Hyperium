package cc.hyperium.integrations.spotify.impl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpenStateGraph {

    @SerializedName("private_session")
    @Expose
    private boolean privateSession;

    public boolean isPrivateSession() {
        return privateSession;
    }

}
