/*
 * * Copyright 2019-2020 github.com/ReflxctionDev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.hyperium.mods.rodcolor.rodcolor;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.client.TickEvent;
import cc.hyperium.mods.AbstractMod;
import net.minecraft.client.Minecraft;

public class RodColor extends AbstractMod {

    protected static boolean openGUI = false;

    @Override
    public AbstractMod init() {
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandRodColor());
        EventBus.INSTANCE.register(this);
        return this;
    }

    @InvokeEvent
    public void onTick(TickEvent event) {
        if (openGUI) {
            Minecraft.getMinecraft().displayGuiScreen(new RodColorGui());
            openGUI = false;
        }
    }

    @Override
    public Metadata getModMetadata() {
        return new Metadata(this, "RodColor", "1.3", "Reflxction");
    }
}
