/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.utils.mods;

import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.utils.ChatColor;
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

    // URL that's generated upon upload
    public static String url;

    // Imgur API token
    private final String clientID;

    // File that's being uploaded
    private final File uploadFile;

    /**
     * Initialize the clientId and uploaded file
     *
     * @param clientID   Imgur API token
     * @param uploadFile File that's being uploaded
     */
    public ImgurUploader(String clientID, File uploadFile) {
        this.clientID = clientID;
        this.uploadFile = uploadFile;
    }

    /**
     * Threaded task that uploads whatever the uploadFile is
     */
    public void run() {
        HttpURLConnection conn = null;
        try {
            // Create the URL it's being uploaded to
            URL url = new URL("https://api.imgur.com/3/image");

            // Create a connection
            conn = (HttpURLConnection) url.openConnection();

            // Create the image
            BufferedImage image;

            // Assign the uploaded image
            File file = uploadFile;

            // Create the file
            file.mkdir();

            // Read image
            image = ImageIO.read(file);

            // Create a byte array
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();

            // Write to the image
            ImageIO.write(image, "png", byteArray);

            // Convert the image to a bunch of bytes
            byte[] byteImage = byteArray.toByteArray();

            // Create a Base64 encoded string from the image bytes
            String dataImage = Base64.encodeBase64String(byteImage);

            // Create the data url
            String data = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(dataImage, "UTF-8");

            // Make the connection produce an output
            conn.setDoOutput(true);

            // Make the connection produce an input
            conn.setDoInput(true);

            // Set the request method
            conn.setRequestMethod("POST");

            // Set the request property
            conn.setRequestProperty("Authorization", "Client-ID " + clientID);

            // Set that request method
            conn.setRequestMethod("POST");

            // Add in the content type
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Connect
            conn.connect();

            // Create an output stream writer and connect it to the connection
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            // Write the data
            wr.write(data);

            // Flush it
            wr.flush();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            // Create a parser
            JsonParser jsonParser = new JsonParser();

            // Parse the imgur json
            JsonObject imgurJson = jsonParser.parse(new InputStreamReader(conn.getInputStream())).getAsJsonObject();

            // Get the data string
            JsonObject two = imgurJson.getAsJsonObject("data");

            // Get the link
            String link = two.get("link").getAsString();

            // Create a message to send to the player
            ChatComponentText component2 = new ChatComponentText(ChatColor.RED + "[Hyperium] " + ChatColor.WHITE + "Uploaded to " + link);

            // Allow it to be an openable link
            component2.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link));

            // Send it to the player
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(component2);

            // Clear the writer
            wr.close();

            // Clear the response
            rd.close();
        } catch (Exception e) {
            GeneralChatHandler.instance().sendMessage("Error occurred while uploading.");
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
}
