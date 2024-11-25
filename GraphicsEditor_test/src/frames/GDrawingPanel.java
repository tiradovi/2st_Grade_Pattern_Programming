package frames;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JColorChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.awt.Graphics2D;
import shapetools.GShape;
import shapetools.GShape.EAnchors;
import shapetools.GShape.EDrawingStyle;

public class GDrawingPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private enum EDrawingState {
		eIdle, e2PState, eNPState, eTransformation
	}

	private EDrawingState eDrawingState;

	private Vector<GShape> shapes;
	private GShape shapeTool;
	private GShape currentShape;
	private JPopupMenu ShapeMenu,GroundMenu;
	private BufferedImage bufferedImage;
	private Graphics2D graphics;
	private Graphics2D bufferedGraphics;
    private Map<Integer, GShape> copiedShapes;
    private int copyCount; 
    
	public GDrawingPanel() {
		this.eDrawingState = EDrawingState.eIdle;
		this.setBackground(Color.gray);
		MouseEventHandler mouseEventHandler = new MouseEventHandler();
		this.addMouseListener(mouseEventHandler);
		this.addMouseMotionListener(mouseEventHandler);
		this.shapes = new Vector<GShape>();
		ShapeMenu();
		GroundMenu();
		  this.copiedShapes = new HashMap<>();
	        this.copyCount = 0;
	}

	public void initialize() {
	}

	// 도형 저장
	public void saveShapes(String fileName) {
		try {
			FileOutputStream fileOut = new FileOutputStream(fileName);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(shapes);
			out.close();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 도형 불러오기
	public void loadShapes(String fileName) {
		try {
			FileInputStream fileIn = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			shapes = (Vector<GShape>) in.readObject();
			in.close();
			fileIn.close();
			repaint();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// 도형 초기화
	public void clear() {
		this.shapes.clear();
		this.repaint();
	}

	// 게터 및 세터
	public void setShapeTool(GShape shapeTool) {
		this.shapeTool = shapeTool;
	}

	public Vector<GShape> getShapes() {
		return this.shapes;
	}

	public void setShapes(Object object) {
		this.shapes = (Vector<GShape>) object;
	}

	@Override
	public void paint(Graphics graphics) {
		repaint();
		super.paintComponent(graphics);
		for (GShape shape : shapes) {
			shape.draw(graphics);
		}
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		for (GShape shape : shapes) {
			Graphics2D graphics2D = (Graphics2D) graphics.create();
			shape.setColor(shape.getColor());
			shape.draw(graphics2D);
			graphics2D.dispose();
		}
	}

	// 그리기 시작
	private void startDrawing(int x, int y) {
		bufferedImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
		bufferedGraphics = (Graphics2D) bufferedImage.getGraphics();
		bufferedGraphics.drawImage(bufferedImage, 0, 0, null);

		bufferedImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_BGR);

		graphics = (Graphics2D) bufferedImage.getGraphics();
		graphics.setColor(this.getForeground());
		graphics.setBackground(this.getBackground());
		graphics.clearRect(0, 0, this.getWidth(), this.getHeight());

		currentShape = shapeTool.clone();
		currentShape.setOrigin(x, y);
	}

	// 그리는 중
	private void keepDrawing(int x, int y) {
		currentShape.movePoint(x, y);

		graphics.clearRect(0, 0, this.getWidth(), this.getHeight());
		currentShape.drag(graphics);
		getGraphics().drawImage(bufferedImage, 0, 0, this.getWidth(), this.getHeight(), this);
	}

	// 계속 그리기
	private void ContinueDrawing(int x, int y) {
		currentShape.addPoint(x, y);
	}

	// 그리기 종료
	private void stopDrawing(int x, int y) {
		shapes.add(currentShape);
		deselectAllShapes();
		currentShape.setSelected(getGraphics());
		repaint();
	}

	// 모든 도형 선택 해제
	private void deselectAllShapes() {
		for (GShape shape : shapes) {
			shape.clearSelected();
		}
		repaint();
	}

	// 도형이 클릭된 경우
	private GShape onShape(int x, int y) {
		for (GShape shape : this.shapes) {
			boolean isShape = shape.onShape(x, y);
			if (isShape) {
				deselectAllShapes();
				shape.setSelected(getGraphics());
				return shape;
			}
		}
		return null;
	}

	// 커서 변경
	private void changeCursor(int x, int y) {
		GShape shape = this.onShape(x, y);
		if (shape == null) {
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		} else {
			this.setCursor(shape.getCursor());
		}
	}

	// 팝업 메뉴 초기화
	private void ShapeMenu() {
	    ShapeMenu = new JPopupMenu();

	    JMenuItem deleteItem = new JMenuItem("삭제");
	    deleteItem.addActionListener(e -> deleteShape());

	    JMenuItem copyItem = new JMenuItem("복사");
	    copyItem.addActionListener(e -> copyShape());

	    JMenuItem changeColorItem = new JMenuItem("테두리 색 변경");
	    changeColorItem.addActionListener(e -> changeShapeLineColor());

	    JMenuItem changeFillColorItem = new JMenuItem("내부 색 변경");
	    changeFillColorItem.addActionListener(e -> changeShapeFillColor());

	    JMenuItem changeStrokeWidthItem = new JMenuItem("테두리 굵기 변경");
	    changeStrokeWidthItem.addActionListener(e -> changeShapeStrokeWidth());
	    JMenuItem showPropertiesItem = new JMenuItem("속성 표시");
	    showPropertiesItem.addActionListener(e -> showShapeProperties());

	    ShapeMenu.add(deleteItem);
	    ShapeMenu.add(copyItem);
	    ShapeMenu.add(changeColorItem);
	    ShapeMenu.add(changeFillColorItem);
	    ShapeMenu.add(changeStrokeWidthItem);
	    ShapeMenu.add(showPropertiesItem);
	}



	private void GroundMenu() {
	    GroundMenu = new JPopupMenu();
	    
	    JMenuItem pasteItem = new JMenuItem("붙여넣기");
	    pasteItem.addActionListener(e -> pasteShape());
	    
	    
	    GroundMenu.add(pasteItem);
	}


    // 도형 복사
    private void copyShape() {
        if (currentShape != null) {
            GShape copiedShape = currentShape.clone();
            shapes.add(copiedShape);
            deselectAllShapes();
            copiedShape.setSelected(getGraphics());
            this.copiedShapes.put(this.copyCount++, copiedShape);
            repaint();
        }
    }

    // 도형 붙여넣기
    private void pasteShape() {
        // 패널 크기의 절반 계산
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        if (!this.copiedShapes.isEmpty()) {
            // 복사된 도형 중 가장 최근 것 가져오기
            GShape pastedShape = this.copiedShapes.get(this.copyCount -1).clone();

            // 패널 중앙 위치에 도형 위치 조정
            pastedShape.movePoint(centerX, centerY);
            shapes.add(pastedShape);

            pastedShape.setSelected(getGraphics());
   
        }
    }

	// 도형 삭제
	private void deleteShape() {
		if (currentShape != null) {
			shapes.remove(currentShape);
			currentShape = null;
			repaint();
		}
	}

	// 도형 선 색깔 변경
	private void changeShapeLineColor() {
		if (currentShape != null) {
			Color newColor = JColorChooser.showDialog(this, "도형 테두리 색 변경", Color.BLACK);
			if (newColor != null) {
				currentShape.setColor(newColor);
				repaint();
			}
		}
	}
	// 도형  색깔 변경
	private void changeShapeFillColor() {
		if (currentShape != null) {
			Color newColor = JColorChooser.showDialog(this, "도형 내부 색 변경", Color.WHITE);

			if (newColor != null) {
				currentShape.setFillColor(newColor);
				repaint();
			}
		}
	}
	// 선택된 도형의 테두리 굵기 변경
	private void changeShapeStrokeWidth() {
	    if (currentShape != null) {
	        String input = JOptionPane.showInputDialog(this, "새로운 테두리 굵기를 입력하세요:");
	        try {
	            int strokeWidth = Integer.parseInt(input);
	            if (strokeWidth > 0) {
	            	currentShape.setLineWidth(strokeWidth);
	                repaint();
	            } else {
	                JOptionPane.showMessageDialog(this, "테두리 굵기는 0보다 커야 합니다.", "오류", JOptionPane.ERROR_MESSAGE);
	            }
	        } catch (NumberFormatException e) {
	            JOptionPane.showMessageDialog(this, "유효한 숫자를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
	        }
	    } else {
	        JOptionPane.showMessageDialog(this, "도형이 선택되지 않았습니다.", "경고", JOptionPane.WARNING_MESSAGE);
	    }
	}
	// 선택된 도형의 속성을 표시
	private void showShapeProperties() {
	    if (currentShape != null) {
	        JOptionPane.showMessageDialog(this, "도형 속성\n" +
	                "이름: " + currentShape.getEDrawingStyle() + "\n" +
	                "테두리 색: " + currentShape.getColor() + "\n" +
	                "내부 색: " + currentShape.getFillColor());
	    } else {
	        JOptionPane.showMessageDialog(this, "도형이 선택되지 않았습니다.", "경고", JOptionPane.WARNING_MESSAGE);
	    }
	}

	// 마우스 이벤트 처리기
	private class MouseEventHandler implements MouseListener, MouseMotionListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (e.getClickCount() == 1) {
					mouseleft1Clicked(e);
				} else if (e.getClickCount() == 2) {
					mouseleft2Clicked(e);
				}
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				mouseRightClicked(e);
			}
		}

		private void mouseleft1Clicked(MouseEvent e) {
			if (eDrawingState == EDrawingState.eIdle) {
				if (shapeTool.getEDrawingStyle() == EDrawingStyle.eNPstyle) {
					startDrawing(e.getX(), e.getY());
					eDrawingState = EDrawingState.eNPState;
				}
			} else if (eDrawingState == EDrawingState.eNPState) {
				ContinueDrawing(e.getX(), e.getY());
				eDrawingState = EDrawingState.eNPState;
			}
		}

		private void mouseleft2Clicked(MouseEvent e) {
			if (eDrawingState == EDrawingState.eNPState) {
				stopDrawing(e.getX(), e.getY());
				eDrawingState = EDrawingState.eIdle;
			}
		}

		private void mouseRightClicked(MouseEvent e) {
			currentShape = onShape(e.getX(), e.getY());
			if (currentShape != null) {
				ShapeMenu.show(e.getComponent(), e.getX(), e.getY());
			}if (currentShape == null) {
				
				GroundMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if (eDrawingState == EDrawingState.eIdle) {
				changeCursor(e.getX(), e.getY());
			} else if (eDrawingState == EDrawingState.eNPState) {
				keepDrawing(e.getX(), e.getY());
				eDrawingState = EDrawingState.eNPState;
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (eDrawingState == EDrawingState.eIdle) {
					currentShape = onShape(e.getX(), e.getY());
					if (currentShape == null) {
						if (shapeTool.getEDrawingStyle() == EDrawingStyle.e2Pstyle) {
							startDrawing(e.getX(), e.getY());
							eDrawingState = EDrawingState.e2PState;
						}
					} else {
						if (currentShape.getSelectedAnchor() == EAnchors.eMM) {
							currentShape.startMove(getGraphics(), e.getX(), e.getY());
						} else if (currentShape.getSelectedAnchor() == EAnchors.eRR) {
							currentShape.startRotate(getGraphics(), e.getX(), e.getY());
						} else {
							currentShape.startResize(getGraphics(), e.getX(), e.getY());
						}
						eDrawingState = EDrawingState.eTransformation;
					}
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (eDrawingState == EDrawingState.e2PState) {
				keepDrawing(e.getX(), e.getY());
			} else if (eDrawingState == EDrawingState.eTransformation) {
				currentShape.keepTransformation(getGraphics(), e.getX(), e.getY());
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (eDrawingState == EDrawingState.e2PState) {
					stopDrawing(e.getX(), e.getY());
					eDrawingState = EDrawingState.eIdle;
				} else if (eDrawingState == EDrawingState.eTransformation) {
					currentShape.finishTransformation(getGraphics());
					repaint();
					eDrawingState = EDrawingState.eIdle;
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
	}
}
