package cc.hyperium.handlers.handlers;

import java.util.HashMap;
import java.util.Map;

public class FontRendererData {
    public static final FontRendererData INSTANCE = new FontRendererData();
    public final Map<String, Integer> stringWidthCache = new HashMap<>();

    private FontRendererData() {

    }


}
