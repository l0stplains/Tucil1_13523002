package tucil_1_stima.util;

public final class ColorUtil {
    // Private constructor to prevent instantiation.
    private ColorUtil() {}

    /**
     * Returns an ANSI escape sequence that sets the foreground color for the given letter.
     * The color is generated based on the letter's position in the alphabet.
     *
     * @param letter the letter to generate a color for
     * @return the ANSI escape sequence for the color
     */
    public static String getAnsiColorForLetter(char letter) {
        int index = Character.toUpperCase(letter) - 'A';
        double hue = (index / 26.0) * 330.0; // not 360 because it will loop and color 'collision'
        int[] rgb = hslToRgb(hue, 1.0, 0.5); // full saturation, medium lightness
        return String.format("\u001B[38;2;%d;%d;%dm", rgb[0], rgb[1], rgb[2]);
    }

    /**
     * Converts HSL color values to an RGB array.
     *
     * @param h hue angle in degrees (0 - 360)
     * @param s saturation (0.0 - 1.0)
     * @param l lightness (0.0 - 1.0)
     * @return an array of integers representing RGB values (each 0 - 255)
     */
    private static int[] hslToRgb(double h, double s, double l) {
        double c = (1 - Math.abs(2 * l - 1)) * s;
        double x = c * (1 - Math.abs((h / 60.0) % 2 - 1));
        double m = l - c / 2;
        double r = 0, g = 0, b = 0;

        if (h < 60) {
            r = c;
            g = x;
            b = 0;
        } else if (h < 120) {
            r = x;
            g = c;
            b = 0;
        } else if (h < 180) {
            r = 0;
            g = c;
            b = x;
        } else if (h < 240) {
            r = 0;
            g = x;
            b = c;
        } else if (h < 300) {
            r = x;
            g = 0;
            b = c;
        } else {
            r = c;
            g = 0;
            b = x;
        }

        int red = (int) Math.round((r + m) * 255);
        int green = (int) Math.round((g + m) * 255);
        int blue = (int) Math.round((b + m) * 255);
        return new int[]{red, green, blue};
    }

    public static final String RESET = "\u001B[0m";

    /**
     * Test method: Prints the letters A to Z in dynamically generated ANSI colors.
     */
    public static void main(String[] args) {
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            String color = getAnsiColorForLetter(letter);
            System.out.print(color + letter + RESET + " ");
        }
        System.out.println();
    }
}
