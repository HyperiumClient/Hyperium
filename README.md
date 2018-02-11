[ ![Discord](https://canary.discordapp.com/api/guilds/411619823445999637/widget.png) ](https://discord.gg/8GakFcT)
# HCC (Hypixel Community Client)
## What is HCC? ##
The HCC is a Minecraft modification in development. It will combine many popular features in addition to suggestions and ideas from the community to provide a unique experience designed for Hypixel.

## Features ##

## Contributing ##

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

**Main Class:**
Your main class __needs__ to extend [Addon](https://github.com/HypixelCommunityClient/HCC/blob/master/src/main/java/com/hcc/addons/Addon.java)

```java
package me.cubxity;

import com.hcc.addon.Addon;

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
## Developers ##
[![](https://cdn.discordapp.com/avatars/376817315830038530/87dd80c68e0598ea39af4e0472b299b7.png)](https://github.com/Sk1er)
[![](https://cdn.discordapp.com/avatars/248159137370734601/8a8b49df90cda7ccd55f28c1f5293ad6.png)](https://github.com/CoalCoding)
[![](https://cdn.discordapp.com/avatars/247785387919933440/e8f6af129f0d6d4db93d8c7360aac15a.png)](https://github.com/KevinPriv)
[![](https://cdn.discordapp.com/avatars/290921387655430144/1495ae41593665e29f683d63d502c600.png)](https://github.com/VRCube)
