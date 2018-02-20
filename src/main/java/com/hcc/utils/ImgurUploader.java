package com.hcc.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ImgurUploader implements Runnable {

    public static String url;
    private String clientID;
    private File uploadFile;

    public ImgurUploader(String clientID, File uploadFile) {
        this.clientID = clientID;
        this.uploadFile = uploadFile;
    }

    public void run() {
        try {
            URL url = new URL("https://api.imgur.com/3/image");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedImage image = null;
            File file = uploadFile;
            file.mkdir();
            // read image
            image = ImageIO.read(file);
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            ImageIO.write(image, "png", byteArray);
            byte[] byteImage = byteArray.toByteArray();

            String dataImage = Base64.encodeBase64String(byteImage);
            String data = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(dataImage, "UTF-8");

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Client-ID " + clientID);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            conn.connect();
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonParser jsonParser = new JsonParser();
            JsonObject imgurJson = jsonParser.parse(new InputStreamReader(conn.getInputStream())).getAsJsonObject();
            JsonObject two = imgurJson.getAsJsonObject("data");
            String link = two.get("link").getAsString();
            ChatComponentText component = new ChatComponentText(ChatColor.RED + "[HCC] " + ChatColor.WHITE + "Uploaded!");
            ChatComponentText component2 = new ChatComponentText(ChatColor.RED + "[HCC] " + ChatColor.WHITE + "Click here to view!");
            component2.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link));
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(component);
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(component2);

            wr.close();
            rd.close();
        } catch (Exception e) {

        }
    }


}