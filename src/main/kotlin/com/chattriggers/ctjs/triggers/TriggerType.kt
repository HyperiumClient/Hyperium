package com.chattriggers.ctjs.triggers

import com.chattriggers.ctjs.engine.ModuleManager

enum class TriggerType {
    // client
    CHAT,
    ACTION_BAR,
    TICK, STEP,
    GAME_UNLOAD, GAME_LOAD,
    CLICKED, DRAGGED,
    GUI_OPENED, SCREENSHOT_TAKEN,
    PICKUP_ITEM, DROP_ITEM,
    MESSAGE_SENT, TOOLTIP,

    // rendering
    RENDER_WORLD,
    BLOCK_HIGHLIGHT,
    RENDER_OVERLAY, RENDER_PLAYER_LIST, RENDER_BOSS_HEALTH, RENDER_DEBUG,
    RENDER_CROSSHAIR, RENDER_HOTBAR, RENDER_EXPERIENCE,
    RENDER_HEALTH, RENDER_FOOD, RENDER_MOUNT_HEALTH, RENDER_AIR,

    // world
    PLAYER_JOIN,
    PLAYER_LEAVE,
    SOUND_PLAY, NOTE_BLOCK_PLAY, NOTE_BLOCK_CHANGE,
    WORLD_LOAD, WORLD_UNLOAD,

    // misc
    COMMAND,
    OTHER;

    fun trigger(vararg args: Any?) {
        ModuleManager.trigger(this, *args)
    }

    fun triggerAll(vararg args: Any?) {
        trigger(*args)
    }
}