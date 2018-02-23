package cc.hyperium.handlers.handlers.remoteresources;

import cc.hyperium.utils.JsonHolder;

import java.awt.image.BufferedImage;

public class HyperiumResource {

    private String data;
    private boolean successfullyLoaded;
    private JsonHolder asJson = null;
    private BufferedImage image;

    public HyperiumResource(String data, boolean successfullyLoaded) {
        this.data = data;
        this.successfullyLoaded = successfullyLoaded;
    }
    //Order switched so null, false won't call just string
    public HyperiumResource(boolean successfullyLoaded, BufferedImage image) {
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
