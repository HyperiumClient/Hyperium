/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.json;

import java.util.Map.Entry;

/**
 * Convert a web browser cookie list string to a JSONObject and back.
 *
 * @author JSON.org
 * @version 2015-12-09
 */
public class CookieList {

    /**
     * Convert a cookie list into a JSONObject. A cookie list is a sequence
     * of name/value pairs. The names are separated from the values by '='.
     * The pairs are separated by ';'. The names and the values
     * will be unescaped, possibly converting '+' and '%' sequences.
     * <p>
     * To add a cookie to a cookie list,
     * cookielistJSONObject.put(cookieJSONObject.getString("name"),
     * cookieJSONObject.getString("value"));
     *
     * @param string A cookie list string
     * @return A JSONObject
     * @throws JSONException
     */
    public static JSONObject toJSONObject(String string) throws JSONException {
        JSONObject jo = new JSONObject();
        JSONTokener x = new JSONTokener(string);
        while (x.more()) {
            String name = Cookie.unescape(x.nextTo('='));
            x.next('=');
            jo.put(name, Cookie.unescape(x.nextTo(';')));
            x.next();
        }
        return jo;
    }

    /**
     * Convert a JSONObject into a cookie list. A cookie list is a sequence
     * of name/value pairs. The names are separated from the values by '='.
     * The pairs are separated by ';'. The characters '%', '+', '=', and ';'
     * in the names and values are replaced by "%hh".
     *
     * @param jo A JSONObject
     * @return A cookie list string
     * @throws JSONException
     */
    public static String toString(JSONObject jo) throws JSONException {
        boolean b = false;
        StringBuilder sb = new StringBuilder();
        for (final Entry<String, ?> entry : jo.entrySet()) {
            final String key = entry.getKey();
            final Object value = entry.getValue();
            if (!JSONObject.NULL.equals(value)) {
                if (b) {
                    sb.append(';');
                }
                sb.append(Cookie.escape(key));
                sb.append("=");
                sb.append(Cookie.escape(value.toString()));
                b = true;
            }
        }
        return sb.toString();
    }
}
