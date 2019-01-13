package net.montoyo.mcef.utilities;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Observable;

// This class downloads a file from a URL.
class Download extends Observable implements Runnable {

    // These are the status codes.
    public static final int DOWNLOADING = 0;
    public static final int PAUSED = 1;
    public static final int COMPLETE = 2;
    public static final int CANCELLED = 3;
    public static final int ERROR = 4;
    // Max size of download buffer.
    private static final int MAX_BUFFER_SIZE = 1024;
    private InputStream is; // download URL
    private long size; // size of download in bytes
    private int downloaded; // number of bytes downloaded
    private int status; // current status of download
    private OutputStream output; // Output file

    // Constructor for Download.
    public Download(InputStream is, OutputStream output, long size) {
        this.is = is;
        this.output = output;
        this.size = size;
        downloaded = 0;
        status = DOWNLOADING;

        // Begin the download.
        download();
    }

    // Get this download's size.
    public long getSize() {
        return size;
    }

    // Get this download's progress.
    public float getProgress() {
        return ((float) downloaded / size) * 100;
    }

    // Get this download's status.
    public int getStatus() {
        return status;
    }

    // Pause this download.
    public void pause() {
        status = PAUSED;
        stateChanged();
    }

    // Resume this download.
    public void resume() {
        status = DOWNLOADING;
        stateChanged();
        download();
    }

    // Cancel this download.
    public void cancel() {
        status = CANCELLED;
        stateChanged();
    }

    // Mark this download as having an error.
    private void error() {
        status = ERROR;
        stateChanged();
    }

    // Start or resume downloading.
    private void download() {
        Thread thread = new Thread(this);
        thread.start();
    }

    // Get file name portion of URL.
    private String getFileName(URL url) {
        String fileName = url.getFile();
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }

    // Download file.
    public void run() {
        InputStream stream = null;

        try {
  /* Set the size for this download if it
     hasn't been already set. */
            System.out.println(size);
            stream = is;
            while (status == DOWNLOADING) {
    /* Size buffer according to how much of the
       file is left to download. */
                byte[] buffer;
                if (size - downloaded > MAX_BUFFER_SIZE) {
                    buffer = new byte[MAX_BUFFER_SIZE];
                } else {
                    buffer = new byte[(int) Math.max(size - downloaded, 4096)];
                }

                // Read from server into buffer.
                int read = stream.read(buffer);
                if (read == -1) {
                    System.out.println("broke " + buffer.length);
                    break;
                }

                // Write buffer to file.
                output.write(buffer, 0, read);
                downloaded += read;
                stateChanged();
            }

  /* Change status to complete if this point was
     reached because downloading has finished. */
            if (status == DOWNLOADING) {
                status = COMPLETE;
                stateChanged();
            }
        } catch (Exception e) {
            error();
        } finally {
            // Close file.
            if (output != null) {
                try {
                    output.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Close connection to server.
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Notify observers that this download's status has changed.
    private void stateChanged() {
        setChanged();
        notifyObservers();
    }
}
