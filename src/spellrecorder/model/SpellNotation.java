package spellrecorder.model;

import java.util.HashMap;
import java.util.Map;

import spellrecorder.model.Elements.Element;

public class SpellNotation {
    // Denotes a self cast
    public static final String SELF_CAST = "!";
    
    // Denotes a charged cast, ie, DDD charged one bar would be cDDD
    public static final String CHARGED_CAST = "c";
    
    // Denotes an uncharged cast, ie, an FD cast to remove wet status effect is nFD
    public static final String UNCHARGED_CAST = "n";
    
    public static Map<Element, String> elementToString = new HashMap<Element, String>();
    public static Map<String, Element> stringToElement = new HashMap<String, Element>();
    
    static {
        elementToString.put(Element.WATER, "Q");
        elementToString.put(Element.LIGHTNING, "A");
        elementToString.put(Element.LIFE, "W");
        elementToString.put(Element.ARCANE, "S");
        elementToString.put(Element.SHIELD, "E");
        elementToString.put(Element.EARTH, "D");
        elementToString.put(Element.COLD, "R");
        elementToString.put(Element.FIRE, "F");
        
        stringToElement = new HashMap<String, Element>();
        for (Element e : elementToString.keySet()) {
            stringToElement.put(elementToString.get(e), e);
        }
    }
}
