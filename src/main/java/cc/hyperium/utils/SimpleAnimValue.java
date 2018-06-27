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
        this.startMs = System.currentTimeMillis();
    }


    /**
     * @return the current animation value
     */
    public float getValue() {
        return Math.max(start + (System.currentTimeMillis() - startMs) * (duration / (end - start)), end);
    }

    /**
     * @return if the animation is finished
     */
    public boolean isFinished() {
        return getValue() == end;
    }
}