/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.client.TickEvent;
import cc.hyperium.event.gui.GuiClickEvent;
import cc.hyperium.event.render.RenderHUDEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static cc.hyperium.gui.HyperiumGui.*;

public class NotificationCenter extends Gui {
    /**
     * List of notifications to be displayed
     */
    private final Queue<Notification> notifications = new LinkedList<>();
    /**
     * The notification currently being displayed
     */
    private Notification currentNotification;
    /**
     * Font renderer to use
     */
    private FontRenderer fontRenderer;

    public NotificationCenter() {

    }

    @InvokeEvent
    public void tick(TickEvent ev) {
        if (currentNotification == null || currentNotification.tick()) currentNotification = notifications.poll();
    }

    @InvokeEvent
    public void onRenderTick(RenderHUDEvent event) {
        if (currentNotification != null) currentNotification.render();
    }

    @InvokeEvent
    public void onClick(GuiClickEvent event) {
        if (currentNotification != null && currentNotification.clickedCallback != null) {
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            int left = currentNotification.getX(sr);
            int top = currentNotification.getY(sr);
            int right = left + currentNotification.width;
            int bottom = top + currentNotification.height;
            int mouseX = event.getMouseX();
            int mouseY = event.getMouseY();

            if (mouseX > left && mouseX < right && mouseY > top && mouseY < bottom) {
                currentNotification.clickedCallback.run();
            }
        }
    }

    /**
     * Create a notification queued to be displayed
     *
     * @param title       Title of the notification
     * @param description Description of the notification
     * @param seconds     Seconds the notification should be displayed for
     * @return The new notification
     */
    public Notification display(String title, String description, float seconds) {
        return display(title, description, seconds, null, null, null);
    }

    /**
     * Create a notification queued to be displayed
     *
     * @param title       Title of the notification
     * @param description Description of the notification
     * @param seconds     Seconds the notification should be displayed for
     * @param img         Image to be displayed with the notification
     * @param callback    Callback to be ran when the user clicks on the notification
     * @return The new notification
     */
    public Notification display(String title, String description, float seconds, @Nullable BufferedImage img, @Nullable Runnable callback, @Nullable Color highlightColor) {
        Notification notif = new Notification(title, description, (int) (seconds * 20), img, callback, highlightColor);

        try {
            notifications.add(notif);
            return notif;
        } catch (Exception e) {
            Hyperium.LOGGER.error("Can't display notification!", e);
        }

        return null;
    }

    /**
     * Set the font renderer to be used to default font renderer
     */
    private void setDefaultFontRenderer() {
        if (fontRenderer == null) fontRenderer = Minecraft.getMinecraft().fontRendererObj;
    }

    /**
     * Notification class
     */
    public class Notification {
        /**
         * Width of this notification
         */
        int width = 175;

        /**
         * Margins between the bottom of the notification and the bottom of the screen
         */
        int bottomMargins = 15;

        /**
         * Margins between text or images (whichever is applicable) from the right of the screen
         */
        int rightMargins = 5;

        /**
         * Padding between the top of the notification and title text
         */
        int topPadding = 5;

        /**
         * Size of every image. Should always be 256.
         */
        private int imgSize = 256;

        /**
         * Maximum number of lines descriptions can span. After this, they will be trimmed
         *
         * @see HyperiumGui#trimString(String, int, FontRenderer, boolean)
         */
        private int maxDescriptionLines = 4;

        /**
         * Width of the highlight bar
         */
        private int highlightBarWidth = 5;

        /**
         * Margins between the highlight bar and the text next to it
         */
        private int highlightBarMargins = 5;

        /**
         * Spacing in pixels between each line
         */
        private int lineSpacing = 1;

        /**
         * Margins between the top of the image and the title
         */
        private int imgTopMargins = 5;

        /**
         * Height of this notification
         */
        int height = 40;

        /**
         * Title text displayed for this notification
         * Max lines is always 1
         */
        private String title;

        /**
         * Description text for this notification
         *
         * @see #maxDescriptionLines
         */
        private String description;

        /**
         * Ticks left until this notification goes bye-bye
         */
        private int ticksLeft;

