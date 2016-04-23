package util;

import java.awt.Color;

public class ColorUtil {
    public static Color ColorFromString(String s) {
        return new Color(
            Integer.valueOf(s.substring(1, 3), 16),
            Integer.valueOf(s.substring(3, 5), 16),
            Integer.valueOf(s.substring(5, 7), 16),
            255
        );
    }
}
