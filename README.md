[ ![Discord](https://canary.discordapp.com/api/guilds/411619823445999637/widget.png) ](https://discord.gg/sk1er)
[ ![Travis-CI](https://travis-ci.org/HyperiumClient/Hyperium.svg?branch=master)](https://travis-ci.org/HyperiumClient/Hyperium)
[ ![Hyperium Website Badge](https://img.shields.io/badge/visit%20our-website-red.svg)](https://hyperium.cc)
[ ![Twitter Follower Count](https://img.shields.io/twitter/follow/HyperiumClient.svg?label=Follow&style=social)](https://twitter.com/HyperiumClient)
[ ![GitHub Forks](https://img.shields.io/github/forks/HyperiumClient/Hyperium.svg?style=social&label=Fork&maxAge=2592000)](https://github.com/HyperiumClient/Hyperium/network)
[ ![GitHub Stars](https://img.shields.io/github/stars/HyperiumClient/Hyperium.svg?style=social&label=Star&maxAge=2592000)](https://github.com/HyperiumClient/Hyperium/stargazers)
[ ![GitHub Watches](https://img.shields.io/github/watchers/HyperiumClient/Hyperium.svg?style=social&label=Watch&maxAge=2592000)](https://github.com/HyperiumClient/Hyperium/watchers)  

# Hyperium #  

## What is Hyperium? ##
Hyperium is an open-source and community-driven Minecraft 1.8.9 client that aims to provide many popular features and add performance improvements to make Minecraft an easier to use, and generally a better platform for all people.

## Features ##
- Discord Rich Presence (Custom)
- Old Animations (Custom)
- Togglesprint (Custom)
- [360 Degrees Perspective](https://www.youtube.com/watch?v=7FdMMpzNdUk) by Canelex
- [AutoGG](https://2pi.pw/mods/autogg) by 2pi
- [BlockOverlay](https://aycy.github.io/) by aycy
- [BossbarMod](https://www.youtube.com/watch?v=tigBu2OyZ4I) by Sk1er LLC
- [ChromaHud](https://sk1er.club/mods/tayber50k) by Sk1er LLC
- [Chunk Animator](https://www.curseforge.com/minecraft/mc-mods/chunk-animator) by Lumien
- Glint Colorizer (Video Unavailable) by Powns
- [Item Physics](https://www.curseforge.com/minecraft/mc-mods/itemphysic) by CreativeMD
- [Keystrokes](https://sk1er.club/mods/keystrokesmod) by Sk1er LLC
- [Levelhead](https://sk1er.club/mods/level_head) by Sk1er LLC
- [MemoryFix](https://prplz.io/memoryfix/) by prplz
- [MotionBlur](https://2pi.pw/mods/motionblur) by 2pi
- [MouseBindFix](https://sk1er.club/mods/mousebindfix) by Sk1er LLC
- [MouseDelayFix](https://prplz.io/mousedelayfix/) by prplz
- [NickHider](https://sk1er.club/mods/nick_hider) by Sk1er LLC
- [Sidebar Mod](https://www.youtube.com/watch?v=cn9VvT43yRs) by Canelex & Powns
- [TimeChanger](https://2pi.pw/mods/timechanger) by 2pi
- [ToggleChat](https://2pi.pw/mods/togglechat) by 2pi

### Beta Test the Client ###  
You can easily beta test the client for free, all you have to do is join [the Discord](https://discord.gg/sk1er) and do `$rank beta testing` in the [`#commands`](https://discordapp.com/channels/411619823445999637/411620555960352787) channel. You can then access betas in [`#beta-announcements`](https://discordapp.com/channels/411619823445999637/595634170336641045)

### FAQ ###  
Q: Please help, I can't build Hyperium!

A: If you don't know how to build a project, please wait for a new beta or release! 

Q: Where do i download Hyperium?

A: Download is available on our [website](https://hyperium.cc/downloads)!

Q: Is there a tutorial to create addons?

A: There is an example provided in the [README](https://github.com/HyperiumClient/Addon-Workspace/blob/master/README.md) of the addon workspace.

Q: I found a bug. Where do I report it?

A: Join the [Discord](https://discord.gg/sk1er) and explain your issue in as much detail as you can in the [`#bug-reports`](https://discordapp.com/channels/411619823445999637/429311217862180867) channel and wait for a staff member to assist you!

Q: My game keeps crashing!

A: Join the [Discord](https://discord.gg/sk1er) and ask a member of the staff team or a developer to review your issue in the [`#support`](https://discordapp.com/channels/411619823445999637/412310617442091008) channel. Please be patient if there are no developers online!

### Addons ###
Addons are Hyperium's system of mods, like Forge, except they're done using the [Addon Workspace / Addon API](https://github.com/HyperiumClient/Addon-Workspace/). The selection to currently available addons is low, so if you wish to make some, feel free to share it with other Hyperium members.

### License ###  
Hyperium is licensed under the GNU Lesser General Public License. You can view it [here](./LICENSE).

## Developer Resources ##
	
### Building the Project with IntelliJ ###

#### Step 1 - Cloning
- Click 'Clone or Download' and click the noteboard icon to copy to clipboard.

#### Step 2 - Importing
- Launch IntelliJ.
- Click Get from Version Control, paste the URL into the URL textfield, then hit Clone.
- Select 'Use Gradle wrapper configuration' when it asks for it.
- Open the new project and wait for all the processes to finish.

#### Step 3 - Decompiling
- Open the Gradle tab on the right hand side of IntelliJ, if it's not there, click 'View', 'Tool Windows', then 'Gradle'.
- In the Gradle tab, expand `Tasks`, `mcgradle`, and run `setup`.

#### Step 4 - Creating a profile
- Open the run configuration in the top right of IntelliJ, next to the green 'Run' button, click 'Edit Configurations'.
- Click the plus button in the top left, select 'Application', name it to 'Minecraft Client', set 'Main Class' to `tk.amplifiable.mcgradle.Start`
- Expand "Application", select 'Minecraft Client', change 'Use classpath of module' to 'Hyperium.generated.main'.
- Click the plus above the hammer saying 'Build', select 'Run Gradle Task', click the Folder icon, select 'Hyperium',
and type 'genProperties', select the one that comes up, click 'Ok', then press the up arrow beside the pencil icon.
- Make sure genProperties task is above the 'Build' task.
- In 'Program Arguments', add `--tweakClass cc.hyperium.launch.HyperiumTweaker`.
- Append `\run` to the end of the working directory.
- Press 'Apply' then 'Ok'.
- Once you've finished all of that, click the green 'Run' button beside the application.

#### Notes
##### Logging into Minecraft
- Include `--username example@examplesuite.com --password examplepassword` after the `HyperiumTweaker` argument.
- Press 'Apply' then 'Ok'.

##### Changing Minecraft source code
- If you change any of the source code to Minecraft (`net/minecraft` package), open the Gradle tab, expand `Tasks`, `mcgradle`, then run `generatePatches`.

##### Pulling Changes
- If you've made any changes to Minecraft, make sure you run `generatePatches` as mentioned in 'Changing Minecraft source code'
- When you pull anything, make sure you rerun the `setup` task as mentioned in 'Step 3 - Decompiling'.

##### Setting up hot reload with Intellij
- Download the DCEVM jar installer from https://github.com/dcevm/dcevm/releases/.
- Run the jar as administrator by opening up a terminal as admin, and change your working directory to the folder where you downloaded the file.
- Type `java -jar <jar name>` and replace `<jar name>` with the file name that you downloaded.
- When the installer opens up, choose your java installation directory and click, `Install DCEVM as altjvm`.
- After it finishes installing, open up Intellij and go to your run configuration.
- Add `-XXaltjvm=dcevm` to your VM options.
- Make sure the JRE matches the java version that you installed DCEVM on. then click Apply and Ok.
- Run the client in debug mode, and to reload any changes, press `CTRL + SHIFT + F9`.

### Addon Development ###
To make an addon, clone the [Addon Workspace](https://github.com/HyperiumClient/Addon-Workspace) and get coding!
**An example comes with the workspace.**

### Developers ###
[![Sk1er](https://avatars1.githubusercontent.com/u/18709703?s=128&v=4)](https://github.com/Sk1er)
[![CoalOres](https://avatars0.githubusercontent.com/u/12765568?s=128&v=4)](https://github.com/CoalCoding)
[![KevinPriv](https://avatars3.githubusercontent.com/u/31252471?s=128&v=4)](https://github.com/KevinPriv)
[![Cubxity](https://avatars1.githubusercontent.com/u/27609129?s=128&v=4)](https://github.com/Cubxity)
[![boomboompower](https://avatars1.githubusercontent.com/u/12974350?s=128&v=4)](https://github.com/boomboompower)
[![FalseHonesty](https://avatars2.githubusercontent.com/u/20765494?s=128&v=4)](https://github.com/FalseHonesty)
[![asbyth](https://avatars1.githubusercontent.com/u/36578995?s=128&v=4)](https://github.com/asbyth)

## Special Thanks ##
[![YourKit Java Profiler](https://www.yourkit.com/images/yklogo.png)](https://www.yourkit.com/java/profiler/)

YourKit supports open source projects with its full-featured Java Profiler.
YourKit, LLC is the creator of [YourKit Java Profiler](https://www.yourkit.com/java/profiler/)
and [YourKit](https://www.yourkit.com/.net/profiler/), innovative and intelligent tools for profiling Java and .NET applications.
