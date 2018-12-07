package me.semx11.autotip.api.reply;

import me.semx11.autotip.api.RequestType;

public abstract class Reply {

    private boolean success;
    private String cause;

    public Reply() {
    }

    public Reply(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getCause() {
        return cause;
    }

    public abstract RequestType getRequestType();

}
