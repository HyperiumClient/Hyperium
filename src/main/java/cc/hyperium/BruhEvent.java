package cc.hyperium;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.entity.EntityEnterChunkEvent;
import cc.hyperium.event.interact.PlayerInteractEvent;
import cc.hyperium.event.world.chunk.ChunkLoadEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class BruhEvent {

    @InvokeEvent
    public void enterChunk(ChunkLoadEvent event) {
        System.out.println("User loaded chunk at " + event.getChunk().xPosition + " ; " + event.getChunk().zPosition);
    }

    @InvokeEvent
    public void interact(PlayerInteractEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        ItemStack heldItem = event.getPlayer().getHeldItem();
        if (event.getPlayer() == mc.thePlayer && heldItem != null) {
            if (heldItem.getItem().equals(Items.fishing_rod)) {
                System.out.println("Player " + event.getPlayer().getName() + " used " + heldItem.getDisplayName());
            }
        }
    }

    @InvokeEvent
    public void joinChunk(EntityEnterChunkEvent event) {
        if (event.getEntity() instanceof EntitySkeleton) {
            System.out.println("Skeleton entered chunk " + event.getNewChunkX() + " ; " + event.getNewChunkZ());
        }
    }
}
