package me.semx11.autotip.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Hosts {

    private static Hosts instance;

    private List<Host> hosts;

    private Hosts() {
    }

    public static Hosts getInstance() {
        return instance;
    }

    public static void updateHosts() {
        Gson gson = new GsonBuilder().setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        try {
            String json = IOUtils.toString(
                    new URL("https://gist.githubusercontent.com/Semx11/35d6b58783ef8d0527f82782f6555834/raw/hosts.json"));
            instance = gson.fromJson(json, Hosts.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Host getHostById(String id) {
        return hosts.stream()
                .filter(h -> h.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

}
