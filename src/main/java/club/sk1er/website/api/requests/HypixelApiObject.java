package club.sk1er.website.api.requests;

import com.google.gson.JsonObject;

/**
 * Created by mitchellkatz on 6/4/17.
 */
public interface HypixelApiObject {


    boolean isValid();

    JsonObject getData();
}
