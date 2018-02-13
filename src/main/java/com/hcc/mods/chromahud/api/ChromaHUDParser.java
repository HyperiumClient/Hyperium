package com.hcc.mods.chromahud.api;


import com.hcc.utils.JsonHolder;

import java.util.Map;

/**
 * Created by mitchellkatz on 1/8/18. Designed for production use on Sk1er.club
 */
public interface ChromaHUDParser {

    DisplayItem parse(String type, int ord, JsonHolder item);

    Map<String, String> getNames();

    ChromaHUDDescription description();

}
