package cc.hyperium.mods.autogg;

import java.net.*;
import org.apache.commons.io.*;
import java.util.*;

public class GetTriggers implements Runnable
{
    @Override
    public void run() {
        try {
            final String rawTriggers = IOUtils.toString(new URL("https://gist.githubusercontent.com/minemanpi/72c38b0023f5062a5f3eba02a5132603/raw/triggers.txt"));
            AutoGG.getInstance().setTriggers(new ArrayList<>(Arrays.asList(rawTriggers.split("\n"))));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
