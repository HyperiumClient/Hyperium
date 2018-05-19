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

package cc.hyperium.installer.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPDownloadUtil {

    private HttpURLConnection httpConn;

    /**
     * hold input stream of HttpURLConnection
     */
    private InputStream inputStream;

    private String fileName;
    private int contentLength;

    /**
     * Downloads a file from a URL
     *
     * @param fileURL HTTP URL of the file to be downloaded
     * @throws IOException If it fails
     */
    public void downloadFile(String fileURL) throws IOException {
        URL url = new URL(fileURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("GET");
        httpConn.setUseCaches(true);
        httpConn.addRequestProperty("User-Agent", "Mozilla/4.76 Hyperium Installer");
        httpConn.setReadTimeout(15000);
        httpConn.setConnectTimeout(15000);
        httpConn.setDoOutput(true);

        int responseCode = httpConn.getResponseCode();

        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String disposition = httpConn.getHeaderField("Content-Disposition");
            String contentType = httpConn.getContentType();
            contentLength = httpConn.getContentLength();

            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            } else {
                // extracts file name from URL
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                        fileURL.length());
            }

            // output for debugging purpose only
            System.out.println("Content-Type = " + contentType);
            System.out.println("Content-Disposition = " + disposition);
            System.out.println("Content-Length = " + contentLength);
            System.out.println("fileName = " + fileName);

            // opens input stream from the HTTP connection
            inputStream = httpConn.getInputStream();

        } else {
            throw new IOException(
                    "No file to download. Server replied HTTP code: "
                            + responseCode);
        }
    }

    public void disconnect() throws IOException {
        inputStream.close();
        httpConn.disconnect();
    }

    public String getFileName() {
        return this.fileName;
    }

    public int getContentLength() {
        return this.contentLength;
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }
}
