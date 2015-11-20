package ztppro.view;

import java.awt.BorderLayout;
//import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import ztppro.controller.Controller;

/**
 *
 * @author Damian Terlecki
 */
public class MainView extends JFrame implements View {

    public JDesktopPane desktop;
    private Controller mainController;

    public MainView(Controller controller) {
        this.mainController = controller;
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        //Set up the GUI.
        desktop = new JDesktopPane(); //a specialized layered pane
        add(new ToolPanel(mainController), BorderLayout.WEST);
        add(new InfoPanel(), BorderLayout.SOUTH);
        add(new LayersPanel(), BorderLayout.EAST);
        add(desktop, BorderLayout.CENTER);
//        createFrame(); //create first "window"
        setJMenuBar(new Menu(controller));

        //Make dragging a little faster but perhaps uglier.
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

        //Make sure we have nice window decorations.
        MainView.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Display the window.
        this.setVisible(true);
    }

    @Override
    public void addToDesktop(MyInternalFrame frame) {
        desktop.add(frame);
    }

}
