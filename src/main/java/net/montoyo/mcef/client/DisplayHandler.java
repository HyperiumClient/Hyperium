package net.montoyo.mcef.client;

import java.util.ArrayList;
import net.montoyo.mcef.api.IDisplayHandler;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefBrowserOsr;
import org.cef.browser.CefFrame;
import org.cef.handler.CefDisplayHandler;

public class DisplayHandler implements CefDisplayHandler {

    private final ArrayList<IDisplayHandler> list = new ArrayList<>();
    private final ArrayList<EventData> queue = new ArrayList<>();

    private enum EventType {

        ADDRESS_CHANGE,
        TITLE_CHANGE,
        TOOLTIP,
        STATUS_MESSAGE

    }

    private static final class EventData {

        private final CefBrowser browser;
        private final String data;
        private final EventType type;

        private EventData(CefBrowser b, String d, EventType t) {
            browser = b;
            data = d;
            type = t;
        }

        private void execute(IDisplayHandler idh) {
            switch (type) {
                case ADDRESS_CHANGE:
                    idh.onAddressChange((CefBrowserOsr) browser, data);
                    break;

                case TITLE_CHANGE:
                    idh.onTitleChange((CefBrowserOsr) browser, data);
                    break;

                case TOOLTIP:
                    idh.onTooltip((CefBrowserOsr) browser, data);
                    break;

                case STATUS_MESSAGE:
                    idh.onStatusMessage((CefBrowserOsr) browser, data);
                    break;
            }
        }

    }

    @Override
    public void onAddressChange(CefBrowser browser, CefFrame frame, String url) {
        synchronized (queue) {
            queue.add(new EventData(browser, url, EventType.ADDRESS_CHANGE));
        }
    }

    @Override
    public void onTitleChange(CefBrowser browser, String title) {
        synchronized (queue) {
            queue.add(new EventData(browser, title, EventType.TITLE_CHANGE));
        }
    }

    @Override
    public boolean onTooltip(CefBrowser browser, String text) {
        synchronized (queue) {
            queue.add(new EventData(browser, text, EventType.TOOLTIP));
        }

        return false;
    }

    @Override
    public void onStatusMessage(CefBrowser browser, String value) {
        synchronized (queue) {
            queue.add(new EventData(browser, value, EventType.STATUS_MESSAGE));
        }
    }

    @Override
    public boolean onConsoleMessage(CefBrowser browser, CefSettings.LogSeverity level,
        String message, String source, int line) {
        return false;
    }

    public void addHandler(IDisplayHandler h) {
        list.add(h);
    }

    public void update() {
        synchronized (queue) {
            while (!queue.isEmpty()) {
                EventData ed = queue.remove(0);

                for (IDisplayHandler idh : list) {
                    ed.execute(idh);
                }
            }
        }
    }

}
