package net.montoyo.mcef.utilities;

/**
 * This interface is used by classes that wants to keep track of the progress of some tasks.
 * @author montoyo
 *
 */
public interface IProgressListener {
    
    /**
     * Call this when the current task progressed.
     * @param d The current task progress in percent.
     */
    public void onProgressed(double d);
    
    /**
     * Call this if the current task changed.
     * @param name The name of the new task.
     */
    public void onTaskChanged(String name);

    /**
     * Call this when everything is finished.
     */
    public void onProgressEnd();

}
