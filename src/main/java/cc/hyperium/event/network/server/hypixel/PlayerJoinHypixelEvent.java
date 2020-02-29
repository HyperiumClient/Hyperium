package cc.hyperium.event.network.server.hypixel;

import cc.hyperium.event.Event;

public class PlayerJoinHypixelEvent extends Event {

  private final String username;

  public PlayerJoinHypixelEvent(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
