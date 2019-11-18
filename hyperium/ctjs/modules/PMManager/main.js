var PMM = new PMMController();
var Toolkit = Java.type("java.awt.Toolkit");
var DataFlavor = Java.type("java.awt.datatransfer.DataFlavor");

var PMMSettings = new SettingsObject("PMManager", [
  {
    name: "Messages",
    settings: [
      new Setting.ColorPicker("Recieved Color", [155, 158, 255]),
      new Setting.ColorPicker("Recieved Text Color", [0, 0, 0]),
      new Setting.ColorPicker("Sent Color", [255, 186, 105]),
      new Setting.ColorPicker("Sent Text Color", [0, 0, 0])
    ]
  }, {
    name: "Titlebar",
    settings: [
      new Setting.ColorPicker("Color", [27, 26, 33]),
      new Setting.ColorPicker("Exit Color", [229, 136, 118]), 
      new Setting.ColorPicker("Minimize Color", [0, 198, 203]),
      new Setting.ColorPicker("Notification Color", [255, 0, 0])
    ]
  }, {
    name: "Background",
    settings: [
      new Setting.ColorPicker("Color", [54, 53, 61]),
      new Setting.Slider("Selected Alpha", 170, 0, 255),
      new Setting.Slider("Unselected Alpha", 128, 0, 255)
    ]
  }
])

PMMSettings.setCommand("pmm");
Setting.register(PMMSettings);

function PMMController() {
  this.pinned = new Shape(0xffffffff).setDrawMode(5)
    .addVertex(6, 0).addVertex(7, 3).addVertex(8, 2)
    .addVertex(5, 1).addVertex(6, 0).addVertex(7, 3)
    .addVertex(5, 1).addVertex(6, 3).addVertex(7, 3)
    .addVertex(5, 2).addVertex(5, 1).addVertex(6, 3)
    .addVertex(5, 2).addVertex(5, 4).addVertex(6, 3)
    .addVertex(4, 3).addVertex(5, 2).addVertex(5, 4)
    .addVertex(4, 3).addVertex(5, 6).addVertex(5, 4)
    .addVertex(2, 3).addVertex(4, 3).addVertex(5, 6)
    
  this.pin = this.pinned.clone()
    .addVertex(2, 5).addVertex(0, 8).addVertex(3, 6)

  this.remove = function(array, element) {
    var index = array.indexOf(element);

    if (index !== -1) {
      array.splice(index, 1);
    }
  }

  this.checkKey = function(key) {
    return (
      (key >= 2 && key <= 13)   ||
      (key >= 16 && key <= 27)  ||
      (key >= 30 && key <= 41)  ||
      (key >= 43 && key <= 53)  ||
      (key >= 73 && key <= 83)  ||
      (key == 55) || (key == 181) || (key == 57)
    );
  }
}

register("chat", function(name, message) {
  var createNewWindow = true;
  PMMWindows.forEach(function(element) {
    if (element.name.getString() == ChatLib.addColor(name)) {
      element.addMessage(false, message);
      createNewWindow = false;
    }
  });

  if (createNewWindow) {
    var element = new PMMWindow(name)
    element.addMessage(false, message);
    PMMWindows.push(element);
  }
}).setCriteria("&dTo ${name}&r&7: ${message}");

register("chat", function(name, message) {
  var createNewWindow = true;
  PMMWindows.forEach(function(element) {
    if (element.name.getString() == ChatLib.addColor(name)) {
      element.addMessage(true, message);
      createNewWindow = false;
    }
  });

  if (createNewWindow) {
    var element = new PMMWindow(name)
    element.addMessage(true, message);
    PMMWindows.push(element);
  }
}).setCriteria("&dFrom ${name}&r&7: ${message}");
