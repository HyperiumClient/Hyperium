package cc.hyperium.config;

/**
 * Represents a pre-config-save listener.
 */
public interface PreSaveHandler {

    /**
     * Code to invoke before saving the config
     */
    void preSave();

}
