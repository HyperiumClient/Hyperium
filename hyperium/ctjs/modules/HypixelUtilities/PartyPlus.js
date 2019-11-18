register("chat", function(ppNum, ppMembers, event) {
	if (!HypixelUtilities.getSetting("CleanChat", "PartyPlus")) return;
	
	cancel(event);

	var list = ppMembers.split(" \u25CF ");
	var leader = false;
	if(removeRank(list[0]).trim().equals(Player.getName())) {
		leader = true;
	}

	ChatLib.chat("&aParty members (" + ppNum + ")");

	if (leader) {
		new Message(
			new TextComponent("&9[Party Warp]").setClick("run_command", "/p warp").setHoverValue("&7Warp party"),
			" ",
			new TextComponent("&4[Disband]").setClick("run_command", "/p disband").setHoverValue("&7Disband the party")
		).chat();
		new TextComponent("&e[Toggle All Invite]").setClick("run_command", "/p settings allinvite").setHoverValue("&7Toggle all invite").chat();
		new TextComponent("&c[Kick Offline]").setClick("run_command", "/p kickoffline").setHoverValue("&7Kick all offline party members").chat();
	}

	ChatLib.chat(" ");
	for (var i = 0; i < list.length - 1; i++) {
		var arrow = "&c\u00BB ";
		if (list[i].endsWith("&a")) {
			arrow = "&a\u00BB ";
		}

		if(leader && i != 0) {
			new Message(
				arrow + list[i].trim(),
				" ",
				new TextComponent("&a[&2^&a]").setClick("run_command", "/p promote " + removeRank(list[i]).trim()).setHoverValue("&7Promote " + list[i].trim()),
				" ",
				new TextComponent("&c[&4-&c]").setClick("run_command", "/p remove " + removeRank(list[i]).trim()).setHoverValue("&7Kick " + list[i].trim())
			).chat();
		} else {
			ChatLib.chat(arrow + list[i].trim());
		}
	}
}).setChatCriteria("&aParty members (${ppNum}): ${ppMembers}");

// helper function to remove rank and formatting from a username
function removeRank(name) {
    name = ChatLib.removeFormatting(name);
    return name.replace(/\[.+]/g, "");
}
