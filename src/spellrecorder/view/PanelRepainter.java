package spellrecorder.view;

import java.util.TimerTask;

import javax.swing.JPanel;

public class PanelRepainter extends TimerTask {

    private JPanel panel;
    
    public PanelRepainter(JPanel panel) {
        this.panel = panel;
    }
    
    @Override
    public void run() {
        panel.repaint();
    }

}
