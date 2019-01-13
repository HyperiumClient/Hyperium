// Copyright (c) 2013 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

// Modified by montoyo for MCEF

package org.cef.browser;

import java.awt.Rectangle;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.montoyo.mcef.MCEF;
import net.montoyo.mcef.utilities.Log;
import org.lwjgl.opengl.GL11;


public class CefRenderer {

    private static final ArrayList<Integer> GL_TEXTURES;
    public int[] texture_id_;
    private boolean transparent_;
    private int view_width_;
    private int view_height_;
    private Rectangle popup_rect_;
    private Rectangle original_popup_rect_;

    protected CefRenderer(final boolean transparent) {
        this.texture_id_ = new int[1];
        this.view_width_ = 0;
        this.view_height_ = 0;
        this.popup_rect_ = new Rectangle(0, 0, 0, 0);
        this.original_popup_rect_ = new Rectangle(0, 0, 0, 0);
        this.transparent_ = transparent;
        this.initialize();
    }

    public static void dumpVRAMLeak() {
        Log.info(">>>>> MCEF: Beginning VRAM leak report");
        CefRenderer.GL_TEXTURES.forEach(tex -> Log.warning(">>>>> MCEF: This texture has not been freed: " + tex, new Object[0]));
        Log.info(">>>>> MCEF: End of VRAM leak report");
    }

    protected boolean isTransparent() {
        return this.transparent_;
    }

    protected void initialize() {
        GlStateManager.enableTexture2D();
        this.texture_id_[0] = GL11.glGenTextures();
        if (MCEF.CHECK_VRAM_LEAK) {
            CefRenderer.GL_TEXTURES.add(this.texture_id_[0]);
        }
        GlStateManager.bindTexture(this.texture_id_[0]);
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glTexEnvf(8960, 8704, 8448.0f);
        GlStateManager.bindTexture(0);
    }

    protected void cleanup() {
        if (this.texture_id_[0] != 0) {
            if (MCEF.CHECK_VRAM_LEAK) {
                CefRenderer.GL_TEXTURES.remove((Object)this.texture_id_[0]);
            }
            GL11.glDeleteTextures(this.texture_id_[0]);
        }
    }

    public void render(final double x1, final double y1, final double x2, final double y2) {
        if (this.view_width_ == 0 || this.view_height_ == 0) {
            return;
        }
        final Tessellator t = Tessellator.getInstance();
        final WorldRenderer wr = t.getWorldRenderer();
        GlStateManager.bindTexture(this.texture_id_[0]);
        wr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        wr.pos(x1, y1, 0.0).tex(0.0, 1.0).color(255, 255, 255, 255).endVertex();
        wr.pos(x2, y1, 0.0).tex(1.0, 1.0).color(255, 255, 255, 255).endVertex();
        wr.pos(x2, y2, 0.0).tex(1.0, 0.0).color(255, 255, 255, 255).endVertex();
        wr.pos(x1, y2, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
        t.draw();
        GlStateManager.bindTexture(0);
    }

    protected void onPopupSize(final Rectangle rect) {
        if (rect.width <= 0 || rect.height <= 0) {
            return;
        }
        this.original_popup_rect_ = rect;
        this.popup_rect_ = this.getPopupRectInWebView(this.original_popup_rect_);
    }

    protected Rectangle getPopupRectInWebView(final Rectangle rc) {
        if (rc.x < 0) {
            rc.x = 0;
        }
        if (rc.y < 0) {
            rc.y = 0;
        }
        if (rc.x + rc.width > this.view_width_) {
            rc.x = this.view_width_ - rc.width;
        }
        if (rc.y + rc.height > this.view_height_) {
            rc.y = this.view_height_ - rc.height;
        }
        if (rc.x < 0) {
            rc.x = 0;
        }
        if (rc.y < 0) {
            rc.y = 0;
        }
        return rc;
    }

    protected void clearPopupRects() {
        this.popup_rect_.setBounds(0, 0, 0, 0);
        this.original_popup_rect_.setBounds(0, 0, 0, 0);
    }

    protected void onPaint(final boolean popup, final Rectangle[] dirtyRects, final ByteBuffer buffer, final int width, final int height, final boolean completeReRender) {
        if (this.transparent_) {
            GlStateManager.enableBlend();
        }
        final int size = width * height << 2;
        if (size > buffer.limit()) {
            Log.warning("Bad data passed to CefRenderer.onPaint() triggered safe guards... (1)");
            return;
        }
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(this.texture_id_[0]);
        final int oldAlignement = GL11.glGetInteger(3317);
        GL11.glPixelStorei(3317, 1);
        if (!popup) {
            if (completeReRender || width != this.view_width_ || height != this.view_height_) {
                this.view_width_ = width;
                this.view_height_ = height;
                GL11.glTexImage2D(3553, 0, 6408, this.view_width_, this.view_height_, 0, 32993, 5121, buffer);
            }
            else {
                GL11.glPixelStorei(3314, this.view_width_);
                for (final Rectangle rect : dirtyRects) {
                    if (rect.x < 0 || rect.y < 0 || rect.x + rect.width > this.view_width_ || rect.y + rect.height > this.view_height_) {
                        Log.warning("Bad data passed to CefRenderer.onPaint() triggered safe guards... (2)");
                    }
                    else {
                        GL11.glPixelStorei(3316, rect.x);
                        GL11.glPixelStorei(3315, rect.y);
                        GL11.glTexSubImage2D(3553, 0, rect.x, rect.y, rect.width, rect.height, 32993, 5121, buffer);
                    }
                }
                GL11.glPixelStorei(3316, 0);
                GL11.glPixelStorei(3315, 0);
                GL11.glPixelStorei(3314, 0);
            }
        }
        else if (this.popup_rect_.width > 0 && this.popup_rect_.height > 0) {
            int skip_pixels = 0;
            int x = this.popup_rect_.x;
            int skip_rows = 0;
            int y = this.popup_rect_.y;
            int w = width;
            int h = height;
            if (x < 0) {
                skip_pixels = -x;
                x = 0;
            }
            if (y < 0) {
                skip_rows = -y;
                y = 0;
            }
            if (x + w > this.view_width_) {
                w -= x + w - this.view_width_;
            }
            if (y + h > this.view_height_) {
                h -= y + h - this.view_height_;
            }
            GL11.glPixelStorei(3314, width);
            GL11.glPixelStorei(3316, skip_pixels);
            GL11.glPixelStorei(3315, skip_rows);
            GL11.glTexSubImage2D(3553, 0, x, y, w, h, 32993, 5121, buffer);
            GL11.glPixelStorei(3314, 0);
            GL11.glPixelStorei(3316, 0);
            GL11.glPixelStorei(3315, 0);
        }
        GL11.glPixelStorei(3317, oldAlignement);
        GlStateManager.bindTexture(0);
    }

    public int getViewWidth() {
        return this.view_width_;
    }

    public int getViewHeight() {
        return this.view_height_;
    }

    static {
        GL_TEXTURES = new ArrayList<Integer>();
    }

}
