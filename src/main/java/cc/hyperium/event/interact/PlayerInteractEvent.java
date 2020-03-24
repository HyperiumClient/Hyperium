package cc.hyperium.event.interact;

import cc.hyperium.event.CancellableEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class PlayerInteractEvent extends CancellableEvent {

  private final EntityPlayer player;
  private final Action action;
  private final World world;
  private final BlockPos pos;
  private final EnumFacing face;
  private final Vec3 localPos;

  public PlayerInteractEvent(EntityPlayer player, Action action, BlockPos pos, World world,
      EnumFacing face) {
    this(player, action, pos, world, face, null);
  }

  public PlayerInteractEvent(EntityPlayer player, Action action, BlockPos pos, World world,
      EnumFacing face, Vec3 localPos) {
    this.player = player;
    this.action = action;
    this.pos = pos;
    this.face = face;
    this.world = world;
    this.localPos = localPos;
  }

  public EntityPlayer getPlayer() {
    return player;
  }

  public Action getAction() {
    return action;
  }

  public World getWorld() {
    return world;
  }

  public BlockPos getPos() {
    return pos;
  }

  public EnumFacing getFace() {
    return face;
  }

  public Vec3 getLocalPos() {
    return localPos;
  }

  public enum Action {
    RIGHT_CLICK_AIR,
    RIGHT_CLICK_BLOCK,
    LEFT_CLICK_BLOCK
  }
}
