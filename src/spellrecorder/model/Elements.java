package spellrecorder.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Elements {
    
    public static List<Element> elementList = new ArrayList<Element>();
    static {
        elementList.add(Element.LIGHTNING);
        elementList.add(Element.WATER);
        elementList.add(Element.LIFE);
        elementList.add(Element.ARCANE);
        elementList.add(Element.SHIELD);
        elementList.add(Element.EARTH);
        elementList.add(Element.COLD);
        elementList.add(Element.FIRE);
    }
    
    public static Map<Element, List<Element>> elementConflictMap = new HashMap<Element, List<Element>>();
    static {
        // This way has some redundancies, but it really doesn't need to be extendable...
        List<Element> waterConflicts = new ArrayList<Element>();
        waterConflicts.add(Element.LIGHTNING);
        
        List<Element> lightningConflicts = new ArrayList<Element>();
        lightningConflicts.add(Element.WATER);
        lightningConflicts.add(Element.EARTH);
        
        List<Element> lifeConflicts = new ArrayList<Element>();
        lifeConflicts.add(Element.ARCANE);
        
        List<Element> arcaneConflicts = new ArrayList<Element>();
        arcaneConflicts.add(Element.LIFE);
        
        List<Element> shieldConflicts = new ArrayList<Element>();
        shieldConflicts.add(Element.SHIELD);
        
        List<Element> earthConflicts = new ArrayList<Element>();
        earthConflicts.add(Element.LIGHTNING);
        
        List<Element> coldConflicts = new ArrayList<Element>();
        coldConflicts.add(Element.FIRE);
        
        List<Element> fireConflicts = new ArrayList<Element>();
        fireConflicts.add(Element.COLD);
        
        elementConflictMap.put(Element.WATER, waterConflicts);
        elementConflictMap.put(Element.LIGHTNING, lightningConflicts);
        elementConflictMap.put(Element.LIFE, lifeConflicts);
        elementConflictMap.put(Element.ARCANE, arcaneConflicts);
        elementConflictMap.put(Element.SHIELD, shieldConflicts);
        elementConflictMap.put(Element.EARTH, earthConflicts);
        elementConflictMap.put(Element.COLD, coldConflicts);
        elementConflictMap.put(Element.FIRE, fireConflicts);
    }
    
    public enum Element {
        WATER,
        FIRE,
        LIGHTNING,
        EARTH,
        COLD,
        SHIELD,
        ARCANE,
        LIFE,
        STEAM,
        ICE
    };
}
