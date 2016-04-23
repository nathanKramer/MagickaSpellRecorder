package spellrecorder.controller;

import java.util.Stack;

import javax.swing.KeyStroke;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;

import spellrecorder.configuration.MagickaConfiguration;
import spellrecorder.configuration.SpellRecorderConfiguration;
import spellrecorder.model.Action;
import spellrecorder.model.Castable;
import spellrecorder.model.Spell;
import spellrecorder.model.Elements.Element;
import spellrecorder.model.magicks.Magick;
import spellrecorder.model.magicks.Magicks;
import spellrecorder.view.SpellRecorderPanel;

public class SpellRecorderController implements NativeKeyListener, NativeMouseListener {
    
    // Configuration
    private MagickaConfiguration magickaConfig;
    private SpellRecorderConfiguration spellRecorderConfig;
    
    public SpellRecorderPanel spellRecorder;
    private Stack<Action> actionHistory = new Stack<Action>();
    private Spell channelingSpell;
    private Spell currentSpell;
    private Magick currentMagick;
    
    private boolean spellRecorderActive = true;
    
    public SpellRecorderController() {
        init();
    }
    
    private void init() {
        currentSpell = new Spell();
        currentMagick = null;
        magickaConfig = new MagickaConfiguration();
        spellRecorderConfig = SpellRecorderConfiguration.getInstance();
        
        magickaConfig.readConfiguration();
        spellRecorderConfig.readConfiguration();
        
        registerHotkeys();
        
        Magicks.loadMagicks();
    }
    
