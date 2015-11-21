package ztppro.view;

/**
 *
 * @author Damian Terlecki
 */
public interface View {

    public void addToDesktop(MyInternalFrame frame);

    public void repaint();
    
    public void repaint(int x, int y, int width, int height);
    
    boolean hasFocus();
    
}
