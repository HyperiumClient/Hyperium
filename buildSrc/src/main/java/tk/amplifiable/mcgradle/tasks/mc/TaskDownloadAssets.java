package tk.amplifiable.mcgradle.tasks.mc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.Input;
import tk.amplifiable.mcgradle.MCGradleConstants;
import tk.amplifiable.mcgradle.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class TaskDownloadAssets extends VersionJsonDownloadTask {
    @Input
    private long initiatedTime = System.currentTimeMillis(); // we do not want this task to be cached by Gradle.

    public TaskDownloadAssets() {
    }

    @Override
    protected JsonObject getDownloadObject(JsonObject versionJson) {
        return versionJson.getAsJsonObject("assetIndex");
    }

    public String getVersion() {
        return version;
    }

    @Input
    public long getInitiatedTime() {
        return initiatedTime;
    }

    @Override
    protected File getDestination() {
        File assetsDir = new File(MCGradleConstants.CACHE_DIRECTORY, "assets");
        String id;
        try {
            id = getDownloadObject(getVersionJson()).get("id").getAsString();
        } catch (IOException e) {
            throw new GradleException("Failed to fetch version JSON", e);
        }
        return new File(assetsDir, "indexes/" + id + ".json");
    }

    @Override
    protected void afterDownload() throws IOException {
        File assetsDir = new File(MCGradleConstants.CACHE_DIRECTORY, "assets");
        String id = getDownloadObject(getVersionJson()).get("id").getAsString();
        File output = new File(assetsDir, "indexes/" + id + ".json");
        File objectsDir = new File(assetsDir, "objects");
        JsonObject object = Utils.readJsonObj(output);
        int total = object.getAsJsonObject("objects").size();
        int current = 0;
        int lastPercentage = -20;
        System.out.println("Downloading assets");
        for (Map.Entry<String, JsonElement> entry : object.getAsJsonObject("objects").entrySet()) {
            JsonObject obj = entry.getValue().getAsJsonObject();
            String hash = obj.get("hash").getAsString();
            String name = hash.substring(0, 2) + '/' + hash;
            int percentage = (int) ((float) current / (float) total * 100.f);
            if (percentage - lastPercentage >= 10) {
                System.out.println(percentage + "% complete");
                lastPercentage = percentage;
            }
            download("http://resources.download.minecraft.net/" + name, hash, new File(objectsDir, name), false);
            current++;
        }
        System.out.print("100% complete");
    }
}
