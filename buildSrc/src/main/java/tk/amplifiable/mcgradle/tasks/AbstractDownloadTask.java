package tk.amplifiable.mcgradle.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import tk.amplifiable.mcgradle.MCGradleConstants;
import tk.amplifiable.mcgradle.Utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class AbstractDownloadTask extends DefaultTask {
    protected void afterDownload() throws IOException {

    }

    protected void download(String url, String sha1, File dest, boolean print) throws IOException {
        if (sha1 != null && dest.exists()) {
            sha1 = sha1.toLowerCase();
            String destSha1 = Utils.sha1(dest).toLowerCase();
            if (sha1.equalsIgnoreCase(destSha1)) return;
            if (!dest.delete()) throw new GradleException("Failed to delete old file");
        }
        if (print) System.out.println("Downloading " + url + " to " + dest);
        URL urlObj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
        conn.setRequestProperty("User-Agent", MCGradleConstants.USER_AGENT);
        if (dest.getParentFile() != null) {
            MCGradleConstants.prepareDirectory(dest.getParentFile());
        }
        try (InputStream is = conn.getInputStream()) {
            try (OutputStream os = new FileOutputStream(dest)) {
                byte[] buffer = new byte[4096];
                for (; ; ) {
                    int i = is.read(buffer, 0, buffer.length);
                    if (i < 0) {
                        break;
                    }
                    os.write(buffer, 0, i);
                }
            }
        }
        if (sha1 != null) {
            sha1 = sha1.toLowerCase();
            String destSha1 = Utils.sha1(dest).toLowerCase();
            if (!sha1.equalsIgnoreCase(destSha1)) {
                throw new GradleException("Hash " + destSha1 + " for " + url + " doesn't match expected " + sha1);
            }
        }
    }

    @TaskAction
    public void download() throws IOException {
        try {
            download(getUrl(), getSha1(), getDestination(), true);
            afterDownload();
        } catch (IOException ex) {
            if (getDestination().exists()) {
                System.err.println("Failed to download " + getUrl() + ".");
                ex.printStackTrace();
            } else {
                throw ex;
            }
        }
    }

    protected String getSha1() {
        return null;
    }

    @OutputFile
    protected abstract File getDestination();

    @Input
    protected abstract String getUrl() throws IOException;
}
