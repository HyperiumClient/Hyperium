/*
 * * Copyright 2018 github.com/ReflxctionDev
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

import cc.hyperium.commands.BaseCommand;
import com.google.common.collect.ImmutableList;

import java.util.Collections;
import java.util.List;

/**
 * Class which handles command input for "/rodcolor"
 */
public class CommandRodColor implements BaseCommand {

    /**
     * Gets the name of the command
     */
    @Override
    public String getName() {
        return "rodcolor";
    }

    /**
     * Gets the usage string for the command.
     */
    @Override
    public String getUsage() {
        return "/rodcolor";
    }

    @Override
    public List<String> getCommandAliases() {
        return ImmutableList.of();
    }

    /**
     * Callback when the command is invoked
     *
     * @param args The arguments that were passed
     */
    @Override
    public void onExecute(String[] args) {
        RodColor.openGUI = true;
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        return Collections.emptyList();
    }

}
