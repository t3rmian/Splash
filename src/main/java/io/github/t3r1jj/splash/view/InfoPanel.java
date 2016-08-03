/* 
 * Copyright 2016 Damian Terlecki.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.t3r1jj.splash.view;

import java.awt.*;
import java.util.Observable;
import javax.swing.*;

import io.github.t3r1jj.splash.controller.Controller;
import io.github.t3r1jj.splash.model.ImageModel;
import io.github.t3r1jj.splash.util.Messages;

public class InfoPanel extends JPanel implements View {

    private final JLabel position;
    private final Controller controller;

    public InfoPanel(Controller controller) {
        this.controller = controller;
        position = new JLabel(Messages.getString("InfoPanel.Position") + ": 0, 0 px"); //$NON-NLS-1$ //$NON-NLS-2$
        this.add(position, BorderLayout.CENTER);
    }

    @Override
    public void update(Observable o, Object arg) {
        Point currentPosition = ((ImageModel) o).getCurrentMousePoint();
        if (currentPosition.equals(new Point(-1, -1))) {
            position.setText(Messages.getString("InfoPanel.Position")); //$NON-NLS-1$
        } else {
            position.setText(Messages.getString("InfoPanel.Position") + ": " + currentPosition.x + ", " + currentPosition.y + " px"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        }
    }

    @Override
    public Graphics paintLayer(Graphics g) {
        throw new UnsupportedOperationException("Not supported yet."); //$NON-NLS-1$
    }

}