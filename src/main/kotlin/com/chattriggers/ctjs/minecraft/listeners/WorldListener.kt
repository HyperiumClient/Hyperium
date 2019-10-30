package com.chattriggers.ctjs.minecraft.listeners

import cc.hyperium.event.*
import cc.hyperium.event.client.TickEvent
import cc.hyperium.event.render.RenderHUDEvent
import cc.hyperium.event.world.WorldLoadEvent
import cc.hyperium.event.world.WorldUnloadEvent
import cc.hyperium.event.world.audio.SoundPlayEvent
import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.kotlin.KotlinListener
import paulscode.sound.Vector3D

@KotlinListener
object WorldListener {
    private var shouldTriggerWorldLoad: Boolean = false
    private var playerList: MutableList<String> = mutableListOf()

    @InvokeEvent
    fun onWorldLoad(event: WorldLoadEvent) {
        playerList.clear()
        shouldTriggerWorldLoad = true
    }

    @InvokeEvent
    fun onRenderGameOverlay(event: RenderHUDEvent) {
        // world load trigger
        if (!shouldTriggerWorldLoad) return

        TriggerType.WORLD_LOAD.triggerAll()
        shouldTriggerWorldLoad = false

        CTJS.sounds
            .stream()
            .filter { it.isListening }
            .forEach { it.onWorldLoad() }

        CTJS.sounds.clear()
    }

    @InvokeEvent
    fun onWorldUnload(event: WorldUnloadEvent) {
        TriggerType.WORLD_UNLOAD.triggerAll()
    }

    @InvokeEvent
    fun onSoundPlay(event: SoundPlayEvent) {
        val position = Vector3D(
            event.sound.xPosF,
            event.sound.yPosF,
            event.sound.zPosF
        )

        val vol = try {
            event.sound.volume
        } catch (ignored: Exception) {
            0
        }
        val pitch = try {
            event.sound.volume
        } catch (ignored: Exception) {
            1
        }

        TriggerType.SOUND_PLAY.triggerAll(
            event,
            position,
            event.sound.soundLocation.resourcePath,
            vol,
            pitch,
            ""
        )
    }

    //TODO: DETERMINE IF THESE ARE NEEDED OR NOT
    /*@InvokeEvent
    fun noteBlockEventPlay(event: NoteBlockEvent.Play) {
        val position = Vector3d(
                event.pos.x.toDouble(),
                event.pos.y.toDouble(),
                event.pos.z.toDouble()
        )

        TriggerType.NOTE_BLOCK_PLAY.triggerAll(
                event,
                position,
                event.note.name,
                event.octave
        )
    }*/

    /*@SubscribeEvent
    fun noteBlockEventChange(event: NoteBlockEvent.Change) {
        val position = Vector3d(
                event.pos.x.toDouble(),
                event.pos.y.toDouble(),
                event.pos.z.toDouble()
        )

        TriggerType.NOTE_BLOCK_CHANGE.triggerAll(
                event,
                position,
                event.note.name,
                event.octave
        )
    }*/

    @InvokeEvent
    fun updatePlayerList(event: TickEvent) {
        World.getAllPlayers().filter {
            !playerList.contains(it.getName())
        }.forEach {
            playerList.add(it.getName())
            TriggerType.PLAYER_JOIN.triggerAll(this)
            return@forEach
        }

        val ite = playerList.listIterator()

        while (ite.hasNext()) {
            val it = ite.next()

            try {
                World.getPlayerByName(it)
            } catch (exception: Exception) {
                playerList.remove(it)
                TriggerType.PLAYER_LEAVE.triggerAll(it)
                break
            }
        }
    }
}
