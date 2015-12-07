package ztppro.view;

import java.awt.*;
import java.util.Observer;

/**
 *
 * @author Damian Terlecki
 */
public interface View extends Observer {

    public void repaint();
    
    public void paintImmediately(int x, int y, int width, int height);
    
    public void repaint(int x, int y, int width, int height);
    
    boolean hasFocus();
    
    public Component add(Component component);
    
    public Graphics paintLayer(Graphics g);
    
    public void setCursor(Cursor cursor);
    
    public Cursor getCursor();
 
    public boolean requestFocusInWindow();

    public void setPreferredSize(Dimension dimension);
    
}
