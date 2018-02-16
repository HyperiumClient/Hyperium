package club.sk1er.website.api.requests;

import com.hcc.utils.JsonHolder;

public class HypixelApiFriendObject {
    private JsonHolder data;
    private int ord = -1;

    public HypixelApiFriendObject(JsonHolder data) {
        this.data = data;

        switch (data.optString("rank")) {
            case "ADMIN":
                ord = 1;
                break;
            case "MODERATOR":
                ord = 2;
                break;
            case "HELPER":
                ord = 3;
                break;
            case "YOUTUBER":
                ord = 4;
                break;
            case "MVP_PLUS_PLUS":
                ord = 5;
                break;
            case "MVP_PLUS":
                ord = 6;
                break;
            case "MVP":
                ord = 7;
                break;
            case "VIP_PLUS":
                ord = 8;
                break;
            case "VIP":
                ord = 9;
                break;
            default:
                ord = 11;
        }
    }

    public String getDisplay() {
        return data.optString("display");
    }

    public String getName() {
        return data.optString("name");
    }

    public int rankOrdinal() {
        return ord;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if (!(obj instanceof HypixelApiFriendObject)) return false;

        return ((HypixelApiFriendObject) obj).getUuid().equalsIgnoreCase(getUuid());
    }

    public String getUuid() {
        return data.optString("uuid");
    }

    public long getAddedOn() {
        return data.optLong("time");
    }

    public String getRank() {
        return data.optString("rank");
    }
}
