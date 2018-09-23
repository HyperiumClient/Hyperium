package com.chattriggers.ctjs.minecraft.libs.renderer

import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCTessellator
import com.chattriggers.ctjs.utils.kotlin.getRenderer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.util.vector.Vector2f

@External
class Shape(private var color: Int) {
    private var vertexes = mutableListOf<Vector2f>()
    private var drawMode = 9

    fun copy(): Shape = clone()
    fun clone(): Shape {
        val clone = Shape(this.color)
        clone.vertexes.addAll(this.vertexes)
        clone.setDrawMode(this.drawMode)
        return clone
    }

    fun getColor(): Int = this.color
    fun setColor(color: Int) = apply { this.color = color }

    fun getDrawMode(): Int = this.drawMode
    /**
     * Sets the GL draw mode of the shape. Possible draw modes are:<br>
     * 0 = points<br>
     * 1 = lines<br>
     * 2 = line loop<br>
     * 3 = line strip<br>
     * 5 = triangles<br>
     * 5 = triangle strip<br>
     * 6 = triangle fan<br>
     * 7 = quads<br>
     * 8 = quad strip<br>
     * 9 = polygon
     */
    fun setDrawMode(drawMode: Int) = apply { this.drawMode = drawMode }

    fun getVertexes(): List<Vector2f> = this.vertexes
    fun addVertex(x: Float, y: Float) = apply { this.vertexes.add(Vector2f(x, y)) }
    fun insertVertex(index: Int, x: Float, y: Float) = apply { this.vertexes.add(index, Vector2f(x, y)) }
    fun removeVertex(index: Int) = apply { this.vertexes.removeAt(index) }

    /**
     * Sets the shape as a line pointing from [x1, y1] to [x2, y2] with a thickness
     */
    fun setLine(x1: Float, y1: Float, x2: Float, y2: Float, thickness: Float) = apply {
        this.vertexes.clear()

        val theta = -Math.atan2((y2 - y1).toDouble(), (x2 - x1).toDouble())
        val i = Math.sin(theta).toFloat() * (thickness / 2)
        val j = Math.cos(theta).toFloat() * (thickness / 2)

        this.vertexes.add(Vector2f(x1 + i, y1 + j))
        this.vertexes.add(Vector2f(x2 + i, y2 + j))
        this.vertexes.add(Vector2f(x2 - i, y2 - j))
        this.vertexes.add(Vector2f(x1 - i, y1 - j))

        this.drawMode = 9
    }

    /**
     * Sets the shape as a circle with a center at [x, y]
     * with radius and number of steps around the circle
     */
    fun setCircle(x: Float, y: Float, radius: Float, steps: Int) = apply {
        this.vertexes.clear()

        val theta = 2 * Math.PI / steps
        val cos = Math.cos(theta).toFloat()
        val sin = Math.sin(theta).toFloat()

        var xHolder: Float
        var circleX = 1f
        var circleY = 0f

        for (i in 0 .. steps) {
            this.vertexes.add(Vector2f(x, y))
            this.vertexes.add(Vector2f(circleX * radius + x, circleY * radius + y))
            xHolder = circleX
            circleX = cos * circleX - sin * circleY
            circleY = sin * xHolder + cos * circleY
            this.vertexes.add(Vector2f(circleX * radius + x, circleY * radius + y))
        }

        this.drawMode = 5
    }

    fun draw() = apply {
        val a = (this.color shr 24 and 255).toFloat() / 255.0f
        val r = (this.color shr 16 and 255).toFloat() / 255.0f
        val g = (this.color shr 8 and 255).toFloat() / 255.0f
        val b = (this.color and 255).toFloat() / 255.0f

        val tessellator = MCTessellator.getInstance()
        val worldRenderer = tessellator.getRenderer()

        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        if (!Renderer.colorized)
            GlStateManager.color(r, g, b, a)

        worldRenderer.begin(this.drawMode, DefaultVertexFormats.POSITION)

        for (vertex in this.vertexes)
            worldRenderer.pos(vertex.x.toDouble(), vertex.y.toDouble(), 0.0).endVertex()

        tessellator.draw()
        GlStateManager.color(1f, 1f, 1f, 1f)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()

        Renderer.finishDraw()

        return this
    }
}