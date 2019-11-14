package me.semx11.autotip.util;

import com.google.gson.JsonObject;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.event.impl.EventClientConnection;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ErrorReport {

    private static Autotip autotip;

    public static void setAutotip(Autotip autotip) {
        ErrorReport.autotip = autotip;
    }

    public static void reportException(Throwable t) {
        Autotip.LOGGER.error(t.getMessage(), t);
        HttpURLConnection conn = null;
        try {
            URL url = new URL("https://api.autotip.pro/error_report.php");
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            JsonObjectBuilder builder = JsonObjectBuilder.newBuilder()
                .addString("username", autotip.getGameProfile().getName())
                .addString("uuid", autotip.getGameProfile().getId())
                .addString("v", autotip.getVersion())
                .addString("mc", autotip.getMcVersion())
                .addString("os", System.getProperty("os.name"))
                .addString("forge", "hyperium")
                .addString("stackTrace", ExceptionUtils.getStackTrace(t))
                .addNumber("time", System.currentTimeMillis());

            if (autotip.isInitialized()) {
                EventClientConnection event = autotip.getEvent(EventClientConnection.class);
                builder.addString("sessionKey", autotip.getSessionManager().getKey()).addString("serverIp", event.getServerIp());
            }

            byte[] jsonBytes = builder.build().toString().getBytes(StandardCharsets.UTF_8);

            conn.setFixedLengthStreamingMode(jsonBytes.length);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("User-Agent", "Autotip v" + autotip.getVersion());
            conn.connect();

            try (OutputStream out = conn.getOutputStream()) {
                out.write(jsonBytes);
            }

            InputStream input = conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST ? conn.getInputStream() : conn.getErrorStream();
            String json = IOUtils.toString(input, StandardCharsets.UTF_8);
            Autotip.LOGGER.info("Error JSON: " + json);
            input.close();
            conn.disconnect();
        } catch (IOException e) {
            // Hmm... what would happen if I were to report this one?
            e.printStackTrace();
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    private static class JsonObjectBuilder {

        private final JsonObject obj;

        private JsonObjectBuilder(JsonObject obj) {
            this.obj = obj;
        }

        static JsonObjectBuilder newBuilder() {
            return new JsonObjectBuilder(new JsonObject());
        }

        JsonObjectBuilder addString(String property, Object value) {
            obj.addProperty(property, String.valueOf(value));
            return this;
        }

        JsonObjectBuilder addNumber(String property, Number value) {
            obj.addProperty(property, value);
            return this;
        }

        JsonObject build() {
            return obj;
        }

    }

}
