package ztppro.view;

import java.awt.Component;
import java.awt.Graphics;
import java.util.Observer;

/**
 *
 * @author Damian Terlecki
 */
public interface View extends Observer {

    public void addToDesktop(MyInternalFrame frame);

    public void repaint();
    
    public void repaint(int x, int y, int width, int height);
    
    boolean hasFocus();
    
    public Component add(Component component);
    
    public Graphics paintLayer(Graphics g);
}
