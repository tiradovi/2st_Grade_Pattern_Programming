package frames;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import gloval.Constants;
import menus.GDesignMenu;
import menus.GFileMenu;

public class GMenuBar extends JMenuBar {
    private static final long serialVersionUID = 1L;
    private GDesignMenu designMenu;
    private GFileMenu fileMenu;
    private GDrawingPanel drawingpanel;

    public GMenuBar() {
        this.fileMenu = new GFileMenu(Constants.GMenubar.EMenu.eFile.getText());
        this.add(this.fileMenu);

        JMenu editMenu = new JMenu(Constants.GMenubar.EMenu.eEdit.getText());
        this.add(editMenu);

        this.designMenu = new GDesignMenu(Constants.GMenubar.EMenu.eDesign.getText());
        this.add(this.designMenu);
    }

    public void initialize() {
        this.fileMenu.initialize();
        this.designMenu.initialize();
    }

    public void associate(GDrawingPanel drawingpanel) {
        this.drawingpanel = drawingpanel;
        this.fileMenu.associate(drawingpanel);
        this.designMenu.associate(drawingpanel);
    }

    public void exitsave() {
        this.fileMenu.save();
    }
}
