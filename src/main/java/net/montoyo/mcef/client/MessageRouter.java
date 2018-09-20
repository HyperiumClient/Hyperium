package net.montoyo.mcef.client;

import net.montoyo.mcef.api.IJSQueryHandler;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefBrowserOsr;
import org.cef.browser.CefFrame;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefMessageRouterHandlerAdapter;

public class MessageRouter extends CefMessageRouterHandlerAdapter {
    
    private IJSQueryHandler handler;
    
    public MessageRouter(IJSQueryHandler h) {
        handler = h;
    }

    @Override
    public boolean onQuery(CefBrowser browser, CefFrame frame, long query_id, String request,
        boolean persistent, CefQueryCallback callback) {
        return handler.handleQuery((CefBrowserOsr) browser, query_id, request, persistent, new QueryCallback(callback));
    }

    @Override
    public void onQueryCanceled(CefBrowser browser, CefFrame frame, long query_id) {
        handler.cancelQuery((CefBrowserOsr) browser, query_id);
    }

}
