package ztppro.view;

import java.awt.GridLayout;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Damian Terlecki
 */
public class ToolPanel extends JPanel {

    private static final long serialVersionUID = 1L;
//	protected ToolDelegate del;
    protected JPanel toolGrid;
    protected ButtonGroup buttonGroup = new ButtonGroup();
    protected JPanel toolOptions = new JPanel();

    AbstractButton defTool;

//    public ToolButton makeToolButton(String key, Tool t) {
//        ToolButton tb = new ToolButton(Resources.getString("ToolPanel." + key), t);
//        tb.setActionCommand(key);
//        tb.addActionListener(this);
//        //tb.setAccelerator(KeyStroke.getKeyStroke(Resources.getKeyboardString("TransformMenu." + key)));
//        tb.setIcon(Resources.getIconForKey("ToolPanel." + key));
//        return tb;
//    }

    public ToolPanel() {
        super();
        BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        this.setLayout(layout);

        toolGrid = new JPanel(new GridLayout(0, 2));

        addButton(new JButton("PENCIL"));
        addButton(new JButton("PAINTBRUSH"));
        //addButton(makeToolButton("ERASE",new EraseTool()));
        defTool = addButton(new JButton("LINE"));
        addButton(new JButton("COLOR_PICKER"));
        addButton(new JButton("OVAL"));
        addButton(new JButton("COLOR_FILL"));
        addButton(new JButton("RECT"));
        addButton(new JButton("ROUNDRECT"));
        addButton(new JButton("TEXT"));
        addButton(new JButton("GRADIENT_LINEAR"));

        toolOptions.setBorder(BorderFactory.createLoweredBevelBorder());
        toolOptions.setMaximumSize(toolGrid.getPreferredSize());
        toolGrid.setMaximumSize(toolGrid.getPreferredSize());

        add(toolGrid);
        add(toolOptions);
        add(new JPanel());
    }

    public void selectDefault() {
        defTool.doClick();
    }

    public <E extends AbstractButton> E addButton(E button) {
        toolGrid.add(button);
        buttonGroup.add(button);
//        button.addActionListener(this);
        return button;
    }

//    public class ToolButton extends JToggleButton {
//
//        private static final long serialVersionUID = 1L;
//
//        public final Tool tool;
//
//        public ToolButton(String tip, Tool t) {
//            this(null, tip, t);
//        }
//
//        public ToolButton(Tool t) {
//            this(null, null, t);
//        }
//
//        public ToolButton(ImageIcon ico, Tool t) {
//            this(ico, null, t);
//        }
//
//        public ToolButton(ImageIcon ico, String tip, Tool t) {
//            super(ico);
//            tool = t;
//            setToolTipText(tip);
//            setPreferredSize(new Dimension(32, 32));
//        }
//    }
//
//    public void actionPerformed(ActionEvent e) {
//        del.setTool(((ToolButton) e.getSource()).tool);
//        return;
//    }
//
//    JComponent curOpt = null;
//
//    public void showOptions(Tool tool) {
//        if (curOpt != null) {
//            toolOptions.remove(curOpt);
//        }
//        toolOptions.add(curOpt = tool.getOptionsComponent());
//        toolOptions.updateUI();
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
}
