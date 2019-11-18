function PMMWindow(name) {
  this.x = 0;
  this.y = 0;
  this.width = 200;
  this.height = 150;

  this.selected = false;
  this.hovered = false;

  this.selectedWidth = false;
  this.hoveredWidth = false;

  this.selectedHeight = false;
  this.hoveredHeight = false;

  this.input = "";
  this.messages = [];

  this.topBar = new Rectangle(0xff1B1A21, 0, 0, 0, 12);
  this.name = new Text(ChatLib.addColor(name), 0, 0);
  this.inputText = new Text("", 0, 0);
  this.window = new Rectangle(0xaa36353d, 0, 0, 0, 0).setShadow(0x501B1A21, 3, 3);
  this.inputBox = new Rectangle(0x0083818B, 0, 0, 0, 0).setOutline(0xff1B1A21, 1);

  this.pinned = false;
  this.hoveredPin = false;

  this.exit = new Text("x", 0, 0);
  this.hoveredExit = false;
  this.shouldExit = false;

  this.minimize = new Text("-", 0, 0);
  this.hoveredMinimize = false;
  this.minimized = false;

  this.showCursor = false;
  this.scroll = 0;
  this.scrollTo = 0;

  this.addMessage = function(recieved, message) {
    this.messages.push({recieved: recieved, message: ChatLib.removeFormatting(message), read: false});
    this.scroll = 0;
    this.scrollTo = 0;
    return this;
  }

  this.typed = function(char, key) {
    if (!this.selected) {return;}

    if (key == 14) { // backspace
      this.input = this.input.slice(0, -1);
      return;
    } else if (key == 28) { // enter
      ChatLib.command("msg " + removeRank(name).trim() + " " + this.input);
      print("/msg " + removeRank(name).trim() + " " + this.input);
      this.input = "";
      return;
    } else if (key == 47) {
      if (Client.getMinecraft().field_142025_a) {
        if (Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220)) {
          var transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
          if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            this.input += transferable.getTransferData(DataFlavor.stringFlavor);
          }
          return;
        }
      } else {
        if (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157)) {
          var transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
          if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            this.input += transferable.getTransferData(DataFlavor.stringFlavor);
          }
          return;
        }
      }
    } else if (!PMM.checkKey(key)) {
      return;
    }

    this.input += char;
  }

  this.draw = function() {
    if (this.selected) {
      this.name.setColor(0xffffffff);
      var color = PMMSettings.getSetting("Background", "Color");
      var alpha = PMMSettings.getSetting("Background", "Selected Alpha");
      this.window.setColor(
        Renderer.color(color[0], color[1], color[2], alpha)
      ).setShadow(0x501B1A21, 3, 3);
    } else {
      this.name.setColor(0x80ffffff);
      var color = PMMSettings.getSetting("Background", "Color");
      var alpha = PMMSettings.getSetting("Background", "Unselected Alpha");
      this.window.setColor(
        Renderer.color(color[0], color[1], color[2], alpha)
      ).setShadow(0x501B1A21, 3, 3);
    }

    if (this.minimized) {this.window.setHeight(12);}
    else {this.window.setHeight(this.height);}
    this.window
      .setX(this.x)
      .setY(this.y)
      .setWidth(this.width)
      .draw();



    if (!this.minimized) {
      var yOffset = 0;
      for (var i = this.messages.length - 1; i >= 0; i--) {
        this.messages[i].read = true;
        var messageText = new Text(this.messages[i].message, 0, 0).setWidth(this.width - 20);
        messageText.setY(this.y + this.height - messageText.getHeight() - 27 + yOffset + this.scroll);

        var y1 = messageText.getY();
        var y2 = y1 + messageText.getHeight() + 2;

        if (y1 < this.y) {y1 = this.y;}
        if (y2 < this.y + 12) {continue;}
        if (y2 > this.y + this.height - 18) {y2 = this.y + this.height - 18}

        if (this.messages[i].recieved) {
          messageText.setX(this.x + 7)

          var x1 = this.x + 5;
          var x2 = x1 + messageText.getMaxWidth() + 3;

          var color = PMMSettings.getSetting("Messages", "Recieved Color");
          new Shape(Renderer.color(color[0], color[1], color[2]))
            .addVertex(x2, y2)
            .addVertex(x2, y1)
            .addVertex(x1, y1)
            .addVertex(x1, y2 - 5)
            .addVertex(x1 - 5, y2)
            .draw();

          yOffset -= messageText.getHeight() + 4;
          for (var j = 0; j < messageText.getLines().length; j++) {
            var drawX = messageText.getX()
            var drawY = messageText.getY() + (j * 9) + 2
            if (drawY < this.y || drawY > this.y + this.height - 10) {continue;}
            var lineText = new Text(messageText.getLines()[j], drawX, drawY);

            color = PMMSettings.getSetting("Messages", "Recieved Text Color");
            lineText.setColor(Renderer.color(color[0], color[1], color[2])).draw();
          }
        } else {
          messageText.setX(this.x + this.width - 6)

          var x2 = messageText.getX() + 1;
          var x1 = x2 - messageText.getMaxWidth() - 3;

          var color = PMMSettings.getSetting("Messages", "Sent Color");
          new Shape(Renderer.color(color[0], color[1], color[2]))
            .addVertex(x2, y2 - 5)
            .addVertex(x2, y1)
            .addVertex(x1, y1)
            .addVertex(x1, y2)
            .addVertex(x2 + 5, y2)
            .draw();

          yOffset -= messageText.getHeight() + 4;
          for (var j = 0; j < messageText.getLines().length; j++) {
            var drawX = messageText.getX()
            var drawY = messageText.getY() + (j * 9) + 2
            if (drawY < this.y || drawY > this.y + this.height - 10) {continue;}
            var lineText = new Text(messageText.getLines()[j], drawX, drawY).setAlign("right");
            if (!this.selected && !this.pinned) {lineText.setColor(0xff808080);}

            color = PMMSettings.getSetting("Messages", "Sent Text Color");
            lineText.setColor(Renderer.color(color[0], color[1], color[2])).draw();
          }
        }

        if (i == 0) {
          if (-yOffset > this.height && this.scrollTo > Math.abs(yOffset) - this.height + 37) {
            this.scrollTo = easeOut(this.scrollTo, Math.abs(yOffset) - this.height + 37, 50, 1);
          }
        }
      }
    }

    var color = PMMSettings.getSetting("Titlebar", "Color");
    this.topBar
      .setX(this.x)
      .setY(this.y)
      .setWidth(this.width)
      .setColor(Renderer.color(color[0], color[1], color[2]))
      .draw()

    if (this.pinned) {
      Renderer.translate(this.x - 1, this.y + 8);
      Renderer.scale(1.35);
      Renderer.rotate(-45);
      PMM.pinned.draw();
    } else {
      Renderer.translate(this.x + 2, this.y + 1);
      Renderer.scale(1.3);
      PMM.pin.draw();
    }

    this.name
      .setX(this.x + 14)
      .setY(this.y + 2)
      .draw();

    var unread = 0;
    this.messages.forEach(function(element) {
      if (!element.read) {unread++;}
    });
    if (unread > 0) {
      var x = this.x + 16 + Renderer.getStringWidth(ChatLib.removeFormatting(this.name.getString()));
      var y = this.y + 1
      var color = PMMSettings.getSetting("Titlebar", "Notification Color");
      new Shape(Renderer.color(color[0], color[1], color[2]))
        .setCircle(x + 5, y + 5, 4.5, 20)
        .draw();
      new Shape(Renderer.color(color[0], color[1], color[2]))
        .setCircle(x + Renderer.getStringWidth(unread) + 5, y + 5, 4.5, 20)
        .draw();
      new Rectangle(
        Renderer.color(color[0], color[1], color[2]), 
        x + 5, y + 0.5, 
        Renderer.getStringWidth(unread) - 1.5, 9
      ).draw();
      new Text(unread, x + 5, y + 1.5).draw();
    }

    if (this.hoveredExit) {
      var color = PMMSettings.getSetting("Titlebar", "Exit Color");
      this.exit.setColor(Renderer.color(color[0], color[1], color[2]));
    } else {this.exit.setColor(0xff808080);}
    this.exit
      .setX(this.x + this.width - 10)
      .setY(this.y + 2)
      .draw();

    if (this.hoveredMinimize) {
      var color = PMMSettings.getSetting("Titlebar", "Minimize Color");
      this.minimize.setColor(Renderer.color(color[0], color[1], color[2]));
    } else {this.minimize.setColor(0xff808080);}
    if (this.minimized) {this.minimize.setY(this.y);}
    else {this.minimize.setY(this.y + 4);}
    this.minimize
      .setX(this.x + this.width - 22)
      .draw();

    if (!this.minimized) {
      this.inputBox
        .setX(this.x + 2)
        .setY(this.y + this.height - 23)
        .setWidth(this.width - 4)
        .setHeight(20)
        .draw();

      if (this.showCursor) {this.inputText.setString(this.input + "_")}
      else {this.inputText.setString(this.input);}
      this.inputText
        .setX(this.x + 3)
        .setY(this.y + this.height - 22)
        .setWidth(this.width - 4)
        .draw();
    }
  }

  this.handleHovered = function() {
    this.hoveredExit = Client.getMouseX() > this.x + this.width - 10
      && Client.getMouseX() < this.x + this.width
      && Client.getMouseY() > this.y
      && Client.getMouseY() < this.y + 12;

    this.hoveredMinimize = Client.getMouseX() > this.x + this.width - 22
      && Client.getMouseX() < this.x + this.width - 10
      && Client.getMouseY() > this.y
      && Client.getMouseY() < this.y + 12;

    if (this.minimized) {
      this.hovered = Client.getMouseX() > this.x
        && Client.getMouseX() < this.x + this.width + 5
        && Client.getMouseY() > this.y
        && Client.getMouseY() < this.y + 12 + 5;
    } else {
      this.hovered = Client.getMouseX() > this.x
        && Client.getMouseX() < this.x + this.width + 5
        && Client.getMouseY() > this.y
        && Client.getMouseY() < this.y + this.height + 5;
    }

    this.hoveredPin = Client.getMouseX() > this.x
      && Client.getMouseX() < this.x + 12
      && Client.getMouseY() > this.y
      && Client.getMouseY() < this.y + 12;

    if (this.minimized) {return;}

    this.hoveredWidth = Client.getMouseX() > this.x + this.width
      && Client.getMouseX() < this.x + this.width + 5
      && Client.getMouseY() > this.y
      && Client.getMouseY() < this.y + this.height + 5;

    this.hoveredHeight = Client.getMouseX() > this.x
      && Client.getMouseX() < this.x + this.width + 5
      && Client.getMouseY() > this.y + this.height
        && Client.getMouseY() < this.y + this.height + 5;

    if (this.hoveredWidth && this.hoveredHeight) {
      new Shape(0xffffffff)
        .setLine(
          Client.getMouseX() + 6,
          Client.getMouseY() + 8,
          Client.getMouseX() + 10,
          Client.getMouseY() + 12,
          1
        ).draw();
    } else {
      if (this.hoveredWidth) {
        new Shape(0xffffffff)
          .setLine(
            Client.getMouseX() + 6,
            Client.getMouseY() + 8,
            Client.getMouseX() + 11,
            Client.getMouseY() + 8,
            1
          ).draw();
      }
      if (this.hoveredHeight) {
        new Shape(0xffffffff)
          .setLine(
            Client.getMouseX() + 6,
            Client.getMouseY() + 8,
            Client.getMouseX() + 6,
            Client.getMouseY() + 13,
            1
          ).draw();
      }
    }

    this.scroll = easeOut(this.scroll, this.scrollTo, 10, 1);
  }

  this.clicked = function(button, state) {
    if (this.hovered) {
      if (button == -1) {
        this.scrollTo += 20;
      } else if (button == -2) {
        this.scrollTo -= 20;
      }
      if (this.scrollTo < 0) {this.scrollTo = 0;}
    }

    if (button != 0) {return false;}
    if (this.hoveredMinimize) {
      if (state == null) {
        this.minimized = !this.minimized;
      } else {
        this.minimized = !state
      }
      World.playSound("gui.button.press", 1, 1);
      return;
    }

    if (state != null && !state) {return false;}
    if (this.hoveredExit) {
      this.shouldExit = true;
      World.playSound("gui.button.press", 1, 1);
      return;
    }

    if (this.hoveredPin) {
      this.pinned = !this.pinned;
      World.playSound("gui.button.press", 1, 1);
      return;
    }

    this.selectedWidth = this.hoveredWidth;
    this.selectedHeight = this.hoveredHeight;

    if (!this.selectedWidth && !this.selectedHeight) {
      this.selected = this.hovered;
    }

    if (!this.selected) {this.showCursor = false;}

    return (this.selected || this.selectedWith || this.selectedHeight);
  }

  this.unselect = function() {
    this.selected = false;
    this.selectedWidth = false;
    this.selectedHeight = false;
    this.showCursor = false;
  }

  this.dragged = function(dx, dy, button) {
    if (!button == 0) {return;}
    earlyReturn = false;

    if (this.selectedWidth) {
      earlyReturn = true;
      this.width += dx;
      if (this.width < 150) {this.width = 150;}
    }

    if (this.selectedHeight) {
      earlyReturn = true;
      this.height += dy;
      if (this.height < 100) {this.height = 100;}
    }

    if (earlyReturn) {return;}

    if (this.selected) {
      this.x += dx;
      this.y += dy;
    }
  }

  this.updateCursor = function() {
    if (!this.selected) {return;}
    this.showCursor = !this.showCursor;
  }
}
