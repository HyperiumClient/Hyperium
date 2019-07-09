package cc.hyperium.mods.togglechat.toggles.defaults;

import cc.hyperium.mods.togglechat.toggles.ToggleBase;

import java.util.LinkedList;
import java.util.regex.Pattern;

public class TypeOfficer extends ToggleBase {

    private final Pattern pattern = Pattern.compile(
        "(O|Officer) > (?<rank>\\[.+] )?(?<player>\\S{1,16})?(?<role>\\[.+])*: (?<message>.*)");

    private boolean enabled = false;

    @Override
    public String getName() {
        return "Officer";
    }

    @Override
    public boolean shouldToggle(String message) {
        return this.pattern.matcher(message).find();
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public LinkedList<String> getDescription() {
        return asLinked(
            "Toggles guild officer",
            "messages"
        );
    }
}
