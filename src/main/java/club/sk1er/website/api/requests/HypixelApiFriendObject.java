package club.sk1er.website.api.requests;

import com.hcc.utils.JsonHolder;

public class HypixelApiFriendObject {
    private JsonHolder data;

    public HypixelApiFriendObject(JsonHolder data) {
        this.data = data;
    }

    public String getDisplay() {
        return data.optString("display");
    }

    public String getName() {
        return data.optString("name");
    }

    public String getAddedOn() {
        return data.optString("time");
    }

    public String getRank() {
        return data.optString("rank");
    }
}
