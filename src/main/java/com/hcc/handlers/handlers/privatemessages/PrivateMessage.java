package com.hcc.handlers.handlers.privatemessages;

public class PrivateMessage {
    private String with;
    private String message;
    private long time;
    private boolean isUser;

    public PrivateMessage(String with, String message, boolean isUser) {
        this.with = with;
        this.message = message;
        this.isUser = isUser;
        this.time = System.currentTimeMillis();
    }

    public boolean isUser() {
        return isUser;
    }

    public String withLower() {
        return getWith().toLowerCase();
    }

    public String getWith() {
        return with;
    }

    public String getMessage() {
        return message;
    }

    public long getTime() {
        return time;
    }
}
