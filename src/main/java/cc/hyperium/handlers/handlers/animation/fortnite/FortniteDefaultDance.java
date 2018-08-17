package cc.hyperium.handlers.handlers.animation.fortnite;

import cc.hyperium.handlers.handlers.animation.AnimatedDance;
import cc.hyperium.utils.JsonHolder;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;

public class FortniteDefaultDance extends AnimatedDance {


    @Override
    public JsonHolder getData() {
        try {
            return new JsonHolder(IOUtils.toString(new URL("https://static.sk1er.club/hyperium/fortnite_dance.json")));
//            return new JsonHolder(FileUtils.readFileToString(new File("fortnite_dance.json")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JsonHolder();
    }
}
