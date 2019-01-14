// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

// Modified by montoyo for MCEF

package org.cef.browser;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.nio.ByteBuffer;
import net.montoyo.mcef.MCEF;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.api.IStringVisitor;
import net.montoyo.mcef.client.StringVisitor;
import net.montoyo.mcef.utilities.Log;
import org.cef.CefClient;
import org.cef.DummyComponent;
import org.cef.callback.CefDragData;
import org.cef.handler.CefClientHandler;
import org.cef.handler.CefRenderHandler;
import org.lwjgl.BufferUtils;

/**
 * This class represents an off-screen rendered browser. The visibility of this class is "package".
 * To create a new CefBrowser instance, please use CefBrowserFactory.
 */
public class CefBrowserOsr extends CefBrowser_N implements CefRenderHandler, IBrowser {

    private CefRenderer renderer_;
    private Rectangle browser_rect_ = new Rectangle(0, 0, 1, 1);  // Work around CEF issue #1437.
    private CefClientHandler clientHandler_;
    private String url_;
    private boolean isTransparent_;
    private CefRequestContext context_;
    private CefBrowserOsr parent_ = null;
    private CefBrowserOsr devTools_ = null;
    private DummyComponent dc_ = new DummyComponent();

    public static boolean CLEANUP = true;

    CefBrowserOsr(CefClientHandler clientHandler,
        String url,
        boolean transparent,
        CefRequestContext context) {
        this(clientHandler, url, transparent, context, null, null);
    }

    private CefBrowserOsr(CefClientHandler clientHandler,
        String url,
        boolean transparent,
        CefRequestContext context,
        CefBrowserOsr parent,
        Point inspectAt) {
        super(MCEF.PROXY.cefClient, url, context, parent, inspectAt);
        isTransparent_ = transparent;
        renderer_ = new CefRenderer(transparent);
        clientHandler_ = clientHandler;
        url_ = url;
        context_ = context;
        parent_ = parent;
        createGLCanvas();
    }

    @Override
    protected CefBrowser_N createDevToolsBrowser(CefClient client, String url,
        CefRequestContext context, CefBrowser_N parent, Point inspectAt) {
        return null;
    }

    @Override
    public int getTextureID() {
        return renderer_.texture_id_[0];
    }

    @Override
    public Component getUIComponent() {
        return dc_;
    }

    @Override
    public CefRenderHandler getRenderHandler() {
        return this;
    }

    @Override
    public synchronized void close() {
        if (context_ != null) {
            context_.dispose();
        }
        if (parent_ != null) {
            parent_.closeDevTools();
            parent_.devTools_ = null;
            parent_ = null;
        }

        if (CLEANUP) {
            MCEF.PROXY.removeBrowser(this);
            renderer_.cleanup();
        }

        super.close(false);
    }

    @Override
    public synchronized CefBrowser getDevTools() {
        return getDevTools(null);
    }

    @Override
    public synchronized CefBrowser getDevTools(Point inspectAt) {
        if (devTools_ == null) {
            devTools_ = new CefBrowserOsr(clientHandler_,
                url_,
                isTransparent_,
                context_,
                this,
                inspectAt);
        }
        return devTools_;
    }

    @Override
    public void resize(int width, int height) {
        browser_rect_.setBounds(0, 0, width, height);
        dc_.setBounds(browser_rect_);
        dc_.setVisible(true);
        wasResized(width, height);
    }

    @Override
    public void draw(double x1, double y1, double x2, double y2) {
        renderer_.render(x1, y1, x2, y2);
    }

    @SuppressWarnings("serial")
    private void createGLCanvas() {
        createBrowser(clientHandler_, 0, url_, isTransparent_, null, context_);
    }

    @Override
    public Rectangle getViewRect(CefBrowser browser) {
        return browser_rect_;
    }

    @Override
    public Point getScreenPoint(CefBrowser browser, Point viewPoint) {
        return viewPoint;
    }

    @Override
    public void onPopupShow(CefBrowser browser, boolean show) {
        if (!show) {
            renderer_.clearPopupRects();
            invalidate();
        }
    }

    private static class PaintData {

        private ByteBuffer buffer;
        private int width;
        private int height;
        private Rectangle[] dirtyRects;
        private boolean hasFrame;
        private boolean fullReRender;
    }

