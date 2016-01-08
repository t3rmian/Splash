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
package ztppro.view;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Observable;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import ztppro.controller.Controller;
import ztppro.model.*;
import ztppro.util.ImageUtil;
import ztppro.util.Messages;

public class LayersDialog extends JDialog implements View {
    
    private static final String[] tableHeaderTooltips = {Messages.getString("LayersDialog.Visibility"), Messages.getString("LayersDialog.Name"), Messages.getString("LayersDialog.Opacity")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    private JTable layersTable = new JTable();
    private final JPopupMenu popupMenu;
    private final LayersModel layersModel;
    private Controller controller;
    private final JMenuItem mergeDown;
    private final JScrollPane scrollPane = new JScrollPane();
    private int defaultRowHeight = -1;
    private boolean isPopupMenuVisible = false;
    
    public LayersDialog(LayersModel layersModel, Controller controller) {
        setIconImage(appIcon);
        setTitle(Messages.getString("LayersDialog.Layers")); //$NON-NLS-1$
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        this.layersModel = layersModel;
        this.layersTable.setModel(layersModel);
        this.controller = controller;
        
        layersTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (layersTable.getSelectedRow() != -1 && !e.getValueIsAdjusting()) {
                ImageModel selectedModel = (ImageModel) layersTable.getValueAt(layersTable.getSelectedRow(), 1);
                for (ImageModel model : layersModel.getLayers()) {
                    if (!selectedModel.equals(model)) {
                        model.setFocus(false);
                    }
                }
                selectedModel.setFocus(true);
                layersTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                layersTable.getColumnModel().getColumn(0).setMaxWidth(3);
                layersTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
                layersTable.getColumnModel().getColumn(2).setCellEditor(new SpinnerEditor());
            }
        });
        
        layersTable.getModel().addTableModelListener((TableModelEvent e) -> {
            if (e.getType() == TableModelEvent.INSERT) {
                SwingUtilities.invokeLater(() -> {
                    ((ImageModel) layersTable.getValueAt(layersModel.getRowCount() - 1, 1)).addObserver(LayersDialog.this);
                    if (defaultRowHeight == -1) {
                        defaultRowHeight = layersTable.getRowHeight() + 5;
                    }
                    layersTable.setRowHeight(defaultRowHeight);
                    setTableSizes();
                });
            } else {
                SwingUtilities.invokeLater(() -> {
                    if (layersTable.getRowCount() > 0) {
                        setTableSizes();
                    }
                });
            }
        });
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportView(panel);
        ToolTipHeader header = new ToolTipHeader(layersTable.getColumnModel());
        header.setToolTipStrings(tableHeaderTooltips);
        layersTable.setTableHeader(header);
        panel.add(layersTable.getTableHeader(), BorderLayout.PAGE_START);
        panel.add(layersTable);
        add(scrollPane);
        initTableOptions();
        
