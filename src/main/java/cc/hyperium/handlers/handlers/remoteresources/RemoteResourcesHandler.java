/*
 *  Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  Hyperium Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.handlers.handlers.remoteresources;

import cc.hyperium.Hyperium;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.JsonHolder;
import com.google.gson.JsonObject;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class RemoteResourcesHandler {

    /*
    Use this for downloading resources.json that should not be hard coded.
    Cache times are only valid when attempting to load from file. If their game is open, it will always the version in memory.

     */
    private final ResourceFrame[] preload = {new ResourceFrame(ResourceType.TEXT, "chat_regex")};
    private final ConcurrentHashMap<String, HyperiumResource> resources = new ConcurrentHashMap<>();
    private final String GITHUB_DATA = "https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/files/";
    private JsonHolder resourceData = new JsonHolder();
    private JsonHolder cacheTimes = new JsonHolder();
    private HashMap<String, String> urlToName = new HashMap<>();
    private ReentrantLock saveLock = new ReentrantLock();

    public RemoteResourcesHandler() {
        Multithreading.runAsync(() -> {
            while (!Hyperium.INSTANCE.isAcceptedTos()) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            int attempts = 0;
            while (attempts < 10) {
                HyperiumResource resource = create("resources.json", ResourceType.TEXT);
                if (resource.isSuccessfullyLoaded()) {
                    this.resourceData = resource.getasJson();
                    break;
                }
                attempts++;
            }

            cacheTimes = new JsonHolder(readFileString("cachetimes.txt"));
            String resources = readFileString("resources");
            if (!resources.isEmpty() && !resources.equalsIgnoreCase("{}")) {
                resourceData = new JsonHolder(resources);
            }

            if (resourceData == null) {
                GeneralChatHandler.instance().sendMessage("Unable to load critical critical resource file and no cached version is available. " +
                        "Please restart your game.");
                return;
            }
            for (String name : resourceData.optJSONObject("resources.json").getKeys()) {
                JsonHolder holder = resourceData.optJSONObject("resources.json").optJSONObject(name);
                for (String s : holder.getJsonArrayAsStringList("urls")) {
                    urlToName.put(s, name.toLowerCase());
                }
                if (holder.optBoolean("preload")) {
                    getResourceAsync(holder.optString("preload_url"), ResourceType.valueOf(holder.optString("type")), (HyperiumResource -> {
                        if (HyperiumResource.isSuccessfullyLoaded())
                            System.out.println("Preloaded Hyperium Resource: " + holder.optString("url"));
                        else System.out.println("Failed to load Hyperium Resource " + holder.optString("url"));
                    }));
                }
            }
            //for those hardcoded urls
            for (ResourceFrame s : preload) {
                getResourceAsync(s.getUrl(), s.getType(), (HyperiumResource -> {
                    if (HyperiumResource.isSuccessfullyLoaded())
                        System.out.println("Preloaded Hyperium Resource: " + s);
                    else System.out.println("Failed to load Hyperium Resource " + s);
                }));
            }
            saveTextFile(this.resourceData.toString(), "resources.json");
        });

    }

    private String decodeName(String name) {
        return urlToName.get(name.toLowerCase());
    }

    private File getFile(String name) {
        return new File(Hyperium.folder + "/cache/", name.toLowerCase());
    }

    private String readFileString(String name) {
        try {
            if (!getFile(name).exists()) {
                return "";
            }

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
    public HyperiumResource getResourceSync(String url, ResourceType type) {
        return resources.computeIfAbsent(url.toLowerCase(), s -> create(url, type));
    }

    private HyperiumResource create(String url, ResourceType type) {
        if (!url.startsWith("http")) {
            //Check for cache, we only cache data that has is within our data system
            JsonHolder resources = resourceData.optJSONObject("resources");
            if (resources.has(url.toLowerCase())) {
                JsonHolder theResourceData = resources.optJSONObject(url.toLowerCase());
                if (useCache(url)) {
                    if (type == ResourceType.TEXT) {
                        return new HyperiumResource(readFileString(url), true);
                    } else if (type == ResourceType.IMAGE) {
                        return new HyperiumResource(true, readFileImage(url));
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
            String s1 = rawHttp(url);
            JsonHolder holder = new JsonHolder(s1);
            boolean success = true;
            if (holder.isParsedCorrectly()) {
                //Response was json check if it was bad
                if (holder.has("success") && !holder.optBoolean("success")) {
                    success = !holder.optString("cause").equalsIgnoreCase("exception");
                }
            }
            //resource file
            if (!s1.endsWith("resources.json"))
                saveTextFile(s1, decodeName(url));
            return new HyperiumResource(s1, success);
        } else if (type == ResourceType.IMAGE) {
            try {
                URL url1 = new URL(url);
                BufferedImage read = ImageIO.read(url1);
                saveImage(read, decodeName(url));
                return new HyperiumResource(true, read);
            } catch (Exception e) {
                return new HyperiumResource(false, null);
            }
        } else return new HyperiumResource("{}", false);
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

    public void getResourceAsync(String url, ResourceType type, Consumer<HyperiumResource> callback) {
        resources.computeIfAbsent(url.toLowerCase(), s -> {
            Multithreading.runAsync(() -> {
                HyperiumResource value = create(url, type);
                this.resources.put(url, value);
                if (callback != null)
                    callback.accept(value);
            });
            return null;
        });
    }

    public String rawHttp(String url) {
        url = url.trim().replace(" ", "%20");
        try {
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(true);
            connection.addRequestProperty("User-Agent", "Mozilla/4.76 Hyperium ");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoOutput(true);
            InputStream is = connection.getInputStream();
            return IOUtils.toString(is, Charset.forName("UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObject object = new JsonObject();
        object.addProperty("success", false);
        object.addProperty("cause", "Exception");
        return object.toString();

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