    private final PaintData paintData = new PaintData();

    @Override
    public void onPaint(CefBrowser browser, boolean popup, Rectangle[] dirtyRects,
        ByteBuffer buffer, int width, int height) {
        if (popup) {
            return;
        }

        final int size = (width * height) << 2;

        synchronized (paintData) {
            if (buffer.limit() > size) {
                Log.warning("Skipping MCEF browser frame, data is too heavy");
            } else {
                if (paintData.hasFrame) //The previous frame was not uploaded to GL texture, so we skip it and render this on instead
                {
                    paintData.fullReRender = true;
                }

                if (paintData.buffer == null || size != paintData.buffer
                    .capacity()) //This only happens when the browser gets resized
                {
                    paintData.buffer = BufferUtils.createByteBuffer(size);
                }

                paintData.buffer.position(0);
                paintData.buffer.limit(buffer.limit());
                buffer.position(0);
                paintData.buffer.put(buffer);
                paintData.buffer.position(0);

                paintData.width = width;
                paintData.height = height;
                paintData.dirtyRects = dirtyRects;
                paintData.hasFrame = true;
            }
        }
    }

    public void mcefUpdate() {
        synchronized (paintData) {
            if (paintData.hasFrame) {
                renderer_.onPaint(false, paintData.dirtyRects, paintData.buffer, paintData.width,
                    paintData.height, paintData.fullReRender);
                paintData.hasFrame = false;
                paintData.fullReRender = false;
            }
        }
    }

    @Override
    public void onPopupSize(CefBrowser browser, Rectangle size) {
        renderer_.onPopupSize(size);
    }

    @Override
    public void injectMouseMove(int x, int y, int mods, boolean left) {
        MouseEvent ev = new MouseEvent(dc_, left ? MouseEvent.MOUSE_EXITED : MouseEvent.MOUSE_MOVED,
            0, mods, x, y, 0, false);
        sendMouseEvent(ev);
    }

    @Override
    public void injectMouseButton(int x, int y, int mods, int btn, boolean pressed, int ccnt) {
        MouseEvent ev = new MouseEvent(dc_,
            pressed ? MouseEvent.MOUSE_PRESSED : MouseEvent.MOUSE_RELEASED, 0, mods, x, y, ccnt,
            false, btn);
        sendMouseEvent(ev);
    }

    @Override
    public void injectKeyTyped(char c, int mods) {
        KeyEvent ev = new KeyEvent(dc_, KeyEvent.KEY_TYPED, 0, mods, 0, c);
        sendKeyEvent(ev);
    }

    @Override
    public void injectKeyPressed(char c, int mods) {
        KeyEvent ev = new KeyEvent(dc_, KeyEvent.KEY_PRESSED, 0, mods, 0, c);
        sendKeyEvent(ev);
    }

    @Override
    public void injectKeyReleased(char c, int mods) {
        KeyEvent ev = new KeyEvent(dc_, KeyEvent.KEY_RELEASED, 0, mods, 0, c);
        sendKeyEvent(ev);
    }

    public void injectKeyEvent(KeyEvent e) {
        sendKeyEvent(e);
    }

    @Override
    public void injectMouseWheel(int x, int y, int mods, int amount, int rot) {
        MouseWheelEvent ev = new MouseWheelEvent(dc_, MouseEvent.MOUSE_WHEEL, 0, mods, x, y, 0,
            false, MouseWheelEvent.WHEEL_UNIT_SCROLL, amount, rot);
        sendMouseWheelEvent(ev);
    }

    @Override
    public void onCursorChange(CefBrowser browser, int cursorType) {
    }

    @Override
    public boolean startDragging(CefBrowser browser,
        CefDragData dragData,
        int mask,
        int x,
        int y) {
        return false;
    }

    @Override
    public void updateDragCursor(CefBrowser browser, int operation) {
    }

    @Override
    public void runJS(String script, String frame) {
        executeJavaScript(script, frame, 0);
    }

    @Override
    public void visitSource(IStringVisitor isv) {
        getSource(new StringVisitor(isv));
    }

    @Override
    public boolean isPageLoading() {
        return isLoading();
    }
}
