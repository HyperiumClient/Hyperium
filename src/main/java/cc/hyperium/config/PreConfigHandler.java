package cc.hyperium.config;

/**
 * Represents a post-config-load listener.
 */
public interface PreConfigHandler {

    /**
     * Code to invoke before the config is loaded.
     */
    void preUpdate();

}
