package frames;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import gloval.Constants.EShapeButtons;

public class GShapeToolBar extends JToolBar {

    private static final long serialVersionUID = 1L;
    private GDrawingPanel drawingPanel;
    private GMainFrame mainFrame;

    // 생성자
    public GShapeToolBar(GMainFrame mainFrame) {
        this.mainFrame = mainFrame;
        ButtonGroup buttonGroup = new ButtonGroup();
        ShapeActionHandler shapeActionHandler = new ShapeActionHandler();

        // 각 모양 버튼 추가
        for (EShapeButtons eShapeButtons : EShapeButtons.values()) {
            String fileName = eShapeButtons.getImageFileName();
            String relativePath = "src/" + fileName + ".png"; 
            ImageIcon icon = new ImageIcon(relativePath);
            Image image = icon.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
            icon = new ImageIcon(image);

            JToggleButton button = new JToggleButton(icon);
            button.setToolTipText(eShapeButtons.getText());
            button.addActionListener(shapeActionHandler);
            button.setActionCommand(eShapeButtons.toString());
            button.setPreferredSize(new Dimension(45, 45));

            buttonGroup.add(button);
            add(button);
        }

        // 'Add Slide' 버튼 추가
        JButton addSlideButton = new JButton("Add Slide");
        addSlideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.addNewSlide();
            }
        });
        this.add(addSlideButton);
    }

    // 툴바 초기화
    public void initialize() {
        if (drawingPanel != null) {
       
            JToggleButton defaultButton = (JToggleButton) (this.getComponent(EShapeButtons.eRectangle.ordinal()));
            defaultButton.doClick();
        }
    }

    // 툴바와 드로잉 패널 연결
    public void associate(GDrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }

    // 모양 설정
    private void setShapeTool(EShapeButtons eShapeButton) {
        this.drawingPanel.setShapeTool(eShapeButton.getShapeTool());
    }

    // 각 모양 버튼의 액션을 처리하는 내부 클래스
    private class ShapeActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 눌린 버튼에 해당하는 모양 설정
            EShapeButtons eShapeButton = EShapeButtons.valueOf(e.getActionCommand());
            setShapeTool(eShapeButton);
        }
    }
}
