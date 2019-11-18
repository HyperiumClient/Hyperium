var HypixelFriends = new HFController();
// main friend chat trigger
register("chat", function(event) {
	HypixelFriends.friendsList(event);
}).setCriteria("&r&9 &6Friends (Page ").setParameter("contains");

// confirmation command for removing a friend
register("command", function(name) {
	HypixelFriends.removeFriend(name);
}).setName("fremove");

// helper command for friend list
register("command", function(page) {
	ChatLib.command("f list " + page);
}).setName("flist");

// helper command for confirmation of removing a friend
register("command", function(name) {
	HypixelFriends.confirmRemove(name);
}).setName("fremoveConfirm");

// helper command to cancel removing a friend
register("command", function() {
	ChatLib.clearChat(69131, 69132, 69133, 69134);
}).setName("fremoveCancel");

function HFController() {
	this.Id = 69135;
	this.IdOffset = 0;
	this.getId = function() {
		return this.Id + this.IdOffset++;
	}

	this.friendsList = function(event) {
		if (!HypixelUtilities.getSetting("HypixelFriends", "toggle")) return;

		cancel(event);
		this.clearFriends();

		var lines = ChatLib.getChatMessage(event, true).split("\n");
		print(lines.length);
		for (var i = 0; i < lines.length; i++) {
			// breaks
			if (i == 0 || i == lines.length - 1) {
				new Message("&9&m" + ChatLib.getChatBreak("-")).setChatLineId(this.getId()).chat();
			}

			// page numbers
			else if (lines[i].indexOf("&r&9 &6Friends (Page ") != -1) {
				var line = ChatLib.removeFormatting(lines[i]);
				var num1 = Number(line.substring(line.indexOf("(Page ") + 6, line.indexOf(" of ")));
				var num2 = Number(line.substring(line.indexOf(" of ") + 4, line.indexOf(")")));

				var leftArrow = "&8<";
				var rightArrow = "&8>";

				if (num1 != NaN && num2 != NaN) {
					if (num1 != 1) {
						leftArrow = new TextComponent("&9<")
							.setClick("run_command", "/flist " + (num1-1))
							.setHoverValue("&7Page " + (num1-1));
					}

					if (num1 != num2) {
						rightArrow = new TextComponent("&9>")
							.setClick("run_command", "/flist " + (num1+1))
							.setHoverValue("&7Page " + (num1+1));
					}

					new Message(
						"                             ",
						leftArrow,
						" &6Friends (" + num1 + "/" + num2 + ") ",
						rightArrow
					).setChatLineId(this.getId()).chat();
				}
			}

			// idle in limbo
			else if (lines[i].indexOf("&r&e is idle in Limbo&r&9") != -1) {
				var name = lines[i].substring(0, lines[i].indexOf("&r&e is idle in Limbo&r&9"));

				if (name.indexOf(" ") == -1) {
					new Message(
						new TextComponent(" &e\u25A0 ").setClick("run_command", "/p invite " + ChatLib.removeFormatting(name)).setHoverValue("&7Party " + name),
						new TextComponent(name).setClick("suggest_command", "/w " + ChatLib.removeFormatting(name) + " ").setHoverValue("&7Message " + name),
						" &8idle"
					).setChatLineId(this.getId()).chat();
				}
			}

			// offline
			else if (lines[i].indexOf("&r&c is currently offline&r&9") != -1) {
				var name = lines[i].substring(0, lines[i].indexOf("&r&c is currently offline&r&9"));

				if (name.indexOf(" ") == -1) {
					new Message(
						" &c\u25A0 " + name + " ",
						new TextComponent("&4-").setClick("run_command", "/fremove " + name).setHoverValue("&7Remove " + name)
					).setChatLineId(this.getId()).chat();
				}
			}

			// unknown realm
			else if (lines[i].indexOf("&r&e is in an unknown realm&r&9") != -1) {
				var name = lines[i].substring(0, lines[i].indexOf("&r&e is in an unknown realm&r&9"));
				if (name.indexOf(" ") != -1) {
					new Message(
						new TextComponent(" &e\u25A0 ").setClick("run_command", "/p invite " + ChatLib.removeFormatting(name)).setHoverValue("&7Party " + name),
						new TextComponent(name).setClick("suggest_command", "/w " + ChatLib.removeFormatting(name) + " ").setHoverValue("&7Message " + name),
						" &8an unknown realm"
					).setChatLineId(this.getId()).chat();
				}
			}

			// in game or lobby
			else if (lines[i].indexOf("&r&e is in ") != -1) {
				var name = lines[i].substring(0, lines[i].indexOf("&r&e is in "));
				var game = lines[i].substring(lines[i].indexOf("&r&e is in ") + 11);
				
				if (name.indexOf(" ") == -1) {
					new Message(
						new TextComponent(" &a\u25A0 ").setClick("run_command", "/p invite " + ChatLib.removeFormatting(name)).setHoverValue("&7Party " + name),
						new TextComponent(name).setClick("suggest_command", "/w " + ChatLib.removeFormatting(name) + " ").setHoverValue("&7Message " + name),
						" &8" + game
					).setChatLineId(this.getId()).chat();
				}
			}

			// default
			else {
				new Message(lines[i]).setChatLineId(this.getId()).chat();
			}
		}
	}

	this.clearFriends = function() {
		if (!HypixelUtilities.getSetting("HypixelFriends", "delete old page")) return;

		for (var i = this.Id; i <= this.Id + this.IdOffset; i++) {
			ChatLib.clearChat(i);
		}
		this.Id = 69135;
		this.IdOffset = 0;
	}

	this.removeFriend = function(name) {
		if (!HypixelUtilities.getSetting("HypixelFriends", "ask to remove")) {
			ChatLib.command("f remove " + ChatLib.removeFormatting(name));
			return;
		}
		
		if (name != null) {
			new Message("&4&m" + ChatLib.getChatBreak("-")).setChatLineId(69131).chat();

			new Message(" &cAre you sure you want to remove " + name).setChatLineId(69132).chat();

			new Message(
				new TextComponent(" &2&l[Remove]").setClick("run_command", "/fremoveConfirm " + ChatLib.removeFormatting(name)).setHoverValue("&7Remove " + name),
				" ",
				new TextComponent("&4&l[Cancel]").setClick("run_command", "/fremoveCancel").setHoverValue("&7Cancel")
			).setChatLineId(69133).chat();

			new Message("&4&m" + ChatLib.getChatBreak("-")).setChatLineId(69134).chat();
		}
	}

	this.confirmRemove = function(name) {
		ChatLib.clearChat(69131, 69132, 69133, 69134);

		ChatLib.command("f remove " + name);
	}
}
