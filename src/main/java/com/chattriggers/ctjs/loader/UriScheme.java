package com.chattriggers.ctjs.loader;

import cc.hyperium.Hyperium;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class UriScheme {
    private static final String PROTOCOL = "chattriggers://";
    private static final int PORT = 21965;

    public static void main(String[] args) {
        if (args.length < 1) {
            Hyperium.LOGGER.error("No ChatTriggers URL found, aborting...");
            return;
        }

        if (!args[0].startsWith(PROTOCOL)) {
            Hyperium.LOGGER.error("URL found is not supported, aborting...");
            Hyperium.LOGGER.debug(args[0]);
            return;
        }

        String url = args[0];

        Hyperium.LOGGER.debug("Trying to work with URL: " + url);

        String module = url.substring(PROTOCOL.length()).replace("/", "");

        try {
            connectWithSockets(module);
        } catch (Exception e) {
            copyModuleIn(module);
        }
    }

    private static void connectWithSockets(String module) throws Exception {
        Socket socket = new Socket(InetAddress.getLocalHost(), PORT);
        socket.getOutputStream().write(module.getBytes());
        socket.close();
    }

    private static void copyModuleIn(String module) {
        String dataFolder = System.getenv("APPDATA");
        File modulesDir = new File(dataFolder + "\\.minecraft\\config\\ChatTriggers\\modules");

        File toDownload = new File(modulesDir, ".to_download.txt");

        try {
            PrintWriter pw = new PrintWriter(new FileWriter(toDownload, true));
            pw.append(module).append(",");
            pw.close();
        } catch (Exception e) {
            Hyperium.LOGGER.error("Error writing to to_download file.");
        }
    }
}
