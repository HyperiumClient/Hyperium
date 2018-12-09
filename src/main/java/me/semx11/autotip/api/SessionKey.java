package me.semx11.autotip.api;

public class SessionKey {

    private final String key;

    public SessionKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return key;
    }

}