    private void registerHotkeys() {
        try {
            /* Register jNativeHook */
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            /* Its error */
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        
        /* Construct the example object and initialze native hook. */
        GlobalScreen.getInstance().addNativeKeyListener(this);
        GlobalScreen.getInstance().addNativeMouseListener(this);
    }
    
    public Spell getCurrentSpell() {
        return currentSpell;
    }
    
    public Magick getCurrentMagick() {
        return currentMagick;
    }
    
    public Stack<Action> getHistory() {
        Stack<Action> historyCopy = (Stack<Action>) actionHistory.clone();
        return historyCopy;
    }
    
    private void handleElement(Element e) {
        currentSpell.push(e);
        currentMagick = null;
    }
    
    private void handleMagick(Magick m) {
        currentSpell = new Spell();
        if (currentMagick == null || !currentMagick.getName().equals(m.getName())) {
            currentMagick = m;
            
            if (m != null && m.getName().equals("Haste")) {
                forceCast();
            }
            
        } else if (currentMagick.getName().equals(m.getName())) {
            currentMagick = null;
            currentSpell = new Spell();
        }
    }
    
    private void forceCast() {
        
        if (currentMagick != null) {
            actionHistory.push(currentMagick);
            currentMagick = null;
        }
        
        castSpell(false);
    }
    
    private void selfCast() {
        castSpell(true);
    }
    
    private void finishCasting() {
        if (channelingSpell == null) {
            return;
        }
        
        channelingSpell.finishCasting();
        channelingSpell = null;
    }
    
    private void tryToFinishChannel(boolean selfCastPress) {
        if (channelingSpell == null) {
            return;
        }
        
        if (selfCastPress == channelingSpell.isSelfCast()) {
            finishCasting();
        }
    }
    
    private void castSpell(boolean isSelfCast) {
        if (currentSpell.getElements().size() > 0) {
            currentSpell.startCasting(isSelfCast);
            actionHistory.push(currentSpell);
            
            if (currentSpell.hasFinishedCasting()) {
                currentSpell = new Spell();
            } else {
                channelingSpell = currentSpell;
                currentSpell = new Spell();
            }
        }
    }

    public void clearHistory() {
        this.actionHistory = new Stack<Action>();
    }
    
    public void toggleInputs() {
        spellRecorderActive = !spellRecorderActive;
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {

        String keyText = NativeKeyEvent.getKeyText(e.getKeyCode());
        KeyStroke keyStroke = KeyStroke.getKeyStroke(keyText);
        
        int keyCode = -1;
        
        if (keyStroke != null) {
            keyCode = keyStroke.getKeyCode();
        }

        if (keyCode == spellRecorderConfig.getToggleInput().getKeyCode()) {
            toggleInputs();
        }
        
        if (!spellRecorderActive) {
            return;
        }
        
        if (keyCode == spellRecorderConfig.getClearSpellHistory().getKeyCode()) {
            clearHistory();
        }
        
        if (keyCode == spellRecorderConfig.getRereadConfig().getKeyCode()) {
            magickaConfig.readConfiguration();
        }
        
        // Elements
        if (keyText.equalsIgnoreCase(magickaConfig.keybindings.water)) {
            currentSpell.push(Element.WATER);
            currentMagick = null;
        } else if (keyText.equalsIgnoreCase(magickaConfig.keybindings.lightning)) {
            currentSpell.push(Element.LIGHTNING);
            currentMagick = null;
        } else if (keyText.equalsIgnoreCase(magickaConfig.keybindings.life)) {
            currentSpell.push(Element.LIFE);
            currentMagick = null;
        } else if (keyText.equalsIgnoreCase(magickaConfig.keybindings.arcane)) {
            currentSpell.push(Element.ARCANE);
            currentMagick = null;
        } else if (keyText.equalsIgnoreCase(magickaConfig.keybindings.shield)) {
            currentSpell.push(Element.SHIELD);
            currentMagick = null;
        } else if (keyText.equalsIgnoreCase(magickaConfig.keybindings.earth)) {
            currentSpell.push(Element.EARTH);
            currentMagick = null;
        } else if (keyText.equalsIgnoreCase(magickaConfig.keybindings.cold)) {
            currentSpell.push(Element.COLD);
            currentMagick = null;
        } else if (keyText.equalsIgnoreCase(magickaConfig.keybindings.fire)) {
            currentSpell.push(Element.FIRE);
            currentMagick = null;
        } else
        
            // Now spell casts
            if (keyText.equalsIgnoreCase(magickaConfig.keybindings.cast_spell)) {
            forceCast();
        } else if (keyText.equalsIgnoreCase(magickaConfig.keybindings.cast_self)) {
            selfCast();
        } else if (magickaConfig.keybindings.cast_self.equalsIgnoreCase("space") && keyText.equalsIgnoreCase("Leertaste")) {
            selfCast();
        }
        
        System.out.println(magickaConfig.keybindings.cast_self);
        
        // Magicks
        if (keyText.equals(magickaConfig.keybindings.magick1)) {
            handleMagick(Magicks.magicksMap.get(magickaConfig.inventory.magick1));
        } else if (keyText.equals(magickaConfig.keybindings.magick2)) {
            handleMagick(Magicks.magicksMap.get(magickaConfig.inventory.magick2));
        } else if (keyText.equals(magickaConfig.keybindings.magick3)) {
            handleMagick(Magicks.magicksMap.get(magickaConfig.inventory.magick3));
        } else if (keyText.equals(magickaConfig.keybindings.magick4)) {
            handleMagick(Magicks.magicksMap.get(magickaConfig.inventory.magick4));
        }
        
        spellRecorder.repaint();
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        String keyText = NativeKeyEvent.getKeyText(e.getKeyCode());
        
        // Spell casts are the only thing we really care about, for key releases.
        if (channelingSpell != null) {
            if (keyText.equalsIgnoreCase(magickaConfig.keybindings.cast_spell)) {
                tryToFinishChannel(false);
            } else if (keyText.equalsIgnoreCase(magickaConfig.keybindings.cast_self)) {
                tryToFinishChannel(true);
            }
        }
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        
        if (!spellRecorderActive) {
            return;
        }
        
        // Elements
        if ((e.getModifiers() & MagickaConfiguration.getMouseMask(magickaConfig.keybindings.water)) != 0) {
            handleElement(Element.WATER);
        } else if ((e.getModifiers() & MagickaConfiguration.getMouseMask(magickaConfig.keybindings.lightning)) != 0) {
            handleElement(Element.LIGHTNING);
        } else if ((e.getModifiers() & MagickaConfiguration.getMouseMask(magickaConfig.keybindings.life)) != 0) {
            handleElement(Element.LIFE);
        } else if ((e.getModifiers() & MagickaConfiguration.getMouseMask(magickaConfig.keybindings.arcane)) != 0) {
            handleElement(Element.ARCANE);
        } else if ((e.getModifiers() & MagickaConfiguration.getMouseMask(magickaConfig.keybindings.shield)) != 0) {
            handleElement(Element.SHIELD);
        } else if ((e.getModifiers() & MagickaConfiguration.getMouseMask(magickaConfig.keybindings.earth)) != 0) {
            handleElement(Element.EARTH);
        } else if ((e.getModifiers() & MagickaConfiguration.getMouseMask(magickaConfig.keybindings.cold)) != 0) {
            handleElement(Element.COLD);
        } else if ((e.getModifiers() & MagickaConfiguration.getMouseMask(magickaConfig.keybindings.fire)) != 0) {
            handleElement(Element.FIRE);
        } else
        
        // Now spell casts
        if ((e.getModifiers() & MagickaConfiguration.getMouseMask(magickaConfig.keybindings.cast_spell)) != 0) {
            forceCast();
        } else if ((e.getModifiers() & MagickaConfiguration.getMouseMask(magickaConfig.keybindings.cast_self)) != 0) {
            selfCast();
        }
        
        // Magicks
        if ((e.getModifiers() & MagickaConfiguration.getMouseMask(magickaConfig.keybindings.magick1)) != 0) {
            handleMagick(Magicks.magicksMap.get(magickaConfig.inventory.magick1));
        } else if ((e.getModifiers() & MagickaConfiguration.getMouseMask(magickaConfig.keybindings.magick2)) != 0) {
            handleMagick(Magicks.magicksMap.get(magickaConfig.inventory.magick2));
        } else if ((e.getModifiers() & MagickaConfiguration.getMouseMask(magickaConfig.keybindings.magick3)) != 0) {
            handleMagick(Magicks.magicksMap.get(magickaConfig.inventory.magick3));
        } else if ((e.getModifiers() & MagickaConfiguration.getMouseMask(magickaConfig.keybindings.magick4)) != 0) {
            handleMagick(Magicks.magicksMap.get(magickaConfig.inventory.magick4));
        }
        
        spellRecorder.repaint();
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        // Now spell casts
        if ((e.getButton() == MagickaConfiguration.getMouseButton(magickaConfig.keybindings.cast_spell))) {
            tryToFinishChannel(false);
        } else if ((e.getButton() == MagickaConfiguration.getMouseButton(magickaConfig.keybindings.cast_self))) {
            tryToFinishChannel(true);
        }
        
    }
}
