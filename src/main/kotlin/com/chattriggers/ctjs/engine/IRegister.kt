package com.chattriggers.ctjs.engine

import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.triggers.*
import kotlin.reflect.full.memberFunctions

interface IRegister {
    /**
     * Helper method register a trigger. <br/>
     * Called by taking the original name of the method, i.e. `registerChat`,
     * removing the word register, and making the first letter lowercase.
     *
     * @param triggerType the type of trigger
     * @param method the name of the method or the actual method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun register(triggerType: String, method: Any): OnTrigger {
        val capitalizedName = triggerType.substring(0, 1).toUpperCase() + triggerType.substring(1)

        val func = this::class.memberFunctions.firstOrNull {
            it.name == "register$capitalizedName"
        }

        //println("params for func ${func?.name}: ${func?.parameters?.toString()}")

        return func?.call(this, method) as OnTrigger? ?: throw NoSuchMethodException()
    }

    /**
     * Registers a new trigger that runs before a chat message is received.<br></br>
     *
     *
     * Passes through multiple arguments:<br></br>
     * any number of chat criteria variables<br></br>
     * the chat event<br></br>
     * Available modifications:<br></br>
     * [OnRenderTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled<br></br>
     * [OnChatTrigger.setChatCriteria] Sets the chat criteria<br></br>
     * [OnChatTrigger.setParameter] Sets the chat parameter<br></br>
     * [OnTrigger.setPriority] Sets the priority<br></br>
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerChat(method: Any): OnChatTrigger {
        return OnChatTrigger(method, TriggerType.CHAT, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before an action bar message is received.<br></br>
     *
     *
     * Passes through multiple arguments:<br></br>
     * any number of chat criteria variables<br></br>
     * the chat event<br></br>
     * Available modifications:<br></br>
     * [OnRenderTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled<br></br>
     * [OnChatTrigger.setChatCriteria] Sets the chat criteria<br></br>
     * [OnChatTrigger.setParameter] Sets the chat parameter<br></br>
     * [OnTrigger.setPriority] Sets the priority<br></br>
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerActionBar(method: Any): OnChatTrigger {
        return OnChatTrigger(method, TriggerType.ACTION_BAR, getImplementationLoader())
    }

    /**
     * Registers a trigger that runs before the world loads.<br></br>
     *
     *
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerWorldLoad(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.WORLD_LOAD, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the world unloads.<br></br>
     *
     *
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerWorldUnload(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.WORLD_UNLOAD, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before a mouse button is being pressed or released.<br></br>
     *
     *
     * Passes through 4 arguments:<br></br>
     * mouse x<br></br>
     * mouse y<br></br>
     * mouse button<br></br>
     * mouse button state<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerClicked(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.CLICKED, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs while a mouse button is being held down.<br></br>
     *
     *
     * Passes through 5 arguments:<br></br>
     * mouse delta x<br></br>
     * mouse delta y<br></br>
     * mouse x<br></br>
     * mouse y<br></br>
     * mouse button<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerDragged(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.DRAGGED, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before a sound is played.<br></br>
     *
     *
     * Passes through 6 arguments:<br></br>
     * the sound event<br></br>
     * the sound event's position<br></br>
     * the sound event's name<br></br>
     * the sound event's volume<br></br>
     * the sound event's pitch<br></br>
     * the sound event's category's name<br></br>
     * Available modifications:<br></br>
     * [OnSoundPlayTrigger.setSoundNameCriteria] Sets the sound name criteria<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerSoundPlay(method: Any): OnSoundPlayTrigger {
        return OnSoundPlayTrigger(method, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before a noteblock is played.<br></br>
     *
     *
     * Passes through 4 arguments:<br></br>
     * the note block play event<br></br>
     * the note block play event's Vector3d position<br></br>
     * the note block play event's note's name<br></br>
     * the note block play event's octave<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerNoteBlockPlay(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.NOTE_BLOCK_PLAY, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before a noteblock is changed.<br></br>
     *
     *
     * Passes through 4 arguments:<br></br>
     * the note block change event<br></br>
     * the note block change event's Vector3d position<br></br>
     * the note block change event's note's name<br></br>
     * the note block change event's octave<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerNoteBlockChange(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.NOTE_BLOCK_CHANGE, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before every game tick.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * ticks elapsed<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerTick(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.TICK, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs in predictable intervals. (60 per second by default)<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * steps elapsed<br></br>
     * Available modifications:<br></br>
     * [OnStepTrigger.setFps] Sets the fps<br></br>
     * [OnStepTrigger.setDelay] Sets the delay in seconds<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerStep(method: Any): OnStepTrigger {
        return OnStepTrigger(method, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the world is drawn.<br></br>
     *
     *
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerRenderWorld(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.RENDER_WORLD, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the overlay is drawn.<br></br>
     *
     *
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerRenderOverlay(method: Any): OnRenderTrigger {
        return OnRenderTrigger(method, TriggerType.RENDER_OVERLAY, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the player list is being drawn.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * The render event<br></br>
     * Available modifications:<br></br>
     * [OnRenderTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerRenderPlayerList(method: Any): OnRenderTrigger {
        return OnRenderTrigger(method, TriggerType.RENDER_PLAYER_LIST, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the crosshair is being drawn.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * The render event<br></br>
     * Available modifications:<br></br>
     * [OnRenderTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerRenderCrosshair(method: Any): OnRenderTrigger {
        return OnRenderTrigger(method, TriggerType.RENDER_CROSSHAIR, getImplementationLoader())
    }

    /**
     * Registers a trigger that runs before the debug screen is being drawn.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * The render event<br></br>
     * Available modifications:<br></br>
     * [OnRenderTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerRenderDebug(method: Any): OnRenderTrigger {
        return OnRenderTrigger(method, TriggerType.RENDER_DEBUG, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the boss health bar is being drawn.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * The render event<br></br>
     * Available modifications:<br></br>
     * [OnRenderTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerRenderBossHealth(method: Any): OnRenderTrigger {
        return OnRenderTrigger(method, TriggerType.RENDER_BOSS_HEALTH, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the player's health is being drawn.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * The render event<br></br>
     * Available modifications:<br></br>
     * [OnRenderTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerRenderHealth(method: Any): OnRenderTrigger {
        return OnRenderTrigger(method, TriggerType.RENDER_HEALTH, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the player's food is being drawn.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * The render event<br></br>
     * Available modifications:<br></br>
     * [OnRenderTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerRenderFood(method: Any): OnRenderTrigger {
        return OnRenderTrigger(method, TriggerType.RENDER_FOOD, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the player's mount's health is being drawn.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * The render event<br></br>
     * Available modifications:<br></br>
     * [OnRenderTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerRenderMountHealth(method: Any): OnRenderTrigger {
        return OnRenderTrigger(method, TriggerType.RENDER_MOUNT_HEALTH, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the player's experience is being drawn.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * The render event<br></br>
     * Available modifications:<br></br>
     * [OnRenderTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerRenderExperience(method: Any): OnRenderTrigger {
        return OnRenderTrigger(method, TriggerType.RENDER_EXPERIENCE, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the player's hotbar is drawn.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * The render event<br></br>
     * Available modifications:<br></br>
     * [OnRenderTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerRenderHotbar(method: Any): OnRenderTrigger {
        return OnRenderTrigger(method, TriggerType.RENDER_HOTBAR, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the player's air level is drawn.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * The render event<br></br>
     * Available modifications:<br></br>
     * [OnRenderTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerRenderAir(method: Any): OnRenderTrigger {
        return OnRenderTrigger(method, TriggerType.RENDER_AIR, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the block highlight box is drawn.<br></br>
     *
     *
     * Passes through 2 arguments:<br></br>
     * The draw block highlight event<br></br>
     * The draw block highlight event's position<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerDrawBlockHighlight(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.BLOCK_HIGHLIGHT, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs after the game loads.<br></br>
     * This runs after the initial loading of the game directly after scripts are loaded and after "/ct load" happens.<br></br>
     *
     *
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerGameLoad(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.GAME_LOAD, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the game unloads.<br></br>
     * This runs before shutdown of the JVM and before "/ct load" happens.<br></br>
     *
     *
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerGameUnload(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.GAME_UNLOAD, getImplementationLoader())
    }

    /**
     * Registers a new command that will run the method provided.<br></br>
     *
     *
     * Passes through multiple arguments:<br></br>
     * The arguments supplied to the command by the user<br></br>
     * Available modifications:<br></br>
     * [OnCommandTrigger.setCommandName] Sets the command name<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerCommand(method: Any): OnCommandTrigger {
        return OnCommandTrigger(method, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs when a new gui is first opened.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * the gui opened event<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerGuiOpened(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.GUI_OPENED, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs when a player joins the world.<br></br>
     * Maximum is one per tick. Any extras will queue and run in later ticks.<br></br>
     * This trigger is asynchronous.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * the [com.chattriggers.ctjs.minecraft.wrappers.objects.PlayerMP] object<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerPlayerJoined(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.PLAYER_JOIN, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs when a player leaves the world.<br></br>
     * Maximum is one per tick. Any extras will queue and run in later ticks.<br></br>
     * This trigger is asynchronous.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * the name of the player that left<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerPlayerLeft(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.PLAYER_LEAVE, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before an item is picked up.<br></br>
     *
     *
     * Passes through 3 arguments:<br></br>
     * the [Item] that is picked up<br></br>
     * the [com.chattriggers.ctjs.minecraft.wrappers.objects.PlayerMP] that picked up the item<br></br>
     * the item's position vector<br></br>
     * the item's motion vector<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerPickupItem(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.PICKUP_ITEM, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before an item is dropped.<br></br>
     *
     *
     * Passes through 3 arguments:<br></br>
     * the [Item] that is dropped up<br></br>
     * the [com.chattriggers.ctjs.minecraft.wrappers.objects.PlayerMP] that dropped the item<br></br>
     * the item's position vector<br></br>
     * the item's motion vector<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerDropItem(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.DROP_ITEM, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before a screenshot is taken.<br></br>
     *
     *
     * Passes through 2 arguments:<br></br>
     * the name of the screenshot<br></br>
     * the screenshot event<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerScreenshotTaken(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.SCREENSHOT_TAKEN, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before a message is sent in chat.<br></br>
     *
     *
     * Passes through 2 arguments:<br></br>
     * the message event<br></br>
     * the message<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerMessageSent(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.MESSAGE_SENT, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before a message is sent in chat.<br></br>
     *
     *
     * Passes through 2 arguments:<br></br>
     * the list of lore to modify<br></br>
     * the [Item] that this lore is attached to.<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerItemTooltip(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.TOOLTIP, getImplementationLoader())
    }

    fun getImplementationLoader(): ILoader
}