package cc.hyperium.handlers.handlers.animation.fortnite;

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


    public BodyPart calc(float percent, BodyPart next) {
        BodyPart bodyPart = new BodyPart();
        bodyPart.rotationPointX = interpolate(this.rotationPointX, next.rotationPointX, percent);
        bodyPart.rotationPointY = interpolate(this.rotationPointY, next.rotationPointY, percent);
        bodyPart.rotationPointZ = interpolate(this.rotationPointZ, next.rotationPointZ, percent);
        bodyPart.rotateAngleX = interpolate(this.rotateAngleX, next.rotateAngleX, percent);
        bodyPart.rotateAngleY = interpolate(this.rotateAngleY, next.rotateAngleY, percent);
        bodyPart.rotateAngleZ = interpolate(this.rotateAngleZ, next.rotateAngleZ, percent);
        bodyPart.offsetX = interpolate(this.offsetX, next.offsetX, percent);
        bodyPart.offsetY = interpolate(this.offsetY, next.offsetY, percent);
        bodyPart.offsetZ = interpolate(this.offsetZ, next.offsetZ, percent);

        return bodyPart;
    }

    private float interpolate(final float now, final float then, final float percent) {
        return (now + (then - now) * percent);
    }

}
