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

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class DownloadTask extends SwingWorker<Void, Void> {
    private static final int BUFFER_SIZE = 4096;
    private final String downloadURL;
    private final String saveDirectory;
    private String fileName;

    public DownloadTask(String downloadURL, String saveDirectory) {
        this.downloadURL = downloadURL;
        this.saveDirectory = saveDirectory;
    }

    /**
     * Executed in background thread
     */
    @Override
    protected Void doInBackground() throws Exception {
        HTTPDownloadUtil util = new HTTPDownloadUtil();
        util.downloadFile(downloadURL);
        this.fileName = util.getFileName();
        File saveFilePath = new File(saveDirectory, util.getFileName());

        InputStream inputStream = util.getInputStream();
        // opens an output stream to save into file
        FileOutputStream outputStream = new FileOutputStream(saveFilePath);

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;
        long totalBytesRead = 0;
        int percentCompleted = 0;
        long fileSize = util.getContentLength();

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
            totalBytesRead += bytesRead;
            percentCompleted = (int) (totalBytesRead * 100 / fileSize);

            setProgress(percentCompleted);
        }

        outputStream.close();

        util.disconnect();
        return null;
    }

    /**
     * Executed in Swing's event dispatching thread
     */
    @Override
    protected void done() {

    }

    public String getFileName() {
        return fileName;
    }
}