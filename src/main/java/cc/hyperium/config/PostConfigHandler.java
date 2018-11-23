package cc.hyperium.config;

/**
 * Represents a post-config-load listener.
 */
public interface PostConfigHandler {

    /**
     * Code to invoke when the config has been loaded
     */
    void postUpdate();

}
