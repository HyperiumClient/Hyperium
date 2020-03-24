package cc.hyperium.event.network.server.hypixel;

import cc.hyperium.event.Event;

public class PlayerLeaveHypixelEvent extends Event {

  private final String username;

  public PlayerLeaveHypixelEvent(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
