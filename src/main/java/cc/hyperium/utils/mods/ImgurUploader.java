/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ImgurUploader implements Runnable {

    public static String url;
    private final String clientID;
    private final File uploadFile;

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
            ChatComponentText component2 = new ChatComponentText(ChatColor.RED + "[Hyperium] " + ChatColor.WHITE + "Uploaded to " + link);
            component2.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link));
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(component2);

            wr.close();
            rd.close();
        } catch (Exception e) {
            GeneralChatHandler.instance().sendMessage("Error occurred while uploading.");
        }
    }

}
