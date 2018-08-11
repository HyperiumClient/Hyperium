package cc.hyperium.handlers.handlers.animation;

import cc.hyperium.utils.JsonHolder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class TwerkDance extends AnimatedDance {


    //THE EPITOME OF PAIN. FOR YOUR OWN GOOD, DO NOT ATTEMPT
    public JsonHolder getData() {
        try {
//            holder = new JsonHolder(IOUtils.toString(new URL("https://static.sk1er.club/hyperium/twerk.json")));
            return new JsonHolder(FileUtils.readFileToString(new File("twerk.json")));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JsonHolder();
    }
}