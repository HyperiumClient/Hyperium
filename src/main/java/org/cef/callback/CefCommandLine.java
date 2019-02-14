// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package org.cef.callback;

import java.util.Map;
import java.util.Vector;

/**
 * Class used to create and/or parse command line arguments. Arguments with
 * '--', '-' and, on Windows, '/' prefixes are considered switches. Switches
 * will always precede any arguments without switch prefixes. Switches can
 * optionally have a value specified using the '=' delimiter (e.g.
 * "-switch=value"). An argument of "--" will terminate switch parsing with all
 * subsequent tokens, regardless of prefix, being interpreted as non-switch
 * arguments. Switch names are considered case-insensitive.
 */
public interface CefCommandLine {
    /**
     * Reset the command-line switches and arguments but leave the program
     * component unchanged.
     */
    void reset();
    /**
     * Get the program part of the command line string (the first item).
     */
    String getProgram();
    /**
     * Set the program part of the command line string (the first item).
     * @param program Name of the program.
     */
    void setProgram(String program);
    /**
     * Checks if the command line has switches.
     * @return true if the command line has switches.
     */
    boolean hasSwitches();
    /**
     * Checks if the command line has a specific switches.
     * @param name A switch name to test for.
     * @return true if the command line contains the given switch.
     */
    boolean hasSwitch(String name);
    /**
     * Returns the value associated with the given switch. If the switch has no
     * value or isn't present this method returns the empty string.
     * @param name the name of the switch.
     * @return the value of the switch.
     */
    String getSwitchValue(String name);
    /**
     * Returns the map of switch names and values. If a switch has no value an
     * empty string is returned.
     * @return Map of switches and each value.
     */
    Map<String, String> getSwitches();
    /**
     * Add a switch with an empty value to the end of the command line.
     * @param name name of the switch.
     */
    void appendSwitch(String name);
    /**
     * Add a switch with the specified value to the end of the command line.
     * @param name name of the switch.
     * @param value value for the switch.
     */
    void appendSwitchWithValue(String name, String value);
    /**
     * Tests if there are remaining command line arguments.
     * @return True if there are remaining command line arguments.
     */
    boolean hasArguments();
    /**
     * Get the remaining command line arguments.
     * @return Vector of command line arguments.
     */
    Vector<String> getArguments();
    /**
     * Add an argument to the end of the command line.
     * @param argument name of the argument.
     */
    void appendArgument(String argument);
}
