package spellrecorder.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import spellrecorder.Main;
import spellrecorder.configuration.SpellRecorderConfiguration;
import spellrecorder.controller.SpellRecorderController;
import spellrecorder.model.Action;
import spellrecorder.model.Castable;
import spellrecorder.model.Elements;
import spellrecorder.model.Spell;
import spellrecorder.model.Elements.Element;
import spellrecorder.model.magicks.Magick;
import util.ColorUtil;
import util.StringMetrics;

public class SpellRecorderPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = -7291470505654408282L;
    
    // User's customizable settings
    private static int SPELL_COUNT;
    private static int ELEMENT_SPACING;
    private static float ELEMENT_ICON_SCALE;
    private static float MAGICK_ICON_SCALE;
    private static String MAGICK_DISPLAY_TYPE;
    private static boolean BLANK_ELEMENT_ICONS;
    private static boolean AUTO_FIT_MAGICKS;
    private static int NUMBER_OF_ROWS_FOR_MAGICK;
    
    private static ImageIcon emptyElementImage;
    private static ImageIcon selfCast;
    private static Image border;
    
    private Timer timer = new Timer(80, this);
    private Map<Element, ImageIcon> imageMap = new HashMap<Element, ImageIcon>();
    public SpellRecorderController controller = new SpellRecorderController(); 
    
    private static SpellRecorderConfiguration config;
    
    public SpellRecorderPanel() {
        initializeMap();
        controller.spellRecorder = this;
        setOpaque(false);
        
        readUserConfig();
    }
    
    private void readUserConfig() {
        config = SpellRecorderConfiguration.getInstance();
        SPELL_COUNT = config.getConfig().numberOfSpells;
        ELEMENT_SPACING = (int) config.getConfig().elementSpacing;
        ELEMENT_ICON_SCALE = config.getConfig().elementIconScale;
        MAGICK_ICON_SCALE = config.getConfig().magickIconScale;
        MAGICK_DISPLAY_TYPE = config.getConfig().magickDisplayType;
        BLANK_ELEMENT_ICONS = config.getConfig().blankElementIcons;
        AUTO_FIT_MAGICKS = config.getConfig().autoFitMagicks;
        NUMBER_OF_ROWS_FOR_MAGICK = config.getConfig().numberOfRowsForMagick;
    }
    
    private void initializeMap() {
        //Read from an input stream
        InputStream is;
        try {
            SpellRecorderConfiguration config = SpellRecorderConfiguration.getInstance();
            boolean customIcons = !config.getConfig().classicIcons;
            String prefix = "/" + (customIcons ? "custom_" : "");
            
            for (Element e : Elements.elementList) {
                ImageIcon icon;
                String elementName = e.toString().toLowerCase();
                System.out.println(elementName);
                
                is = Main.class.getResourceAsStream(prefix + elementName + ".png");
                icon = new ImageIcon(ImageIO.read(is));
                is.close();
                
                imageMap.put(e, icon);
            }

            is = Main.class.getResourceAsStream(prefix + "blank.png");
            emptyElementImage = new ImageIcon(ImageIO.read(is));
            is.close();
            
            is = Main.class.getResourceAsStream(prefix + "self_cast.png");
            selfCast = new ImageIcon(ImageIO.read(is));
            is.close();
            
            is = Main.class.getResourceAsStream(prefix + "border.png");
            border = ImageIO.read(is);
            is.close();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int elementWidth = (int) (ELEMENT_ICON_SCALE * emptyElementImage.getIconWidth());
        int spellRowWidth = ((Spell.spellSize - 1) * ELEMENT_SPACING) + elementWidth;
        
        int startX = (int) ((this.getBounds().getWidth() * 0.5) - (spellRowWidth * 0.5));
        int startY = (int) this.getBounds().getHeight() - 128;
                
        // History underline
        g.drawImage(border, startX, startY + 2, spellRowWidth, 2, null);
        
        if (controller.getCurrentMagick() != null && !MAGICK_DISPLAY_TYPE.equals("off")) {
            int yChange = 0;
            if (MAGICK_DISPLAY_TYPE.equals("text")) {
                yChange = (int) (ELEMENT_SPACING * 0.5);
            } else {
                yChange = 16;
            }
            paintMagick(g, controller.getCurrentMagick(), startX, startY + yChange);
        } else {
            paintSpell(g, controller.getCurrentSpell(), startX, startY + (int) (ELEMENT_SPACING * 0.25));
        }

        // Paint historical actions
        Stack<Action> actionHistory = controller.getHistory();
        int actionCount = 0;
        while (!actionHistory.isEmpty() && actionCount < SPELL_COUNT) {
            // if we need to alter the vertical drawing position of this action
            int yChange = 0;
            
            Action oldAction = actionHistory.pop();
            
            int yShift = ELEMENT_SPACING;
            if (oldAction instanceof Magick) {
                if (config.getConfig().magickDisplayType.equals("off")) {
                    continue;
                }
                
                if (config.getConfig().magickDisplayType.equals("image")) {
                    Magick m = (Magick) oldAction;
                    if (AUTO_FIT_MAGICKS) {
                        yShift = (int) ((m.getIcon().getIconHeight() * MAGICK_ICON_SCALE) * 1.25);
                        yChange = (int) ((m.getIcon().getIconHeight() * MAGICK_ICON_SCALE) / 6.0);
                        actionCount += 2;
                    } else {
                        yShift = ELEMENT_SPACING * NUMBER_OF_ROWS_FOR_MAGICK;
                        actionCount += NUMBER_OF_ROWS_FOR_MAGICK;
                    }
                } else if (config.getConfig().magickDisplayType.equals("text")) {
                    yShift = ELEMENT_SPACING;
                    yChange = (int) ((0.6 * ELEMENT_SPACING));
                    actionCount += 1;
                }
                
            } else {
                actionCount += 1;
            }
            
            if (actionCount > SPELL_COUNT) {
                break;
            }
            
            startY = startY - yShift;
            int yPosition = startY + yChange;
            
            paintAction(g, oldAction, startX, yPosition);
        }
    }
    
    private void paintAction(Graphics g, Action action, int x, int y) {
        if (action instanceof Magick) {
            paintMagick(g, (Magick) action, x, y);
        } else if (action instanceof Spell) {
            paintSpell(g, (Spell) action, x, y);
        }
    }
    
    private void paintMagick(Graphics g, Magick magick, int x, int y) {
        if (magick != null) {
            
            if (config.getConfig().magickDisplayType.equals("text")) {
                if (g instanceof Graphics2D) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    int style = Font.PLAIN;
                    boolean bold = config.getConfig().magickText_bold;
                    boolean italics = config.getConfig().magickText_italics;                 
                    
                    if (bold || italics) {
                        int boldFlag = bold ? Font.BOLD : 0;
                        int italicsFlag = italics ? Font.ITALIC : 0;
                        
                        style = boldFlag | italicsFlag;
                    }
                    
                    g2.setFont(new Font(config.getConfig().magickText_font, style, config.getConfig().magickText_size));
                    
                    StringMetrics stringMetrics = new StringMetrics(g2);

                    g2.setColor(
                        ColorUtil.ColorFromString(config.getConfig().magickText_color)
                    );

                    int xPosition = (int) ((this.getBounds().getWidth() * 0.5) - (stringMetrics.getWidth(magick.getName()) * 0.5));
                    g2.drawString(magick.getName(), xPosition, y);
                }
            } else if (MAGICK_DISPLAY_TYPE.equals("image")) {
                int magickSizeX = (int) (magick.getIcon().getIconWidth() * MAGICK_ICON_SCALE);
                int magickSizeY = (int) (magick.getIcon().getIconHeight() * MAGICK_ICON_SCALE);
                int xPosition = (int) ((this.getBounds().getWidth() * 0.5) - (magickSizeX * 0.5));
                int yposition = y;
                g.drawImage(magick.getImage(), xPosition, yposition, magickSizeX, magickSizeY, null);
            }
        }
    }
    
    private void paintSpell(Graphics g, Spell spell, int x, int y) {
        int totalExpected = Spell.spellSize; // Pull this out later
        int elementCount = 0;
        
        boolean drawingBlanks = !spell.hasStartedCasting() || BLANK_ELEMENT_ICONS;
        int xPush = drawingBlanks || config.getConfig().spellAlignment.equals("left") ? 0 : ((totalExpected - spell.getElements().size()) * ELEMENT_SPACING);
        
        x += xPush;
        
        if (spell.isSelfCast()) {
            g.drawImage(selfCast.getImage(), x - (int) (ELEMENT_SPACING / 2.5), y, (int) (selfCast.getIconWidth() * ELEMENT_ICON_SCALE), (int) (selfCast.getIconHeight() * ELEMENT_ICON_SCALE), null);
        }
        
        for (Element e : spell.getElements()) {
            ImageIcon imageIcon = imageMap.get(e);
            int xSize = (int) (ELEMENT_ICON_SCALE * imageIcon.getIconWidth());
            int ySize = (int) (ELEMENT_ICON_SCALE * imageIcon.getIconHeight());
            
            g.drawImage(imageIcon.getImage(), x + elementCount * ELEMENT_SPACING, y, xSize, ySize, null);
            elementCount ++;
        }
        
        if (drawingBlanks) {
            int blankElements = totalExpected - elementCount;
            for (int i = 0; i < blankElements; i ++) {
                int xSize = (int) (ELEMENT_ICON_SCALE * emptyElementImage.getIconWidth());
                int ySize = (int) (ELEMENT_ICON_SCALE * emptyElementImage.getIconHeight());
                
                g.drawImage(emptyElementImage.getImage(), x + elementCount * ELEMENT_SPACING, y, xSize, ySize, null);
                elementCount ++;
            }
        }
        
//        if (g instanceof Graphics2D) {
//            Graphics2D g2 = (Graphics2D) g;
//            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            g2.setFont(new Font("Monospaced", Font.BOLD, 15));
//            
//            float spellDuration = spell.getChannelDuration();
//            if (spellDuration != 0) {
//                System.out.println(spellDuration);
//                g2.setColor(
//                    new Color(
//                        255, 
//                        (int) Math.min(255, 255 * (1 - (Math.min(0.5F, spellDuration-2.0F) / 0.5F))), 
//                        (int) Math.min(255, 255 * (1 - (Math.min(0.5F, spellDuration-2.0F) / 0.5F)))
//                    )
//                );
//                
//                g2.drawString(String.format("%.2fs", spellDuration), 200, y + 28);
//            }
//        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == timer) {
            repaint();
        }
    }
}
