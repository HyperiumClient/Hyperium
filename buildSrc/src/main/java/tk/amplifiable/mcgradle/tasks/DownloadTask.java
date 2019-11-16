package tk.amplifiable.mcgradle.tasks;

import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;

import javax.inject.Inject;
import java.io.File;

public class DownloadTask extends AbstractDownloadTask {
    @Input
    public String url;
    @OutputFile
    public File destination;
    private boolean alwaysRefresh;

    @Inject
    public DownloadTask(String url, File destination, boolean alwaysRefresh) {
        this.url = url;
        this.destination = destination;
        this.alwaysRefresh = alwaysRefresh;
    }

    @Input
    public long getTime() {
        return alwaysRefresh ? System.currentTimeMillis() : 0;
    }

    @Override
    @OutputFile
    protected File getDestination() {
        return destination;
    }

    @Override
    @Input
    protected String getUrl() {
        return url;
    }
}
