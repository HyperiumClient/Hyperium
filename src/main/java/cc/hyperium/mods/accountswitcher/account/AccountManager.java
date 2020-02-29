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

package cc.hyperium.mods.accountswitcher.account;

import cc.hyperium.Hyperium;
import com.mojang.authlib.Agent;
import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.util.UUIDTypeAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.util.UUID;

public class AccountManager {

  private final UserAuthentication auth;

  public AccountManager() {
    // Create a random UUID for the session token.
    UUID uuid = UUID.randomUUID();
    // Create a new session service, for use.
    AuthenticationService service = new YggdrasilAuthenticationService(
        Minecraft.getMinecraft().getProxy(), uuid.toString());
    // Setup auth & the session service.
    auth = service.createUserAuthentication(Agent.MINECRAFT);
    service.createMinecraftSessionService();
  }

  public boolean setUser(String username, String password) {
    // Make sure the user isn't already logged in, to not waste time relogging.
    if (Minecraft.getMinecraft().getSession().getUsername().equalsIgnoreCase(username)) {
      Hyperium.LOGGER.warn("Tried to log in as the user already logged in, can't do that.");
      return false;
    }

    auth.logOut();
    auth.setUsername(username);
    auth.setPassword(password);

    // Make sure it can be logged in with.
    if (auth.canLogIn()) {
      try {
        auth.logIn();
        // Create a new session, using the new log in.
        Session session = new Session(auth.getSelectedProfile().getName(),
            UUIDTypeAdapter.fromUUID(auth.getSelectedProfile().getId()),
            auth.getAuthenticatedToken(), auth.getUserType().getName());
        // Set the session.
        Minecraft.getMinecraft().setSession(session);
      } catch (AuthenticationException e) {
        e.printStackTrace();
        return false;
      }
    } else {
      // :(
      Hyperium.LOGGER.warn("Can't log in with this account.");
      return false;
    }

    return true;
  }

  public void setOffline(String username) {
    // Log out of the current session
    auth.logOut();
    // Create a new offline session, and set it
    Session session = new Session(username, username, "0", "legacy");
    Minecraft.getMinecraft().setSession(session);
  }

}
