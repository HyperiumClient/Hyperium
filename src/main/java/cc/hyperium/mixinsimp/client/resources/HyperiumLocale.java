package cc.hyperium.mixinsimp.client.resources;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.io.InputStream;
import java.util.function.Supplier;

public class HyperiumLocale {
    public static final Multimap<String, Supplier<InputStream>> LANG_FILES = ArrayListMultimap.create();

    public static void registerHyperiumLang(String lang) {
        HyperiumLocale.LANG_FILES.put(
            lang,
            () -> HyperiumLocale.class.getResourceAsStream("/assets/hyperium/lang/" + lang + ".lang")
        );
    }
}
