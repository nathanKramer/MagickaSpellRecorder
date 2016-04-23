package spellrecorder;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class AppTray {
    public static void initTray() {
//        TrayIcon trayIcon = null;
//        
//        if (SystemTray.isSupported()) {
//            SystemTray tray = SystemTray.getSystemTray();
//
//            InputStream is = Main.class.getResourceAsStream("/atomic_logo_32.png");
//            Image image;
//            try {
//                image = ImageIO.read(is);
//            } catch (IOException e) {
//                e.printStackTrace();
//                return;
//            }
//            
//            ActionListener listener = new ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//                    // execute default action of the application
//                    // ...
//                }
//            };
//
//            PopupMenu popup = new PopupMenu();
//            
//            MenuItem defaultItem = new MenuItem("Settings");
//            defaultItem.addActionListener(listener);
//            popup.add(defaultItem);
//
//            trayIcon = new TrayIcon(image, "Atomic Spell Recorder", popup);
//            trayIcon.setImageAutoSize(true);
//            trayIcon.addActionListener(listener);
//            try {
//                tray.add(trayIcon);
//            } catch (AWTException e) {
//                System.err.println(e);
//            }
//            // ...
//        } else {
//            // disable tray option in your application or
//            // perform other actions
//        }
    }
}
