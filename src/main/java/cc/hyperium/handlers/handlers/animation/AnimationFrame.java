package cc.hyperium.handlers.handlers.animation;

public class AnimationFrame {

    public String name;
    private long time;
    private BodyPart leftUpperArm = new BodyPart();
    private BodyPart leftLowerArm = new BodyPart();
    private BodyPart rightUpperArm = new BodyPart();
    private BodyPart rightLowerArm = new BodyPart();
    private BodyPart chest = new BodyPart();
    private BodyPart head = new BodyPart();
    private BodyPart leftUpperLeg = new BodyPart();
    private BodyPart leftLowerLeg = new BodyPart();
    private BodyPart rightUpperLeg = new BodyPart();
    private BodyPart rightLowerLeg = new BodyPart();
    private BodyPart leftLowerLeg_adj = new BodyPart();
    private BodyPart rightLowerLeg_adj = new BodyPart();
    private BodyPart rightLowerArm_adj = new BodyPart();
    private BodyPart leftLowerArm_adj = new BodyPart();
    private BodyPart butt = new BodyPart();

    public AnimationFrame(long time) {
        this.time = time;
        butt.visible = false;
    }

    public BodyPart getRightLowerArm_adj() {
        return rightLowerArm_adj;
    }

    public BodyPart getLeftLowerArm_adj() {
        return leftLowerArm_adj;
    }

    public String getName() {
        return name;
    }

    public BodyPart getLeftLowerLeg_adj() {
        return leftLowerLeg_adj;
    }

    public BodyPart getRightLowerLeg_adj() {
        return rightLowerLeg_adj;
    }

    public BodyPart getButt() {
        return butt;
    }

    @Override
    public String toString() {
        return "AnimationFrame{" +
            "time=" + time +
            ", leftUpperArm=" + leftUpperArm +
            ", leftLowerArm=" + leftLowerArm +
            ", rightUpperArm=" + rightUpperArm +
            ", rightLowerArm=" + rightLowerArm +
            ", chest=" + chest +
            ", head=" + head +
            ", leftUpperLeg=" + leftUpperLeg +
            ", leftLowerLeg=" + leftLowerLeg +
            ", rightUpperLeg=" + rightUpperLeg +
            ", rightLowerLeg=" + rightLowerLeg +
            ", name='" + name + '\'' +
            '}';
    }

    public long getTime() {
        return time;
    }

    public BodyPart getLeftUpperArm() {
        return leftUpperArm;
    }

    public BodyPart getLeftLowerArm() {
        return leftLowerArm;
    }

    public BodyPart getRightUpperArm() {
        return rightUpperArm;
    }

    public BodyPart getRightLowerArm() {
        return rightLowerArm;
    }

    public BodyPart getChest() {
        return chest;
    }

    public BodyPart getHead() {
        return head;
    }

    public BodyPart getLeftUpperLeg() {
        return leftUpperLeg;
    }

    public BodyPart getLeftLowerLeg() {
        return leftLowerLeg;
    }

    public BodyPart getRightUpperLeg() {
        return rightUpperLeg;
    }

    public BodyPart getRightLowerLeg() {
        return rightLowerLeg;
    }
}
