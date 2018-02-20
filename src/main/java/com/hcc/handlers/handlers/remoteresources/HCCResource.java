package com.hcc.handlers.handlers.remoteresources;

import com.hcc.utils.JsonHolder;

import java.awt.image.BufferedImage;

public class HCCResource {

    private String data;
    private boolean successfullyLoaded;
    private JsonHolder asJson = null;
    private BufferedImage image;

    public HCCResource(String data, boolean successfullyLoaded) {
        this.data = data;
        this.successfullyLoaded = successfullyLoaded;
    }
    //Order switched so null, false won't call just string
    public HCCResource(boolean successfullyLoaded, BufferedImage image) {
        this.successfullyLoaded = successfullyLoaded;
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getData() {
        return data;
    }

    public boolean isSuccessfullyLoaded() {
        return successfullyLoaded;
    }

    public JsonHolder getasJson() {
        if (asJson == null) {
            asJson = new JsonHolder(data);
        }
        return asJson;
    }
}
