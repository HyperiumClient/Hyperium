package com.hcc.handlers.handlers.remoteresources;

public class HCCResource {

    private String data;
    private boolean successfullyLoaded;


    public HCCResource(String data, boolean successfullyLoaded) {
        this.data = data;
        this.successfullyLoaded = successfullyLoaded;
    }


    public String getData() {
        return data;
    }

    public boolean isSuccessfullyLoaded() {
        return successfullyLoaded;
    }
}
