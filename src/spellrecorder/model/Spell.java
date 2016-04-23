package spellrecorder.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.Timer;

import spellrecorder.model.Elements.Element;

public class Spell extends Action implements Castable {
    
    public static Integer spellSize = 5;
    private List<Element> elements;
    private boolean selfCast;
    
    private float channelDuration = -1F;
    private long startCastTime = 0L;
    private long stopCastTime = 0L;
    
    private boolean canChannel;
    
    public Spell() {
        selfCast = false;
        elements = new ArrayList<Element>();
    }
    
    @Override
    public void startCasting(boolean selfCast) {
        this.selfCast = selfCast;
        this.startCastTime = System.currentTimeMillis();
        
        this.classifySpellForm();
        System.out.println("Starting to cast");
        
        if (!canChannel) {
            finishCasting();
            channelDuration = 0L;
        }
    }
    
    @Override
    public void finishCasting() {
        this.stopCastTime = System.currentTimeMillis();
        this.channelDuration = getChannelDuration();
    }
    
    public float getChannelDuration() {
        float duration = 0;
        
        if (channelDuration != -1F) {
            duration = channelDuration;
        } else if (hasStartedCasting()) {
            long timePassed = System.currentTimeMillis() - startCastTime;
            duration = timePassed / 1000.0F;
        }  
        
        return duration;
    }
    
    public boolean hasStartedCasting() {
        return startCastTime != 0L;
    }
    
    public boolean hasFinishedCasting() {
        return stopCastTime != 0L;
    }
    
    private void classifySpellForm() {
        canChannel = true;
        
        if (selfCast) {
            Boolean allLifeElements = true;
            for (Element e : elements) {
                if (e != Element.LIFE) {
                    allLifeElements = false;
                    break;
                }
            }
            
            if (!allLifeElements) {
                canChannel = false;
            }
        }
        
        if (canChannel) {
            if (elements.contains(Element.SHIELD) || elements.contains(Element.LIGHTNING)) {
                canChannel = false;
            }
        }
    }
    
    public void push(Element e) {
        ListIterator<Element> iter = elements.listIterator(elements.size());
        boolean conflicted = false;
        
        if (e == Element.LIGHTNING) {
            while (iter.hasPrevious()) {
                Element existingElement = iter.previous();
                
                if (existingElement == Element.EARTH) {
                    iter.remove();
                    conflicted = true;
                    break;
                }
            }
        }
        
        iter = elements.listIterator(elements.size());
        if (!conflicted) {
            while (iter.hasPrevious()) {
                Element existingElement = iter.previous();
                if (Elements.elementConflictMap.get(e).contains(existingElement)) {
                    iter.remove();
                    conflicted = true;
                    break;
                }
            }
        }
        
        boolean safeToAdd = !conflicted && elements.size() < spellSize;
        
        if (safeToAdd) {
            elements.add(e);
        }
    }
    
    public boolean isSelfCast() {
        return selfCast;
    }
    
    public List<Element> getElements() {
        return new ArrayList<Element>(elements);
    }
    
    @Override
    public String toString() {
        String out = "";
        out += selfCast ? SpellNotation.SELF_CAST : "";
        for (Element e : elements) {
            out += SpellNotation.elementToString.get(e);
        }
        return out;
    }
}
