package frames;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import gloval.Constants;

public class GMainFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    // 부품
    private GMenuBar menuBar;           
    private GShapeToolBar shapetoolBar; 
    private GDrawingPanel drawingPanel; 
    private SlideListPanel slideListPanel; 
    private Vector<GDrawingPanel> slides;  

    public GMainFrame() {
        // 프레임 속성 설정
        this.setSize(Constants.GMainFrame.WIDTH, Constants.GMainFrame.HEIGHT);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null); 

        // 레이아웃 설정
        BorderLayout layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        // 메뉴 바 추가
        this.menuBar = new GMenuBar();
        this.setJMenuBar(this.menuBar);

        // 도형 툴바 추가
        this.shapetoolBar = new GShapeToolBar(this);
        this.add(shapetoolBar, BorderLayout.NORTH);

        // 슬라이드 목록 초기화
        this.slides = new Vector<>();
        this.drawingPanel = new GDrawingPanel();
        slides.add(drawingPanel); 
        this.slideListPanel = new SlideListPanel(slides);

        // 슬라이드 목록과 그림 그리는 패널을 나누기
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, slideListPanel, drawingPanel);
        splitPane.setDividerLocation(62);
        this.add(splitPane, BorderLayout.CENTER);

    
        // 슬라이드 목록 패널에 마우스 리스너 
        slideListPanel.getSlideList().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList<?> list = (JList<?>) e.getSource();
                if (e.getClickCount() == 2) {
                    int index = list.locationToIndex(e.getPoint());
                    if (index >= 0) {
                 
                        drawingPanel = slides.get(index);
                        splitPane.setRightComponent(drawingPanel);
                        menuBar.associate(drawingPanel);
                        shapetoolBar.associate(drawingPanel);
                        shapetoolBar.initialize(); 
                    }
                }
            }
        });

        // 윈도우 종료 이벤트 처리
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit(); 
            }
        });
      
        // 부품과 연결
        this.menuBar.associate(this.drawingPanel);
        this.shapetoolBar.associate(this.drawingPanel);

    }

    // 새로운 슬라이드 추가
    public void addNewSlide() {
        GDrawingPanel newSlide = new GDrawingPanel();
        slides.add(newSlide);
        slideListPanel.addSlide(newSlide);
    }

    // 부품 초기화
    public void initialize() {
        this.menuBar.initialize();
        this.shapetoolBar.initialize();
        this.drawingPanel.initialize();
    }

    // 종료시 저장확인
    private void confirmExit() {
        String[] options = {"저장", "저장 안 함", "취소"};
        int option = JOptionPane.showOptionDialog(
            this, "변경 사항을 저장하시겠습니까?", "종료",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null, options, options[0]
        );

        if (option == JOptionPane.YES_OPTION) {
            menuBar.exitsave(); // 저장하고 종료
            System.exit(0);
        } else if (option == JOptionPane.NO_OPTION) {
            System.exit(0); // 저장하지 않고 종료
        }
    }
}
