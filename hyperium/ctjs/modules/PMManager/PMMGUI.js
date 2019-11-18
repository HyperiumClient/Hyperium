var PMMGui = new Gui();
var PMMWindows = [];
var BreakException = {};

register("command", function() {
  PMMGui.open();

  // debugging window
  /*PMMWindows.push(
    new PMMWindow("test")
    .addMessage(true, "hi")
    .addMessage(false, "This is a test")
    .addMessage(false, "This is a test of a really really long message to see the wrapping")
  );*/
}).setName("pm");

register("renderOverlay", function() {
  if (PMMGui.isOpen()) {
    new Rectangle(0x40000000, 0, 0, Renderer.screen.getWidth(), Renderer.screen.getHeight()).draw();
  }

  var DrawnPMMWindows = PMMWindows.slice(0).reverse();
  DrawnPMMWindows.forEach(function(element) {
    if (PMMGui.isOpen() || element.pinned) {
      element.draw();
      element.handleHovered();
    }
  });
});

PMMGui.registerClicked(function(x, y, button) {
  var clickedElement;
  try {
    PMMWindows.forEach(function(element) {
      if (element.clicked(button)) {
        clickedElement = element;
        throw BreakException;
      }
    });
  } catch (e) {
    if (e !== BreakException) throw e;
    PMMWindows = PMMWindows.filter(function(element) {
      if (element !== clickedElement) {
        element.unselect();
        return element;
      }
    });
    PMMWindows.unshift(clickedElement);
  }

  // close
  PMMWindows = PMMWindows.filter(function(element) {
    if (!element.shouldExit) {
      return element;
    }
  });
});

register("clicked", function(x, y, button, state) {
  if (!Client.isInChat()) return;

  try {
    PMMWindows.forEach(function(element) {
      if (element.pinned) {
        if (element.clicked(button, state)) {
          element.minimized = false;
          PMMGui.open();
          throw BreakException;
        }
      }
    });
  } catch (e) {
    if (e !== BreakException) throw e;
  }

  // close
  PMMWindows = PMMWindows.filter(function(element) {
    if (!element.shouldExit) {
      return element;
    }
  });
});

PMMGui.registerKeyTyped(function(char, key) {
  PMMWindows.forEach(function(element) {
    element.typed(char, key);
  });
});

register("dragged", function(dx, dy, x, y, button) {
  if (!PMMGui.isOpen()) {return;}

  PMMWindows.forEach(function(element) {
    element.dragged(dx, dy, button);
  });
});

register("step", function() {
  if (!PMMGui.isOpen()) {return;}

  PMMWindows.forEach(function(element) {
    element.updateCursor();
  });
}).setDelay(0.5);
