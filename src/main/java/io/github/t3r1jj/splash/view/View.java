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
import java.util.Observer;
import javax.swing.ImageIcon;

public interface View extends Observer {
    
    Image appIcon = new ImageIcon(View.class.getClassLoader().getResource("images/splash.png")).getImage();

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
