package com.hcc.handlers.handlers.remoteresources;

import com.hcc.mods.sk1ercommon.Multithreading;
import com.hcc.mods.sk1ercommon.Sk1erMod;
import com.hcc.utils.JsonHolder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class RemoteResourcesHandler {

    /*
    Use this for downloading resouces that should not be hard coded.
     */

    private ConcurrentHashMap<String, HCCResource> resources = new ConcurrentHashMap<>();

    //Sync loading
    public HCCResource getResouceSync(String url) {
        return resources.computeIfAbsent(url.toUpperCase(), this::create);
    }

    private HCCResource create(String url) {
        String s1 = Sk1erMod.getInstance().rawWithAgent(url);
        JsonHolder holder = new JsonHolder(s1);
        boolean success = true;
        if (holder.isParsedCorrectly()) {
            //Response was json check if it was bad
            if (holder.has("success") && !holder.optBoolean("success")) {
                success = !holder.optString("cause").equalsIgnoreCase("exception");
            }
        }
        return new HCCResource(s1, success);
    }

    public void getResouceAsync(String url, Consumer<HCCResource> callback) {
        resources.computeIfAbsent(url.toUpperCase(), s -> {
            Multithreading.runAsync(() -> {
                HCCResource value = create(url);
                this.resources.put(url, value);
                if (callback != null)
                    callback.accept(value);
            });
            return null;
        });
    }

}
