package ztppro.view;

import java.awt.*;
import java.util.Observer;
import javax.swing.ImageIcon;

/**
 *
 * @author Damian Terlecki
 */
public interface View extends Observer {
    
    Image appIcon = new ImageIcon(View.class.getResource("/images/splash.png")).getImage();

    void repaint();
    
    void paintImmediately(int x, int y, int width, int height);
    
    void repaint(int x, int y, int width, int height);
    
    boolean hasFocus();
    
    Component add(Component component);
    
    Graphics paintLayer(Graphics g);
    
    void setCursor(Cursor cursor);
    
    Cursor getCursor();
 
    boolean requestFocusInWindow();

    void setPreferredSize(Dimension dimension);
    
}
