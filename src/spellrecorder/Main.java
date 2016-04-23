package spellrecorder;

public class Main {
    
    @SuppressWarnings("unused")
    private static AppWindow app;
    
    public static void main(String[] args) {
        System.out.println("Working dir:  " + System.getProperty("user.dir"));
        System.out.println("User Home: " + System.getProperty("user.home"));
        System.out.println("APPDATA: " + System.getenv("APPDATA"));

        app = new AppWindow();
    }
}
