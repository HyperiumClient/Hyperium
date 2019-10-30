package club.sk1er.website.api.requests;

import cc.hyperium.utils.JsonHolder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitchellkatz on 6/4/17.
 */
public class HypixelApiFriends implements HypixelApiObject {

    private JsonHolder master;

    public HypixelApiFriends(JsonHolder o) {
        if (o != null)
            master = o;
        else
            master = new JsonHolder();
    }

    @Override
    public String toString() {
        return master.toString();
    }

    @Override
    public JsonHolder getData() {
        return master;
    }

    public boolean isValid() {
        return master != null && !master.isNull("records");
    }

    public JsonArray getFriends() {
        return master.optJSONArray("records");
    }

    public List<JsonHolder> getFriendsAsList() {
        List<JsonHolder> friends = new ArrayList<>();
        for (JsonElement tmp1 : getFriends()) friends.add(new JsonHolder(tmp1.getAsJsonObject()));
        return friends;
    }

    public int getCount() {
        return getFriends().size();
    }

}
