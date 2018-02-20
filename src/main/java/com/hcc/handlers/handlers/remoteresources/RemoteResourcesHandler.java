package com.hcc.handlers.handlers.remoteresources;

import com.hcc.HCC;
import com.hcc.handlers.handlers.chat.GeneralChatHandler;
import com.hcc.mods.sk1ercommon.Multithreading;
import com.hcc.mods.sk1ercommon.Sk1erMod;
import com.hcc.utils.JsonHolder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class RemoteResourcesHandler {

    /*
    Use this for downloading resources that should not be hard coded.
    Cache times are only valid when attempting to load from file. If their game is open, it will always the version in memory.

     */
    private final ResourceFrame[] preload = {new ResourceFrame(ResourceType.TEXT, "chat_regex")};
    private final ConcurrentHashMap<String, HCCResource> resources = new ConcurrentHashMap<>();
    private final String GITHUB_DATA = "https://raw.githubusercontent.com/HypixelCommunityClient/HCC-Repo/master/files/";
    private JsonHolder resourceData = null;
    private JsonHolder cacheTimes = new JsonHolder();
    private HashMap<String, String> urlToName = new HashMap<>();
    private ReentrantLock saveLock = new ReentrantLock();

    public RemoteResourcesHandler() {
        Multithreading.runAsync(() -> {
            int attempts = 0;
            while (attempts < 10) {
                HCCResource resource = create("resources", ResourceType.TEXT);
                if (resource.isSuccessfullyLoaded()) {
                    this.resourceData = resource.getasJson();
                    break;
                }
                attempts++;
            }

            cacheTimes = new JsonHolder(readFileString("cachetimes.txt"));
            //TODO add local cache check
            if (resourceData == null) {
                GeneralChatHandler.instance().sendMessage("Unable to load critical critical resource file and no cached version is available. " +
                        "Please restart your game.");
                return;
            }
            for (String name : resourceData.optJSONObject("resources").getKeys()) {
                JsonHolder holder = resourceData.optJSONObject("resources").optJSONObject(name);
                for (String s : holder.getJsonArrayAsStringList("urls")) {
                    urlToName.put(s, name.toLowerCase());
                }
                if (holder.optBoolean("preload")) {
                    getResourceAsync(holder.optString("preload_url"), ResourceType.valueOf(holder.optString("type")), (hccResource -> {
                        if (hccResource.isSuccessfullyLoaded())
                            System.out.println("Preloaded HCC Resource: " + holder.optString("url"));
                        else System.out.println("Failed to load HCC Resource " + holder.optString("url"));
                    }));
                }
            }
            //for those hardcoded urls
            for (ResourceFrame s : preload) {
                getResourceAsync(s.getUrl(), s.getType(), (hccResource -> {
                    if (hccResource.isSuccessfullyLoaded())
                        System.out.println("Preloaded HCC Resource: " + s);
                    else System.out.println("Failed to load HCC Resource " + s);
                }));
            }
        });

    }

    private String decodeName(String name) {
        return urlToName.get(name.toLowerCase());
    }

    private File getFile(String name) {
        return new File(HCC.folder + "/cache/", name.toLowerCase());
    }

    private String readFileString(String name) {
        try {
            FileReader fr = new FileReader(getFile(name));
            BufferedReader br = new BufferedReader(fr);
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null)
                builder.append(line);

            return builder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private BufferedImage readFileImage(String name) {
        File file = getFile(name);
        if (file.exists()) {
            try {
                return ImageIO.read(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private boolean useCache(String resource) {
        if (resourceData == null || resource == null)
            return false;
        JsonHolder resources = resourceData.optJSONObject("resources");
        if (resources.has(resource.toLowerCase())) {
            JsonHolder theResourceData = resources.optJSONObject(resource.toLowerCase());
            long lastCached = cacheTimes.optLong(resource);
            return System.currentTimeMillis() - lastCached > theResourceData.optLong("cache_time");
        }
        return false;
    }

    //Sync loading
    public HCCResource getResourceSync(String url, ResourceType type) {
        return resources.computeIfAbsent(url.toLowerCase(), s -> create(url, type));
    }

    private HCCResource create(String url, ResourceType type) {
        if (!url.startsWith("http")) {
            //Check for cache, we only cache data that has is within our data system
            JsonHolder resources = resourceData.optJSONObject("resources");
            if (resources.has(url.toLowerCase())) {
                JsonHolder theResourceData = resources.optJSONObject(url.toLowerCase());
                if (useCache(url)) {
                    if (type == ResourceType.TEXT) {
                        return new HCCResource(readFileString(url), true);
                    } else if (type == ResourceType.IMAGE) {
                        return new HCCResource(true, readFileImage(url));
                    }
                }
                //Allows remapping of urls to not just be the github file system
                if (theResourceData.has("url"))
                    url = theResourceData.optString("url");
            } else
                url = GITHUB_DATA + url;
        }
        //not cached -> get from http
        if (type == ResourceType.TEXT) {
            String s1 = Sk1erMod.getInstance().rawWithAgent(url);
            JsonHolder holder = new JsonHolder(s1);
            boolean success = true;
            if (holder.isParsedCorrectly()) {
                //Response was json check if it was bad
                if (holder.has("success") && !holder.optBoolean("success")) {
                    success = !holder.optString("cause").equalsIgnoreCase("exception");
                }
            }

            return new HCCResource(s1, success);
        } else if (type == ResourceType.IMAGE) {
            try {
                URL url1 = new URL(url);
                BufferedImage read = ImageIO.read(url1);
                saveImage(read, decodeName(url));
                return new HCCResource(true, read);
            } catch (Exception e) {
                return new HCCResource(false, null);
            }
        } else return new HCCResource("{}", false);
    }

    public void saveTextFile(String text, String name) {
        try {
            saveLock.lock();
            cacheTimes.put(name.toLowerCase(), System.currentTimeMillis());
            File file = getFile(name);
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(text);
            bw.close();
            fw.close();
        } catch (Exception e) {

        } finally {
            saveLock.unlock();
        }
    }

    private void saveDataFile() {
        saveTextFile(cacheTimes.toString(), "cachetimes.txt");
    }

    public void saveImage(BufferedImage image, String name) {
        try {
            saveLock.lock();
            cacheTimes.put(name.toLowerCase(), System.currentTimeMillis());
            File file = getFile(name);
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            saveLock.unlock();
        }
    }

    public void getResourceAsync(String url, ResourceType type, Consumer<HCCResource> callback) {
        resources.computeIfAbsent(url.toLowerCase(), s -> {
            Multithreading.runAsync(() -> {
                HCCResource value = create(url, type);
                this.resources.put(url, value);
                if (callback != null)
                    callback.accept(value);
            });
            return null;
        });
    }

    public enum ResourceType {
        TEXT,
        IMAGE;
    }

    class ResourceFrame {
        private ResourceType type;
        private String url;

        ResourceFrame(ResourceType type, String url) {
            this.type = type;
            this.url = url;
        }

        public ResourceType getType() {
            return type;
        }

        public String getUrl() {
            return url;
        }
    }

}
