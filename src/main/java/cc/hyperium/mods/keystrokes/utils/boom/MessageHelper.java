package cc.hyperium.mods.keystrokes.utils.boom;

import java.util.Random;

/**
 * Class to help with messages and stuff
 *
 * @author boomboompower
 */
@SuppressWarnings("ALL")
public abstract class MessageHelper {

    private static char[] numbers = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static char[] alphabet = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    private static Random rand = new Random();

    public static String getRandomString(int charCount) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < charCount; i++) {
            builder.append(getRandomCharacter());
        }
        return builder.toString().trim().isEmpty() ? getRandomCharacter() + "" + getRandomNumber(100) : builder.toString().trim();
    }

    public static int getRandomNumber() {
        return rand.nextInt();
    }

    public static int getRandomNumber(int highest) {
        return rand.nextInt(capInt(highest, 0, Integer.MAX_VALUE));
    }

    public static char getRandomCharacter() {
        return alphabet[rand.nextInt(alphabet.length)];
    }

    public static char getRandomCharacter(char... options) {
        return options[rand.nextInt(options.length)];
    }

    public static char getRandomDigit() {
        return numbers[rand.nextInt(numbers.length)];
    }

    // Translates decimal colors into an instance of the java.awt.Color class
    public static java.awt.Color numberToColor(int number) {
        return new java.awt.Color((number >> 16 & 255), (number >> 8 & 255), (number & 255), (number >> 24 & 255));
    }

    public static float capFloat(float valueIn, float minValue, float maxValue) {
        return valueIn < minValue ? minValue : valueIn > maxValue ? maxValue : valueIn;
    }

    public static double capDouble(double valueIn, double minValue, double maxValue) {
        return valueIn < minValue ? minValue : valueIn > maxValue ? maxValue : valueIn;
    }

    public static int capInt(int valueIn, int minValue, int maxValue) {
        return valueIn < minValue ? minValue : valueIn > maxValue ? maxValue : valueIn;
    }
}
