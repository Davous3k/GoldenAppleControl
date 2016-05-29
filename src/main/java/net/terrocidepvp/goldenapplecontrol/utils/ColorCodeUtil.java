package net.terrocidepvp.goldenapplecontrol.utils;

public class ColorCodeUtil {

    public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
        final char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            // Check for color codes.
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i+1]) > -1) {
                // Replace character.
                b[i] = '§';
                // Make sure the color code is lower case.
                b[i+1] = Character.toLowerCase(b[i+1]);
            }
        }
        return new String(b);
    }

}
