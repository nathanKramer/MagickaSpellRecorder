package spellrecorder.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jnativehook.mouse.NativeMouseEvent;

import spellrecorder.model.Elements.Element;

/**
 * Read from file, this is the user's Magicka configuration
 * We're only concerned with controls and which magicks are equipped, so those are all that this class models
 * @author NathanK
 *
 */
public class MagickaConfiguration {

    /**
     * Inventory Setup
     * We only read and store user's magicks
     */
    public class InventorySetup {
        public String magick1;
        public String magick2;
        public String magick3;
        public String magick4;
    }
    
    /**
     * Keybindings
     * We read all in game actions
     */
    public class Keybindings {
        
        public static final String MOUSE1 = "mouse_left";
        public static final String MOUSE2 = "mouse_right";
        public static final String MOUSE3 = "mouse_middle";
        public static final String MOUSE4 = "mouse_extra_1";
        public static final String MOUSE5 = "mouse_extra_2";
        
        // General controls
        public String cast_self;
        public String cast_spell;
        public String click;

        // Elements
        public String arcane;
        public String cold;
        public String earth;
        public String fire;
        public String life;
        public String lightning;
        public String shield;
        public String water;
        
        // Magicks
        public String magick1;
        public String magick2;
        public String magick3;
        public String magick4;
    }
    
    public InventorySetup inventory;
    public Keybindings keybindings;
    public static Map<String, Integer> mouseMasks = new HashMap<String, Integer>();
    public static Map<String, Integer> mouseButtons = new HashMap<String, Integer>();
    public static Map<Element, String> elementKeybindings = new HashMap<Element, String>();
    
    public MagickaConfiguration() {
        inventory = new InventorySetup();
        keybindings = new Keybindings();
        
        mouseMasks.put(Keybindings.MOUSE1, NativeMouseEvent.BUTTON1_MASK);
        mouseMasks.put(Keybindings.MOUSE2, NativeMouseEvent.BUTTON2_MASK);
        mouseMasks.put(Keybindings.MOUSE3, NativeMouseEvent.BUTTON3_MASK);
        mouseMasks.put(Keybindings.MOUSE4, NativeMouseEvent.BUTTON4_MASK);
        mouseMasks.put(Keybindings.MOUSE5, NativeMouseEvent.BUTTON5_MASK);
        
        mouseButtons.put(Keybindings.MOUSE1, NativeMouseEvent.BUTTON1);
        mouseButtons.put(Keybindings.MOUSE2, NativeMouseEvent.BUTTON2);
        mouseButtons.put(Keybindings.MOUSE3, NativeMouseEvent.BUTTON3);
        mouseButtons.put(Keybindings.MOUSE4, NativeMouseEvent.BUTTON4);
        mouseButtons.put(Keybindings.MOUSE5, NativeMouseEvent.BUTTON5);
    }

    public void readConfiguration() {
        String wizardWarsConfigDir = System.getProperty("user.home") + "\\AppData\\Roaming\\WizardWars";
        
        String file = "";
        try {
            file = new String(Files.readAllBytes(Paths.get(wizardWarsConfigDir + "\\user_settings.config")), "UTF-8");
        } catch (IOException e) {
            // Das lame
            e.printStackTrace();
        }
        
        // Regex dat shiz
        Map<String, String> inventoryMap = readLuaTable(file, "inventory_setup");
        Map<String, String> keyBindingsMap = readLuaTable(file, "keybindings");
        
        inventory.magick1 = inventoryMap.get("magick_1");
        inventory.magick2 = inventoryMap.get("magick_2");
        inventory.magick3 = inventoryMap.get("magick_3");
        inventory.magick4 = inventoryMap.get("magick_4");
        
        keybindings.arcane = keyBindingsMap.get("arcane");
        keybindings.cold = keyBindingsMap.get("cold");
        keybindings.earth = keyBindingsMap.get("earth");
        keybindings.fire = keyBindingsMap.get("fire");
        keybindings.life = keyBindingsMap.get("life");
        keybindings.lightning = keyBindingsMap.get("lightning");
        keybindings.water = keyBindingsMap.get("water");
        keybindings.shield = keyBindingsMap.get("shield");
        keybindings.magick1 = keyBindingsMap.get("magick1");
        keybindings.magick2 = keyBindingsMap.get("magick2");
        keybindings.magick3 = keyBindingsMap.get("magick3");
        keybindings.magick4 = keyBindingsMap.get("magick4");
        
        keybindings.cast_self = keyBindingsMap.get("cast_self");
        keybindings.cast_spell = keyBindingsMap.get("cast_spell");
        keybindings.click = keyBindingsMap.get("click");
    }
    
    private Map<String, String> readLuaTable(final String input, final String tableName) {
        Map<String, String> luaTableContent = new HashMap<String, String>();
        
        String tableStart = "^" + tableName + "\\s+=\\s+\\{\\r?\\n?";
        String tableContent = "((?:\\s+\\w+\\s+=\\s+\"(?:\\w+|\\s+)*\"\\r?\\n?)*)";
        String tableEnd = "}";
        
        // Pattern match dat shiz wizzle. groupNumber % 2 = 1 ? key : value
        Pattern readInventoryPattern = Pattern.compile(tableStart + tableContent + tableEnd, Pattern.MULTILINE);
        Matcher matcher = readInventoryPattern.matcher(input);

        String tableStrContent = "";
        while (matcher.find()) {
            tableStrContent = matcher.group(1);
        }
        
        tableStrContent.replaceAll("^\\s+", "");
        String[] tableVals = tableStrContent.split("(?:\\s+=\\s+)|(?:\\r?\\n\\s*)");
        
        
        for (int i = 0; i < tableVals.length; i ++) {
            if (i % 2 == 1) {
                String key = tableVals[i - 1].replaceAll("^\\s+", "");
                String value = tableVals[i].replace("\"", "");
                
                luaTableContent.put(key, value);
            }
        }
        
        return luaTableContent;
    }
    
    public static int getMouseMask(String mouseButtonName) {
        int retVal = 0;
        if (mouseMasks.containsKey(mouseButtonName)) {
            retVal = mouseMasks.get(mouseButtonName);
        }
        
        return retVal;
    }
    
    public static int getMouseButton(String mouseButtonName) {
        int retVal = 0;
        if (mouseButtons.containsKey(mouseButtonName)) {
            retVal = mouseButtons.get(mouseButtonName);
        }
        
        return retVal;
    }
}
