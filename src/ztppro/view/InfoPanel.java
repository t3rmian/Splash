package ztppro.view;

import java.awt.*;
import java.util.Observable;
import javax.swing.*;
import ztppro.controller.Controller;
import ztppro.model.ImageModel;

/**
 *
 * @author Damian Terlecki
 */
public class InfoPanel extends JPanel implements View {

    private final JLabel position;
    private final Controller controller;

    public InfoPanel(Controller controller) {
        this.controller = controller;
        position = new JLabel("Pozycja: 0, 0 px");
        this.add(position, BorderLayout.CENTER);
        controller.getModel().addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        Point currentPosition = ((ImageModel) o).getCurrentMousePoint();
        if (currentPosition.equals(new Point(-1, -1))) {
            position.setText("Pozycja: ");
        } else {
            position.setText("Pozycja: " + currentPosition.x + ", " + currentPosition.y + " px");
        }
    }

    @Override
    public Graphics paintLayer(Graphics g) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
