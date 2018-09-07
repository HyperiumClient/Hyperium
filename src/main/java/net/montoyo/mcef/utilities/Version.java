package net.montoyo.mcef.utilities;

/**
 * A utility class used for parsing decimal dot-separated versions from strings and comparing them.
 * Example: 5.4.9
 * 
 * @author montoyo
 *
 */
public class Version {
    
    private int[] numbers;
    
    /**
     * Constructs a version from a decimal dot-separated version string.
     * @param v A version string, such as 5.4.9
     */
    public Version(String v) {
        String[] ray = v.trim().split("\\.");
        numbers = new int[ray.length];
        
        for(int i = 0; i < ray.length; i++) {
            try {
                numbers[i] = Integer.parseInt(ray[i]);
            } catch(NumberFormatException e) {
                Log.error("Couldn't parse %s. Number %d will be zero.", v, i);
                e.printStackTrace();
                numbers[i] = 0;
            }
        }
        
        //Look for useless ending zeroes
        int end;
        for(end = numbers.length - 1; end >= 0; end--) {
            if(numbers[end] != 0)
                break;
        }
        
        end++;
        if(end != numbers.length) { //Is there any? Remove them!
            int[] na = new int[end];
            System.arraycopy(numbers, 0, na, 0, end);
            numbers = na;
        }
    }
    
    /**
     * Compares two versions. Return true if this instance is bigger than v.
     * If both describes the same version, false is returned.
     * 
     * @param v The version to compare with.
     * @return true if this instance is bigger than v.
     */
    public boolean isBiggerThan(Version v) {
        int len = Math.min(numbers.length, v.numbers.length);
        
        for(int i = 0; i < len; i++) {
            if(numbers[i] > v.numbers[i])
                return true;
        }
        
        return numbers.length > v.numbers.length;
    }
    
    /**
     * Compares two versions. Returns true if this instance is equal to o.
     * @param o The version to compare with.
     * @return true if both objects describes the same version.
     */
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Version))
            return false;
        
        Version v = (Version) o;
        if(v == this)
            return true;
        
        if(numbers.length != v.numbers.length)
            return false;
        
        for(int i = 0; i < numbers.length; i++) {
            if(numbers[i] != v.numbers[i])
                return false;
        }
        
        return true;
    }
    
    /**
     * Turns this version class into a decimal dot-separated version string.
     * @return a version string, such as 5.4.9
     */
    @Override
    public String toString() {
        String ret = "";
        for(int i = 0; i < numbers.length; i++) {
            if(i > 0)
                ret += '.';
            
            ret += numbers[i];
        }
        
        return ret;
    }

}
