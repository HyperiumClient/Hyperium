package cc.hyperium.mixinsimp.renderer;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheWriter;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import net.minecraft.client.renderer.GLAllocation;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.SharedDrawable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class FontFixValues {
    public static FontFixValues INSTANCE;

    private final int MAX = 1000 /* Worth bumping up to 10_000? */;

    private Cache<StringHash, CachedString> stringCache = Caffeine.newBuilder()
            .writer(new RemovalListener())
            .executor(Multithreading.POOL)
            .maximumSize(MAX)
            .recordStats()
            .build();
    public List<StringHash> obfuscated = new ArrayList<>();

    @InvokeEvent
    public void tick(TickEvent tickEvent) {
        stringCache.invalidateAll(obfuscated);
        obfuscated.clear();
    }

    public @Nullable CachedString get(StringHash key) {
        return stringCache.getIfPresent(key);
    }

    public void cache(StringHash key, CachedString value) {
        stringCache.put(key, value);
    }

    private class RemovalListener implements CacheWriter<StringHash, CachedString> {
        private SharedDrawable drawable;

        public RemovalListener() {
            try {
                drawable = new SharedDrawable(Display.getDrawable());
            } catch (LWJGLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void write(@Nonnull StringHash key, @Nonnull CachedString value) { }

        @Override
        public void delete(@Nonnull StringHash key, @Nullable CachedString value, @Nonnull RemovalCause cause) {
            if (value == null) return;

            if (drawable == null) {
                System.out.println("big issue font");
                return;
            }

            synchronized (drawable) {
                try {
                    drawable.makeCurrent();
                    GLAllocation.deleteDisplayLists(value.getListId());
                    drawable.releaseContext();
                } catch (LWJGLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
