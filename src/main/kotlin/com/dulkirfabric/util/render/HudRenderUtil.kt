package com.dulkirfabric.util.render

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.events.HudRenderEvent
import meteordevelopment.orbit.EventHandler
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import java.time.Duration

object HudRenderUtil {

    private var curTitle: Text? = null
    private var clearTime: Long = -1

    private fun drawTitle(context: DrawContext, content: Text) {
        val matrices = context.matrices
        val tr = mc.textRenderer
        val w = tr.getWidth(content)
        val sf: Float = mc.window.scaledWidth / w.toFloat() / 3
        matrices.pushMatrix()
        matrices.translate(mc.window.scaledWidth / 3f, mc.window.scaledHeight / 2f)
        matrices.scale(sf, sf)
        context.drawText(tr, content, 0, -tr.fontHeight / 2, -1, true)
        matrices.popMatrix()
    }

    fun drawTitle(content: Text, duration: Duration) {
        curTitle = content
        clearTime = System.currentTimeMillis() + duration.toMillis()
    }

    @EventHandler
    fun onHudRender(event: HudRenderEvent) {
        val content = curTitle ?: return
        if (System.currentTimeMillis() >= clearTime) {
            curTitle = null
            return
        }
        drawTitle(event.context, content)
    }
}