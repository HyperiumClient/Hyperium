package cc.hyperium.handlers.handlers.browser;

public class BrowserHandler {
    public static final BrowserHandler INSTNACE = new BrowserHandler();
    private String page = "https://hyperium.cc";

    public void setPage(String arg) {
        this.page=arg;
    }
}
