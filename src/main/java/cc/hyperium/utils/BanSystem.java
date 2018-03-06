package cc.hyperium.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BanSystem {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("disallow")
    @Expose
    private boolean disallow;

    @SerializedName("reason")
    @Expose
    private String reason;

    @SerializedName("expire")
    @Expose
    private String expire;

    public boolean isSuccessful() {
        return success;
    }

    public boolean isDisallow() {
        return disallow;
    }

    public String getReason() {
        return reason;
    }

    public String getExpire() {
        return expire;
    }

}
