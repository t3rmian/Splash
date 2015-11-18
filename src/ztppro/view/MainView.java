package ztppro.view;

import java.awt.BorderLayout;
import java.awt.Canvas;
//import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

/**
 *
 * @author Damian Terlecki
 */
public class MainView {

    private JFrame frame;
    private JToggleButton bGrid;
    private JScrollPane scroll;

    public File file;
    public Canvas canvas;
//    public Palette pal;
    private JMenuBar menuBar;
    private JToolBar toolBar;
//    private ToolPanel toolPanel;

    public MainView() {

        BufferedImage image = createWhiteBufferedImage(32, 32);
//        pal = new Palette();
        canvas = new Canvas();
        scroll = new JScrollPane(canvas);
//        toolPanel = new ToolPanel(new ToolDelegate(canvas));
//        toolPanel.selectDefault();

        JPanel p = new JPanel(new BorderLayout());
//        p.add(makeToolBar(), BorderLayout.NORTH);
//        p.add(toolPanel, BorderLayout.WEST);
        scroll.getVerticalScrollBar().setUnitIncrement(10);
        scroll.getHorizontalScrollBar().setUnitIncrement(10);
        p.add(scroll, BorderLayout.CENTER);
//        p.add(pal, BorderLayout.SOUTH);

        frame = new JFrame();
//        frame.setJMenuBar(makeMenuBar());
        frame.setContentPane(p);
        frame.setMinimumSize(new Dimension(500, 500));
//		updateTitle();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
//                doClose();
            }
        });
//        new FramePrefsHandler(frame);
    }

    public static BufferedImage createWhiteBufferedImage(int w, int h) {
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);
        return image;
    }
    
    
}
