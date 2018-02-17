package me.semx11.autotip.util;

public class Host {

    private String id;
    private String url;
    private boolean enabled;

    public Host(String id, String url, boolean enabled) {
        this.id = id;
        this.url = url;
        this.enabled = enabled;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public boolean isEnabled() {
        return enabled;
    }

}
