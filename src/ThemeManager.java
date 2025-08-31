public class ThemeManager {
    private static String pieceTheme = "alpha"; // default theme
    private static String block00 = "white";
    private static String block01 = "gray";

    public static String getPTheme() {
        return pieceTheme;
    }
    public static String get00Theme() {
        return block00;
    }
    public static String get01Theme() {
        return block01;
    }

    public static void setPTheme(String theme) {
        pieceTheme = theme;
        System.out.println("Theme set to: " + theme);
    }
    public static void setBTheme(String color , String color2) {
        block00 = color;
        block01 = color2;
        System.out.println("lighter color: " + color);
        System.out.println("darker color: " + color2);
    }
}
