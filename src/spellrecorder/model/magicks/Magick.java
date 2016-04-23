package spellrecorder.model.magicks;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import spellrecorder.Main;
import spellrecorder.configuration.SpellRecorderConfiguration;
import spellrecorder.model.Action;

public class Magick extends Action {
    
    private String name;
    private ImageIcon icon;
    
    public Magick(String name, String apiName) {
        
        SpellRecorderConfiguration config = SpellRecorderConfiguration.getInstance();
        boolean customIcons = !config.getConfig().classicIcons;
        String prefix = "/" + (customIcons ? "custom_" : "");
        
        this.name = name;
        System.out.println(prefix + apiName + ".png");
        InputStream is = Main.class.getResourceAsStream(prefix + apiName + ".png");
        try {
            icon = new ImageIcon(ImageIO.read(is));
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String getName() {
        return name;
    }
    
    public ImageIcon getIcon() {
        return icon;
    }
    
    public Image getImage() {
        return icon.getImage();
    }
    
    @Override
    public String toString() {
        return name;
    }
}