        /**
         * Percentage complete of this notifications lifecycle
         */
        private float percentComplete;

        /**
         * Upper threshold used for easeout of the notification
         */
        private int topThreshold;

        /**
         * Lower threshold used for easeout of the notification
         */
        private int lowerThreshold;

        /**
         * Ran when the user clicks on this notification, if applicable
         */
        private Runnable clickedCallback;

        /**
         * Image rendered with this notification, if applicable
         */
        private DynamicTexture img;

        /**
         * What to scale the image to
         * Should be around <code>height / imgSize</code>
         */
        private double imgScale = 0.125;

        /**
         * Color of the highlight next to the notification
         */
        private Color highlightColor = new Color(149, 201, 144);

        /**
         * Color of the description text
         */
        private Color descriptionColor = new Color(80, 80, 80);

        /**
         * Create a new notification
         * Use {@link NotificationCenter#display} if you wish to create a notification.
         *
         * @param title           Title of the notification
         * @param description     Description of the notification
         * @param ticks           Ticks to display this notification for
         * @param img             Image to display on this notification
         * @param clickedCallback Callback to run when the notification is clicked
         * @throws IllegalArgumentException Title is null
         * @throws IllegalArgumentException description is null
         * @throws IllegalArgumentException Ticks is less than or equal to 0
         */
        Notification(String title, String description, int ticks, BufferedImage img, Runnable clickedCallback, Color highlightColor) {
            assert title != null : "Title cannot be null!";
            assert description != null : "Description cannot be null!";
            assert ticks > 0 : "Ticks cannot be less than or equal to 0!";

            ticksLeft = ticks;

            int fifth = ticks / 5;
            topThreshold = ticks - fifth;
            lowerThreshold = fifth;
            percentComplete = 0.0F;

            setClickedCallback(clickedCallback)
                .setTitle(title)
                .setDescriptionText(description)
                .setHighlightColor(highlightColor)
                .setImage(img);
        }

