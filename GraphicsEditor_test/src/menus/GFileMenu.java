package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import frames.GDrawingPanel;
import gloval.Constants;

public class GFileMenu extends JMenu {
    private static final long serialVersionUID = 1L;
    private GDrawingPanel drawingpanel;

    public void associate(GDrawingPanel drawingpanel) {
        this.drawingpanel = drawingpanel;
    }

    public GFileMenu(String s) {
        super(s);
        ActionHandler actionHandler = new ActionHandler();

        JMenuItem menuItemNew = new JMenuItem("새로 만들기");
        menuItemNew.setActionCommand("새로 만들기");
        menuItemNew.addActionListener(actionHandler);
        this.add(menuItemNew);

        JMenuItem menuItemOpen = new JMenuItem("열기");
        menuItemOpen.setActionCommand("열기");
        menuItemOpen.addActionListener(actionHandler);
        this.add(menuItemOpen);

        JMenuItem menuItemSave = new JMenuItem("저장");
        menuItemSave.setActionCommand("저장");
        menuItemSave.addActionListener(actionHandler);
        this.add(menuItemSave);

        JMenuItem menuItemDiffrentSave = new JMenuItem("다른 이름으로 저장");
        menuItemDiffrentSave.setActionCommand("다른 이름으로 저장");
        menuItemDiffrentSave.addActionListener(actionHandler);
        this.add(menuItemDiffrentSave);
    }

    private void newFile() {
        this.drawingpanel.clear();
        
    }

    private void open() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) { // 파일이 선택된 경우
            File file = fileChooser.getSelectedFile();
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
                Object object = objectInputStream.readObject();
                this.drawingpanel.setShapes(object);
                this.drawingpanel.repaint();
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(this, "Error opening file: " + e.getMessage(), "Open Error", JOptionPane.ERROR_MESSAGE);
         
            }
        }
    }


    public void save() {
        File file = new File(Constants.DEFAULT_FILE_PATH);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            objectOutputStream.writeObject(this.drawingpanel.getShapes());
            JOptionPane.showMessageDialog(this,  Constants.DEFAULT_FILE_PATH + " 저장 성공", "저장 성공", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
   
        }
    }

    public void saveAs() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getAbsolutePath().endsWith(".ser")) {
                file = new File(file.getAbsolutePath() + ".ser");
            }
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
                objectOutputStream.writeObject(this.drawingpanel.getShapes());
                JOptionPane.showMessageDialog(this, file.getAbsolutePath() + " 다른 이름으로 저장 성공", "다른 이름으로 저장 성공", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage(), "Save As Error", JOptionPane.ERROR_MESSAGE);
         
            }
        }
    }

    private class ActionHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "새로 만들기":
                    newFile();
                    break;
                case "열기":
                    open();
                    break;
                case "저장":
                    save();
                    break;
                case "다른 이름으로 저장":
                    saveAs();
                    break;
            }
        }
    }

    public void initialize() {}
}
