package cc.hyperium.handlers.handlers.animation.fortnite;

import cc.hyperium.handlers.handlers.animation.AnimatedDance;
import cc.hyperium.utils.JsonHolder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class FortniteHypeDance extends AnimatedDance {


    public FortniteHypeDance() {
        super();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AnimatedDance.Frame(scanner);
    }

    @Override
    public JsonHolder getData() {
        try {
            return new JsonHolder(FileUtils.readFileToString(new File("hype.json")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JsonHolder();
    }
}
