package cc.hyperium.mixinsimp.renderer;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheWriter;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import net.minecraft.client.renderer.GLAllocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FontFixValues {
    public static FontFixValues INSTANCE;

    private final int MAX = 1000 /* Worth bumping up to 10_000? */;
    public List<StringHash> obfuscated = new ArrayList<>();
    private Cache<StringHash, CachedString> stringCache = Caffeine.newBuilder()
            .writer(new RemovalListener())
            .executor(Multithreading.POOL)

            .maximumSize(MAX)
            .recordStats()
            .build();
    private Queue<Integer> glRemoval = new ConcurrentLinkedQueue<Integer>();
    private boolean created = false;

    public Queue<Integer> getGlRemoval() {
        return glRemoval;
    }

    @InvokeEvent
    public void tick(TickEvent tickEvent) {
        stringCache.invalidateAll(obfuscated);
        obfuscated.clear();
        int startSize = glRemoval.size();
        int thisIt = startSize / 20;
        int it = 0;
        Integer integer;
        while ((integer = glRemoval.poll()) != null && it < thisIt) {
            GLAllocation.deleteDisplayLists(integer);
            it++;
        }
    }


    public @Nullable
    CachedString get(StringHash key) {
        return stringCache.getIfPresent(key);
    }

    public void cache(StringHash key, CachedString value) {
        stringCache.put(key, value);
    }

    private class RemovalListener implements CacheWriter<StringHash, CachedString> {


        @Override
        public void write(@Nonnull StringHash key, @Nonnull CachedString value) {
        }

        @Override
        public void delete(@Nonnull StringHash key, @Nullable CachedString value, @Nonnull RemovalCause cause) {
            if (value == null) return;

            glRemoval.add(value.getListId());

        }
    }
}
