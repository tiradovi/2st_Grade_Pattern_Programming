package frames;

import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.DefaultListCellRenderer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Vector;

public class SlideListPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JList<GDrawingPanel> slideList;
    private DefaultListModel<GDrawingPanel> listModel;

    // 생성자
    public SlideListPanel(Vector<GDrawingPanel> slides) {
        this.setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        for (GDrawingPanel slide : slides) {
            listModel.addElement(slide);
        }

        slideList = new JList<>(listModel);
        slideList.setCellRenderer(new SlideListCellRenderer());
        this.add(new JScrollPane(slideList), BorderLayout.CENTER);
    }

    // 슬라이드 목록 반환
    public JList<GDrawingPanel> getSlideList() {
        return slideList;
    }

    // 새로운 슬라이드 추가
    public void addSlide(GDrawingPanel slide) {
        listModel.addElement(slide);
    }

    // 슬라이드 목록의 셀 렌더러
    private class SlideListCellRenderer extends DefaultListCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setText("슬라이드" + (index + 1));
            return label;
        }
    }
}
