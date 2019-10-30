/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.utils;


//Skidded from Cubxity with permission
public class SimpleAnimValue {
    private Long startMs;
    private Long duration;

    private float start;
    private float end;

    /**
     * @param duration duration animation would last for (in millis)
     * @param start    start value
     * @param end      end value
     */
    public SimpleAnimValue(Long duration, float start, float end) {
        this.duration = duration;
        this.start = start;
        this.end = end;
        startMs = System.currentTimeMillis();
    }


    /**
     * @return the current animation value
     */
    public float getValue() {
        if (end - start == 0) return end;
        float v = start + ((float) (System.currentTimeMillis() - startMs)) * (((float) duration) / (end - start));
        return end > start ? Math.min(v, end) : Math.max(v, end);
    }

    /**
     * @return if the animation is finished
     */
    public boolean isFinished() {
        return getValue() == end;
    }
}