        /**
         * Set the display image for this notification
         *
         * @param img Img to display
         */
        void setImage(BufferedImage img) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                this.img = img != null ? new DynamicTexture(img) : null;
            });
        }

        /**
         * Set the callback to be ran when this notification is clicked
         *
         * @param runnable Runnable to run when clicked
         * @return This
         */
        Notification setClickedCallback(Runnable runnable) {
            clickedCallback = runnable;
            return this;
        }

        /**
         * Set the title of this notification
         *
         * @param title Title
         * @return This
         */
        Notification setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Set the highlight color for this notification
         *
         * @param highlightColor New color
         * @return This
         */
        Notification setHighlightColor(Color highlightColor) {
            this.highlightColor = (highlightColor != null ? highlightColor : new Color(149, 201, 144));
            return this;
        }

        /**
         * Set the description text for this notification
         * Also readjusts the height of notifications
         *
         * @param description Description text
         * @return This
         */
        Notification setDescriptionText(String description) {
            this.description = description;
            adjustHeight();
            return this;
        }

        /**
         * Adjusts the height of the notification to fit all text/images/etc
         */
        void adjustHeight() {
            setDefaultFontRenderer();
            int lineCount = fontRenderer.listFormattedStringToWidth(description, getWrapWidth()).size();
            int totalHeight = (fontRenderer.FONT_HEIGHT + lineSpacing) * (Math.min(maxDescriptionLines, lineCount) + 1) + topPadding;
            if (totalHeight > height) height = totalHeight;
        }

        /**
         * Get the width at which point description text needs to wrap
         *
         * @return The width at which description text should wrap
         */
        private int getWrapWidth() {
            int descRightMargins = (img == null ? rightMargins : rightMargins * 2); // Double right margins if image is there
            // Width that text is permitted to stretch across before wrapping/stopping
            return (int) (width - descRightMargins - imgSize * imgScale - highlightBarMargins - highlightBarWidth);
        }

        /**
         * Tick down this notification
         *
         * @return Whether this notification is complete
         */
        boolean tick() {
            ticksLeft--;

            return ticksLeft <= 0;
        }

        /**
         * Update percentage completed on the notification
         *
         * @return New percentage completed
         */
        float updatePercentage() {
            return percentComplete = clamp(
                easeOut(
                    percentComplete,
                    ticksLeft < lowerThreshold ? 0.0f :
                        ticksLeft > topThreshold ? 1.0f : ticksLeft,
                    0.01f,
                    8f
                ),
                0.0f,
                1.0f
            );
        }

        /**
         * Get the X location of the top left corner of the notification
         *
         * @return X location
         */
        int getX(ScaledResolution sr) {
            return (int) (sr.getScaledWidth() - (width * updatePercentage()));
        }

        /**
         * Get the Y location of the top left corner of the notification
         *
         * @return Y Location
         */
        int getY(ScaledResolution sr) {
            return sr.getScaledHeight() - height - bottomMargins;
        }

        /**
         * Render this notification
         */
        void render() {
            if (ticksLeft <= 0) return;
            if (!Settings.SHOW_INGAME_NOTIFICATION_CENTER) return;
            setDefaultFontRenderer();

            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

            // Update percentage -- Called in getX()
            // updatePercentage();
            int x = getX(sr);
            int y = getY(sr);

            int alpha = (int) clamp(percentComplete * 255, 127, 255);

            // Background
            Gui.drawRect(x, y, x + width, y + height, new Color(30, 30, 30, alpha).getRGB());
            GlStateManager.enableBlend();

            // Highlight color
            setHighlightColor(highlightColor); // Anti-NPE
            drawRect(x, y, x + highlightBarWidth, y + height, highlightColor.getRGB() | alpha << 24);
            GlStateManager.enableBlend();

            // Title Text
            fontRenderer.drawString(trimString(String.valueOf(title), width - rightMargins,
                null, true), x + highlightBarWidth + highlightBarMargins, y + topPadding, 0xFFFFFF | alpha << 24);

            // Description text
            if (descriptionColor == null) descriptionColor = new Color(80, 80, 80); // Anti-NPE
            // Don't draw if no lines
            int wrapWidth = getWrapWidth();
            // Trim & split into multiple lines
            List<String> lines = fontRenderer.listFormattedStringToWidth(String.valueOf(description), wrapWidth);
            if (lines.size() > maxDescriptionLines) { // Trim size & last line if overflow
                String nextLine = lines.get(maxDescriptionLines); // The line that would appear after the last one
                lines = lines.subList(0, maxDescriptionLines);
                // Next line is appended to guarantee three ellipsis on the end of the string
                lines.set(lines.size() - 1, trimString(lines.get(lines.size() - 1) + " " + nextLine,
                    wrapWidth, null, true));
            }

            // Draw lines
            int currentLine = 0;
            for (String line : lines) {
                fontRenderer.drawString(String.valueOf(line),
                    x + highlightBarWidth + highlightBarMargins,
                    y + topPadding + fontRenderer.FONT_HEIGHT + lineSpacing + fontRenderer.FONT_HEIGHT * currentLine,
                    descriptionColor.getRGB() | alpha << 24);

                if (++currentLine >= maxDescriptionLines) break; // stop if too many lines have gone by
            }

            // Notification Image
            if (img != null) {
                imgScale = (double) (height - topPadding - fontRenderer.FONT_HEIGHT - imgTopMargins) / imgSize;

                if (imgScale * imgSize > (double) width / 4) {
                    imgScale = ((double) width / 4) / imgSize; // Limit to 25% of width
                }

                GlStateManager.color(1, 1, 1, 1);
                GlStateManager.scale(imgScale, imgScale, imgScale);
                GlStateManager.bindTexture(img.getGlTextureId());
                GlStateManager.enableTexture2D();
                drawTexturedModalRect(
                    (float) ((x + width - rightMargins) / imgScale - imgSize),
                    (float) (y / imgScale + (((height + fontRenderer.FONT_HEIGHT) / imgScale) - imgSize) / 2),
                    0,
                    0,
                    imgSize,
                    imgSize);
                GlStateManager.scale(1 / imgScale, 1 / imgScale, 1 / imgScale);
            } else {
                imgScale = 0;
            }
        }
    }
}
