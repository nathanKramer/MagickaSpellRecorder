package spellrecorder.configuration;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.prefs.Preferences;

import javax.swing.KeyStroke;

import org.apache.commons.io.IOUtils;

import spellrecorder.Main;

import com.google.gson.Gson;

public class SpellRecorderConfiguration {
    private Preferences userPreferences = Preferences.userNodeForPackage(spellrecorder.Main.class);
    private Config config;
    private KeyStroke toggleInput;
    private KeyStroke clearSpellHistory;
    private KeyStroke rereadConfig;
    
    private static SpellRecorderConfiguration instance;
    
    private SpellRecorderConfiguration() {
        int getUser_toggleInput = userPreferences.getInt("toggleInput", KeyEvent.VK_F5);
        int getUser_toggleInput_modifiers = userPreferences.getInt("toggleInput_modifiers", 0);
        
        int getUser_clearSpellHistory = userPreferences.getInt("clearSpellHistory", KeyEvent.VK_F6);
        int getUser_clearSpellHistory_modifiers = userPreferences.getInt("clearSpellHistory_modifiers", 0);
        
        int getUser_rereadConfig = userPreferences.getInt("rereadConfig", KeyEvent.VK_F7);
        int getUser_rereadConfig_modifiers = userPreferences.getInt("rereadConfig_modifiers", 0);
        
        toggleInput = KeyStroke.getKeyStroke(getUser_toggleInput, getUser_toggleInput_modifiers);
        clearSpellHistory = KeyStroke.getKeyStroke(getUser_clearSpellHistory, getUser_clearSpellHistory_modifiers);
        rereadConfig = KeyStroke.getKeyStroke(getUser_rereadConfig, getUser_rereadConfig_modifiers);
    }
    
    public static SpellRecorderConfiguration getInstance() {
        if (instance == null) {
            instance = new SpellRecorderConfiguration();
            instance.readConfiguration();
        }
        
        return instance;
    }
    
    public class ColorWrapper {
        public int r;
        public int g;
        public int b;
    }
    
    public static class Config {
        public String magickDisplayType = "image"; // image, text, off
        public int numberOfSpells;
        public boolean classicIcons;
        public boolean blankElementIcons;
        public String spellAlignment;
        public float elementIconScale;
        public float magickIconScale;
        public int elementSpacing;
        public String backgroundColor;
        public boolean ingameMode;
        
        public int width;
        public int height;
        public int startPositionX;
        public int startPositionY;
        
        // Magick text settings
        public int magickText_size;
        public String magickText_color;
        public String magickText_font;
        public boolean magickText_bold;
        public boolean magickText_italics;
        
        public boolean autoFitMagicks;
        public int numberOfRowsForMagick;
        
        public Config() {
        }
        
        public static Config getDefaults() {
            return null;
        }
    }
    
    public void readConfiguration() {
        InputStream inputStream = Main.class.getResourceAsStream("/user_config.json");
        String json = null;
        try {
            json = IOUtils.toString(inputStream, "UTF-8");
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        Gson gson = new Gson();
        if (json != null) {        
            config = gson.fromJson(json, Config.class);
            System.out.println(config.backgroundColor);
        } else {
            config = Config.getDefaults();
        }
    }
    
    public Config getConfig() {
        return config; 
    }
    
    public KeyStroke getToggleInput() {
        return toggleInput;
    }
    
    public KeyStroke getClearSpellHistory() {
        return clearSpellHistory;
    }
    
    public KeyStroke getRereadConfig() {
        return rereadConfig;
    }
}
