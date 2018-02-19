[ ![Discord](https://canary.discordapp.com/api/guilds/411619823445999637/widget.png) ](https://discord.gg/8GakFcT)
# HCC (Hypixel Community Client - Not affiliated with Hypixel INC, produced by independent developers)
## What is HCC? ##
The HCC is a Minecraft modification in development. It will combine many popular features in addition to suggestions and ideas from the community to provide a unique experience designed for Hypixel.

## Features ##

## Contributing ##

## Building the project with IntelliJ ##
Step #1 - Cloning the GitHub.

- Click "Clone or Download" and download as ZIP.
- Extract the file inside called "HCC_master" to wherever you want your workspace to be.

Step #2 - Importing the build.gradle in IntelliJ.

- Launch IntelliJ.
- Click New -> Project from existing sources, and select the build.gradle in the HCC_master folder.
- Select "use gradle wrapper configuration" when it asks for it.
- Open the new project and wait for all the processes to finish.

Step #3 - Changing your gradle version.

- Go into gradle/wrapper/gradle-wrapper.properties
- Change the version number in the distribution URL to "2.9" from "3.5".


Step #4 - Disabling shadowJar in the gradle.
- Go into the build.gradle file located in your workspace.
- Comment out anything with "shadow" or "shadowJar" in it.

Step #5 - Setting up decompiled MC.

- Open the gradle tab on the right hand side of IntelliJ, if it's not there, click "View", "Tool Windows"
 and then "gradle".
- In the gradle tab, expand Tasks, forgegradle and run setupDecompWorkspace, wait for it to finish.


Step #6 - Generating run files.
- In the gradle tab, forgegradle again, run "genIntelliJRuns".
- Open the run files in the top of IntelliJ next to the green "Run" button, and click "edit configurations".
- Expand "Application", select Minecraft Client, and change "Use classpath of module" to "HCC_master_main".
- Hit apply and close with "OK".

Step #7 - Resetting the gradle files and running MC.
- Go back into the build.gradle and uncomment out everything you commented out.
- Go back to the gradle-wrapper.properties file and change the version back to 3.5.
- Select "Minecraft Client" next to the run button and click the run button.
- The gradle should automatically rebuild, this may take a minute.
- Minecraft should launch.

## Building ##
Step #1: Cloning the repo
```
git clone https://github.com/HypixelCommunityClient/HCC
```
Step #2: Open the repository you cloned using IDE (We prefer [IntelliJ](https://jetbrains.com/idea))

Step #3: Import as gradle project, use the project's wrapper

Step #4: Run Tasks \> forgeGradle \> setupDecompWorkspace

Step #5: Run Tasks \> forgeGradle \> genIntellijRuns (for IntelliJ)

Step #6: Build project

## Addons ##
**Requirement:** Built HCC, JDK 8+, IDE

**Dependency:**
add HCC that you built as dependency for the project

```java
package me.cubxity;

@Addon(modid = "TestMod", version = "1.0 BETA")
public class TestMod {
    @InvokeEvent
    public void init(InitilizationEvent event) {
        System.out.println("Started!");
    }
}
```

**addon.json**
```json
{
  "name":"MaiCuteAddon",
  "version":"1.0",
  "main":"me.cubxity.TestMod"
}
```
**Examples [here](https://github.com/HypixelCommunityClient/AddonWorkspace)**
## Developers ##
[![](https://cdn.discordapp.com/avatars/376817315830038530/87dd80c68e0598ea39af4e0472b299b7.png)](https://github.com/Sk1er)
[![](https://cdn.discordapp.com/avatars/248159137370734601/8a8b49df90cda7ccd55f28c1f5293ad6.png)](https://github.com/CoalCoding)
[![](https://cdn.discordapp.com/avatars/247785387919933440/e8f6af129f0d6d4db93d8c7360aac15a.png)](https://github.com/KevinPriv)
[![](https://cdn.discordapp.com/avatars/290921387655430144/1495ae41593665e29f683d63d502c600.png)](https://github.com/VRCube)
