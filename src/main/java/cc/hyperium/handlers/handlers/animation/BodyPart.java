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

package cc.hyperium.handlers.handlers.animation;

public class BodyPart {
    public float rotationPointX;
    public float rotationPointY;
    public float rotationPointZ;
    public float rotateAngleX;
    public float rotateAngleY;
    public float rotateAngleZ;
    public float offsetX;
    public float offsetY;
    public float offsetZ;
    public boolean visible = true;

    public BodyPart calc(float percent, BodyPart next) {
        BodyPart bodyPart = new BodyPart();
        bodyPart.rotationPointX = interpolate(rotationPointX, next.rotationPointX, percent);
        bodyPart.rotationPointY = interpolate(rotationPointY, next.rotationPointY, percent);
        bodyPart.rotationPointZ = interpolate(rotationPointZ, next.rotationPointZ, percent);
        bodyPart.rotateAngleX = interpolate(rotateAngleX, next.rotateAngleX, percent);
        bodyPart.rotateAngleY = interpolate(rotateAngleY, next.rotateAngleY, percent);
        bodyPart.rotateAngleZ = interpolate(rotateAngleZ, next.rotateAngleZ, percent);
        bodyPart.offsetX = interpolate(offsetX, next.offsetX, percent);
        bodyPart.offsetY = interpolate(offsetY, next.offsetY, percent);
        bodyPart.offsetZ = interpolate(offsetZ, next.offsetZ, percent);
        bodyPart.visible = visible;
        return bodyPart;
    }

    @Override
    public String toString() {
        return "BodyPart{" +
            "rotationPointX=" + rotationPointX +
            ", rotationPointY=" + rotationPointY +
            ", rotationPointZ=" + rotationPointZ +
            ", rotateAngleX=" + rotateAngleX +
            ", rotateAngleY=" + rotateAngleY +
            ", rotateAngleZ=" + rotateAngleZ +
            ", offsetX=" + offsetX +
            ", offsetY=" + offsetY +
            ", offsetZ=" + offsetZ +
            '}';
    }

    private float interpolate(final float now, final float then, final float percent) {
        return (now + (then - now) * percent);
    }
}
