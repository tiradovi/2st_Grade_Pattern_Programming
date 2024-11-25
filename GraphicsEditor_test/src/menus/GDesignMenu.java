package menus;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JColorChooser;

import frames.GDrawingPanel;

public class GDesignMenu extends JMenu {
    private GDrawingPanel drawingPanel;

    public void associate(GDrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }

    public GDesignMenu(String text) {
        super(text);
        ActionHandler actionHandler = new ActionHandler();

        JMenuItem changeColorItem = new JMenuItem("배경 색 변경");
        changeColorItem.setActionCommand("배경 색 변경");
        changeColorItem.addActionListener(actionHandler);
        this.add(changeColorItem);

        JMenuItem changePatternItem = new JMenuItem("배경 패턴 변경");
        changePatternItem.setActionCommand("배경 패턴 변경");
        changePatternItem.addActionListener(actionHandler);
        this.add(changePatternItem);
    }

    private void changeBackgroundColor() {
        if (drawingPanel != null) {
            Color newColor = JColorChooser.showDialog(this, "Choose Background Color", drawingPanel.getBackground());
            if (newColor != null) {
                drawingPanel.setBackground(newColor);
            }
        }
    }

    private void changeBackgroundPattern() {
  
    }
    private class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "배경 색 변경":
                    changeBackgroundColor();
                    break;
                case "배경 패턴 변경":
                    changeBackgroundPattern();
                    break;
            }
        }
    }
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
}
