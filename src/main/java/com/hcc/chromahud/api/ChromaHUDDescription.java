package com.hcc.chromahud.api;

/**
 * Created by mitchellkatz on 1/8/18. Designed for production use on Sk1er.club
 */
public class ChromaHUDDescription {
    private String id;
    private String version;
    private String name;
    private String description;

    public ChromaHUDDescription(String id, String version, String name, String description) {

        this.id = id;
        this.version = version;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
