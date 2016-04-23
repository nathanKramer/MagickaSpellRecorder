package spellrecorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.*;

import spellrecorder.configuration.SpellRecorderConfiguration;
import spellrecorder.view.SpellRecorderPanel;
import util.ColorUtil;

public class AppWindow extends JFrame {
    
    private static final long serialVersionUID = -23716355859281985L;
    private static Integer WINDOW_WIDTH = 192;
    private static Integer WINDOW_HEIGHT = 450;
    private static Integer START_LOCATION_X = 100;
    private static Integer START_LOCATION_Y = 100;
    
    public SpellRecorderPanel displayPanel = new SpellRecorderPanel();
	
    public AppWindow() {
        super("Atomic Spell Recorder");        
        initialize();
    }
    
    private void initialize() {
        SpellRecorderConfiguration config = SpellRecorderConfiguration.getInstance();
        
        Color backgroundColor = new Color(0, 63, 57, 0);
        Boolean ingameMode = false;
        if (config != null) {
            String bgColor = config.getConfig().backgroundColor;
            if (config.getConfig().backgroundColor != null) {
                backgroundColor = ColorUtil.ColorFromString(bgColor);
            }
            ingameMode = config.getConfig().ingameMode;
            WINDOW_WIDTH = config.getConfig().width;
            WINDOW_HEIGHT = config.getConfig().height;
            START_LOCATION_X = config.getConfig().startPositionX;
            START_LOCATION_Y = config.getConfig().startPositionY;
        }
        
        if (ingameMode) {
            this.setTitle(getTitle() + " - ingame mode");
            backgroundColor = new Color(0, 0, 0, 0);
            setAlwaysOnTop(true);
            setUndecorated(true);
        } else {
            setUndecorated(false);
        }
        
        //getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
        setBackground(backgroundColor);
        setLayout(new BorderLayout());
        setResizable(false);
        setContentPane(displayPanel);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocation(START_LOCATION_X, START_LOCATION_Y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        //Read from an input stream
        InputStream is;
        try {
            is = Main.class.getResourceAsStream("/atomic_logo_32.png");
            Image image = ImageIO.read(is);
            setIconImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //initTrayIcon();
    }
    
    private void initTrayIcon() {
        TrayIcon trayIcon = null;
        
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();

            InputStream is = Main.class.getResourceAsStream("/atomic_logo_32.png");
            Image image;
            try {
                image = ImageIO.read(is);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            
            ActionListener listener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // execute default action of the application
                    // ...
                }
            };

            PopupMenu popup = new PopupMenu();
            
            MenuItem defaultItem = new MenuItem("Settings");
            defaultItem.addActionListener(listener);
            popup.add(defaultItem);

            trayIcon = new TrayIcon(image, "Atomic Spell Recorder", popup);
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(listener);
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println(e);
            }
            // ...
        } else {
            // disable tray option in your application or
            // perform other actions
        }
    }
}
