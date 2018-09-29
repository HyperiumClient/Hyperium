package cc.hyperium.event;

import com.google.common.base.Preconditions;

import org.jetbrains.annotations.NotNull;

/**
 * Called when the player joins hypixel
 */
public final class JoinHypixelEvent extends Event {

    @NotNull
    private final ServerVerificationMethod method;

    public JoinHypixelEvent(@NotNull ServerVerificationMethod method) {
        Preconditions.checkNotNull(method, "method");

        this.method = method;
    }

    @NotNull
    public final ServerVerificationMethod getMethod() {
        return this.method;
    }

    /**
     * All the methods used by HypixelDetector to detect Hypixel or Badlion
     * This is used by the above two events
     */
    public enum ServerVerificationMethod {
        IP,
        MOTD;
    }
}