        popupMenu = new JPopupMenu();
        mergeDown = new JMenuItem(Messages.getString("LayersDialog.MergeDown")); //$NON-NLS-1$
        initPopupMenu(layersModel, controller);
        pack();
        setLocation(Toolkit.getDefaultToolkit().getScreenSize().width - getPreferredSize().width, 50);
        setVisible(true);
    }
    
    @Override
    public void update(Observable o, Object arg) {
    }
    
    @Override
    public Graphics paintLayer(Graphics g) {
        throw new UnsupportedOperationException("Not supported yet."); //$NON-NLS-1$
    }
    
    @Override
    public void paintImmediately(int x, int y, int width, int height) {
        throw new UnsupportedOperationException("Not supported yet."); //$NON-NLS-1$
    }
    
    private void initTableOptions() {
        layersTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        layersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        layersTable.setDragEnabled(true);
        layersTable.setTransferHandler(new ImageModelTransferHandler());
        layersTable.setDropMode(DropMode.INSERT);
    }
    
    private void initPopupMenu(LayersModel layersModel1, Controller controller1) {
        layersTable.addMouseListener(new TableMouseListener());
        JMenuItem menuItemRemove = new JMenuItem(Messages.getString("LayersDialog.DeleteLayer")); //$NON-NLS-1$
        menuItemRemove.addActionListener((ActionEvent ae) -> {
            ImageModel deletion = (ImageModel) layersModel1.getValueAt(layersTable.getSelectedRow(), 1);
            controller1.disposeLayer(deletion);
            layersModel1.removeLayer(layersTable.getSelectedRow());
            resizeTable();
        });
        if (layersTable.getSelectedRow() == layersTable.getSelectedRowCount() - 1) {
            menuItemRemove.setEnabled(false);
        }
        mergeDown.addActionListener((ActionEvent ae) -> {
            ImageModel merge = (ImageModel) layersModel1.getValueAt(layersTable.getSelectedRow(), 1);
            controller1.mergeDown(merge);
            layersModel1.removeLayer(layersTable.getSelectedRow());
            resizeTable();
        });
        JMenuItem scale = new JMenuItem(Messages.getString("LayersDialog.Scale")); //$NON-NLS-1$
        scale.addActionListener((ActionEvent ae) -> {
            ResizeDialog userInput = new ResizeDialog(Messages.getString("LayersDialog.LayerScaling"), controller1.getModel().getImage().getWidth(), controller1.getModel().getImage().getHeight()); //$NON-NLS-1$
            int width = userInput.getResizedWidth();
            int height = userInput.getResizedHeight();
            if (controller1.getModel().getImage().getWidth() != width || controller1.getModel().getImage().getHeight() != height) {
                controller1.getModel().setImage(ImageUtil.imageToBufferedImage(controller1.getModel().getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)));
                controller1.repaintAllLayers();
            }
        });
        JMenuItem offsetChange = new JMenuItem(Messages.getString("LayersDialog.Move")); //$NON-NLS-1$
        offsetChange.addActionListener((ActionEvent ae) -> {
            ImageModel model = (ImageModel) layersModel1.getValueAt(layersTable.getSelectedRow(), 1);
            OffsetChangeJDialog userInput = new OffsetChangeJDialog(model.getXOffset(), model.getYOffset());
            if (!userInput.isCancelled()) {
                model.setOffset(new Point(userInput.getXOffset(), userInput.getYOffset()));
                if (controller != null) {
                    controller.repaintAllLayers();
                }
            }
        });
        
        JMenuItem resize = new JMenuItem(Messages.getString("LayersDialog.Resize")); //$NON-NLS-1$
        resize.addActionListener((ActionEvent ae) -> {
            ResizeDialog userInput = new ResizeDialog(Messages.getString("LayersDialog.ImageResizing"), controller1.getModel().getImage().getWidth(), controller1.getModel().getImage().getHeight()); //$NON-NLS-1$
            int width = userInput.getResizedWidth();
            int height = userInput.getResizedHeight();
            if (controller1.getModel().getImage().getWidth() != width || controller1.getModel().getImage().getHeight() != height) {
                BufferedImage resizedImage = new BufferedImage(width, height, controller1.getModel().getImage().getType());
                Graphics2D g2d = (Graphics2D) resizedImage.getGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, width, height);
                g2d.dispose();
                resizedImage = ImageUtil.imageToBufferedImage(ImageUtil.makeColorTransparent(resizedImage, Color.WHITE));
                g2d = (Graphics2D) resizedImage.getGraphics();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                g2d.drawImage(controller1.getModel().getImage(), 0, 0, null);
                g2d.dispose();
                controller1.getModel().setImage(resizedImage);
                controller1.repaintAllLayers();
            }
        });
        popupMenu.add(scale);
        popupMenu.add(resize);
        popupMenu.add(offsetChange);
        popupMenu.add(menuItemRemove);
        popupMenu.add(mergeDown);
        popupMenu.addPopupMenuListener(new PopupMenuListener() {
            
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                controller = controller1.getChild();
                isPopupMenuVisible = true;
            }
            
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                isPopupMenuVisible = false;
            }
            
            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
        layersTable.setComponentPopupMenu(popupMenu);
        
    }
    
    private void setTableSizes() throws HeadlessException {
        layersTable.setRowSelectionInterval(0, 0);
        layersTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        layersTable.getColumnModel().getColumn(0).setMaxWidth(3);
        layersTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        LayersDialog.this.revalidate();
        LayersDialog.this.pack();
        if (getLocation().x + getPreferredSize().width > Toolkit.getDefaultToolkit().getScreenSize().width) {
            setLocation(Toolkit.getDefaultToolkit().getScreenSize().width - getPreferredSize().width, 50);
        }
        LayersDialog.this.repaint();
    }
    
    private void resizeTable() {
        layersTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        layersTable.getColumnModel().getColumn(0).setMaxWidth(3);
        layersTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    }
    
    public boolean isPopupMenuVisible() {
        return isPopupMenuVisible;
    }
    
    public class ImageModelTransferHandler extends TransferHandler {
        
        private int fromIndex = -1;
        private int toIndex;
        private boolean samePlace = true;
        private final DataFlavor imageModelFlavor;
        
        ImageModelTransferHandler() {
            imageModelFlavor = new DataFlavor(ImageModel.class, "imageModel"); //$NON-NLS-1$
        }
        
        @Override
        public boolean canImport(TransferHandler.TransferSupport info) {
            return info.isDataFlavorSupported(imageModelFlavor);
        }
        
        @Override
        protected Transferable createTransferable(JComponent c) {
            JTable table = (JTable) c;
            fromIndex = table.getSelectedRow();
            return (Transferable) table.getModel().getValueAt(fromIndex, 1);
        }
        
        @Override
        public int getSourceActions(JComponent c) {
            return TransferHandler.COPY_OR_MOVE;
        }
        
        @Override
        public boolean importData(TransferHandler.TransferSupport info) {
            if (!info.isDrop()) {
                return false;
            }
            JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();
            int index = dl.getRow();
            JTable table = (JTable) info.getComponent();
            if (index == fromIndex || fromIndex == table.getRowCount() - 1) {
                samePlace = true;
                return false;
            } else {
                if (fromIndex > index) {
                    fromIndex++;    //after copying index will increase
                }
                samePlace = false;
            }
            if (index == table.getRowCount()) {
                index--;
                if (index < 0) {
                    return false;
                }
            }
            
            LayersModel listModel = (LayersModel) table.getModel();
            
            Transferable t = info.getTransferable();
            ImageModel data;
            try {
                data = (ImageModel) t.getTransferData(imageModelFlavor);
            } catch (UnsupportedFlavorException | IOException e) {
                return false;
            }
            listModel.addLayer(index, data);
            toIndex = index;
            return true;
        }
        
        @Override
        protected void exportDone(JComponent c, Transferable data, int action) {
            if (fromIndex != -1 && !samePlace) {
                JTable table = (JTable) c;
                ((LayersModel) table.getModel()).removeLayer(fromIndex);
                if (fromIndex < toIndex) {
                    toIndex--;
                }
                layersTable.setRowSelectionInterval(toIndex, toIndex);
                if (fromIndex == table.getRowCount() - 1 || toIndex == table.getRowCount() - 1) {
                    ((LayersModel) table.getModel()).getLayers().get(table.getRowCount() - 1).notifySizeChange();
                }
            }
            fromIndex = -1;
            samePlace = true;
        }
        
        protected ImageModel exportImageModel(JComponent c) {
            JTable table = (JTable) c;
            return (ImageModel) table.getModel().getValueAt(table.getSelectedRow(), 1);
        }
        
    }
    
    private class TableMouseListener extends MouseAdapter {
        
        public TableMouseListener() {
        }
        
        @Override
        public void mousePressed(MouseEvent event) {
            Point point = event.getPoint();
            int pressedRow = layersTable.rowAtPoint(point);
            layersTable.setRowSelectionInterval(pressedRow, pressedRow);
            if (pressedRow == layersTable.getRowCount() - 1) {
                mergeDown.setEnabled(false);
            } else {
                mergeDown.setEnabled(true);
            }
        }
    }
    
    class ToolTipHeader extends JTableHeader {
        
        String[] toolTips;
        
        public ToolTipHeader(TableColumnModel model) {
            super(model);
        }
        
        public String getToolTipText(MouseEvent e) {
            int col = columnAtPoint(e.getPoint());
            int modelCol = getTable().convertColumnIndexToModel(col);
            String retStr;
            try {
                retStr = toolTips[modelCol];
            } catch (NullPointerException ex) {
                retStr = ""; //$NON-NLS-1$
            } catch (ArrayIndexOutOfBoundsException ex) {
                retStr = ""; //$NON-NLS-1$
            }
            if (retStr.length() < 1) {
                retStr = super.getToolTipText(e);
            }
            return retStr;
        }
        
        public void setToolTipStrings(String[] toolTips) {
            this.toolTips = toolTips;
        }
    }
    
    class SpinnerEditor extends DefaultCellEditor {
        
        private final JSpinner spinner;
        
        public SpinnerEditor() {
            super(new JTextField());
            spinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
            spinner.setBorder(null);
        }
        
        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected, int row, int column) {
            spinner.setValue(value);
            return spinner;
        }
        
        @Override
        public Object getCellEditorValue() {
            return spinner.getValue();
        }
    }
}
