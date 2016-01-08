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
package ztppro.controller;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Observer;
import javax.swing.JFrame;
import ztppro.model.*;
import ztppro.util.io.exception.UnsupportedExtension;
import ztppro.view.menu.Menu;
import ztppro.view.View;

public interface Controller extends MouseMotionListener, MouseListener, Observer {

    void setView(View view);

    void setModel(ImageModel model);

    View getView();

    ImageModel getModel();

    void choosePencil();

    void choosePaintbrush();

    void chooseLine();

    void chooseForegroundColor(Color color);

    void chooseBackgroundColor(Color backgroundColor);

    void chooseErase();

    void chooseOval();

    void chooseFilling();

    void chooseRectangle();

    void chooseSelect(boolean transparent);

    void addCanvasController(Controller canvasController);

    boolean undo();

    boolean redo();

    boolean copy();

    boolean paste();

    void loseFocus();

    void frameActivated(JFrame frame, Menu menu, ImageModel model);

    void setLayersModel(LayersModel layersModel);

    void addChildController(CanvasController controller);

    void setParent(Controller controller);

    void setChild(Controller controller);

    Controller getChild();

    Controller getParent();

    void repaintLayers(Graphics g);

    void swapChainTowardsTop();

    void swapChainTowardsBottom();

    void chooseMove();

    void repaintAllLayers();

    void chooseColorPicker();

    void chooseText();

    void chooseZoom();

    void chooseTriangle();

    void chooseSpray();

    void chooseRoundedRectangle();

    void saveToFile(File chosenFile, String extension) throws IOException, UnsupportedExtension;

    LayersModel getLayersModel();

    void openFile(File chosenFile) throws IOException, ClassNotFoundException, UnsupportedExtension;

    void invert(boolean layer);

    void rotate(double angle, boolean layer);

    void changeBrightnessContrast(double brightnessPercentage, double brightnessContrast, boolean layer);

    void blur(boolean layer);

    void autoWhiteBalance(boolean layer);

    void sharpen(boolean layer);

    void setDrawingSize(int size);

    void chooseBrokenLine();

    void disposeLayer(ImageModel deletion);

    void mergeDown(ImageModel merge);

    void setViewCursor(Cursor cursor);
    
    void setViewDrawingColors(Color foreground, Color background);

    boolean selectAll();

    boolean delete();

    void resize();

    void changeOffset();

    void scale();
    
    boolean isUndoHistoryEmpty();
    
    boolean isRedoHistoryEmpty();
    
}
