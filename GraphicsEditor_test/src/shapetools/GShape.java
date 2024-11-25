package shapetools;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.awt.BasicStroke;
import gloval.Constants;

public abstract class GShape implements Serializable {
	private static final long serialVersionUID = 1L;

	// 속성
	private EDrawingStyle eDrawingStyle;
	private EAnchors eSelectedAnchor;
	private boolean bSelected;

	// 색상
	protected Color color;
	protected Color fillColor;

	// 도형과 그 외 변수
	protected Shape shape;
	protected Ellipse2D.Float[] anchors;

	// 위치 변수
	protected int x1, y1, x2, y2, ox2, oy2;

	// 변형 변수
	private double sx, sy; // 크기 조절용
	private double dx, dy; // 이동용
	private double startAngle, rotationAngle; // 회전용

	// 선 두께
	protected int lineWidth;

	// 그리기 스타일
	public enum EDrawingStyle {
		e2Pstyle, eNPstyle
	}

	// 앵커 위치
	public enum EAnchors {
		eRR(new Cursor(Cursor.HAND_CURSOR)), eNN(new Cursor(Cursor.N_RESIZE_CURSOR)),
		eSS(new Cursor(Cursor.S_RESIZE_CURSOR)), eEE(new Cursor(Cursor.E_RESIZE_CURSOR)),
		eWW(new Cursor(Cursor.W_RESIZE_CURSOR)), eNE(new Cursor(Cursor.NE_RESIZE_CURSOR)),
		eSE(new Cursor(Cursor.SE_RESIZE_CURSOR)), eNW(new Cursor(Cursor.NW_RESIZE_CURSOR)),
		eSW(new Cursor(Cursor.SW_RESIZE_CURSOR)), eMM(new Cursor(Cursor.CROSSHAIR_CURSOR));

		private Cursor cursor;

		private EAnchors(Cursor cursor) {
			this.cursor = cursor;
		}

		public Cursor getCursor() {
			return this.cursor;
		}
	}

	// 생성자
	public GShape(EDrawingStyle eDrawingStyle, Shape shape) {
		this.eDrawingStyle = eDrawingStyle;
		this.shape = shape;
		this.color = Color.BLACK;
		this.fillColor = null;

		this.bSelected = false;
		this.eSelectedAnchor = null;

		this.x1 = 0;
		this.y1 = 0;
		this.x2 = 0;
		this.y2 = 0;
		this.ox2 = 0;
		this.oy2 = 0;
	}

	// 추상 메소드
	public abstract GShape clone();

	public abstract void drag(Graphics graphics);
	// setter getter

	public Cursor getCursor() {
		return this.eSelectedAnchor != null ? this.eSelectedAnchor.getCursor() : Cursor.getDefaultCursor();
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return this.color;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public Color getFillColor() {
		return this.fillColor;
	}
	public void setLineWidth(int width) {
	    this.lineWidth = width;
	}

	public int getLineWidth() {
	    return this.lineWidth;
	}
	// 선택 여부 관련 메소드
	public void clearSelected() {
		this.anchors = null;
	}

	public void setSelected(Graphics graphics) {
		this.createAnchors();
		this.drawAnchors(graphics);
	}

	public EAnchors getSelectedAnchor() {
		return this.eSelectedAnchor;
	}

	public EDrawingStyle getEDrawingStyle() {
		return this.eDrawingStyle;
	}

	// 앵커 생성 및 그리기
	private void createAnchors() {
		Rectangle rectangle = this.shape.getBounds();
		int x = rectangle.x;
		int y = rectangle.y;
		int w = rectangle.width;
		int h = rectangle.height;
		int ANCHOR_WIDTH = 10;
		int ANCHOR_HEIGHT = 10;

		this.anchors = new Ellipse2D.Float[EAnchors.values().length - 1];
		this.anchors[EAnchors.eRR.ordinal()] = new Ellipse2D.Float(x + w / 2 - ANCHOR_WIDTH / 2, y - ANCHOR_HEIGHT * 2,
				ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eNN.ordinal()] = new Ellipse2D.Float(x + w / 2 - ANCHOR_WIDTH / 2, y - ANCHOR_HEIGHT / 2,
				ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eSS.ordinal()] = new Ellipse2D.Float(x + w / 2 - ANCHOR_WIDTH / 2,
				y + h - ANCHOR_HEIGHT / 2, ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eEE.ordinal()] = new Ellipse2D.Float(x + w - ANCHOR_WIDTH / 2,
				y + h / 2 - ANCHOR_HEIGHT / 2, ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eWW.ordinal()] = new Ellipse2D.Float(x - ANCHOR_WIDTH / 2, y + h / 2 - ANCHOR_HEIGHT / 2,
				ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eNW.ordinal()] = new Ellipse2D.Float(x - ANCHOR_WIDTH / 2, y - ANCHOR_HEIGHT / 2,
				ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eNE.ordinal()] = new Ellipse2D.Float(x + w - ANCHOR_WIDTH / 2, y - ANCHOR_HEIGHT / 2,
				ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eSW.ordinal()] = new Ellipse2D.Float(x - ANCHOR_WIDTH / 2, y + h - ANCHOR_HEIGHT / 2,
				ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eSE.ordinal()] = new Ellipse2D.Float(x + w - ANCHOR_WIDTH / 2, y + h - ANCHOR_HEIGHT / 2,
				ANCHOR_WIDTH, ANCHOR_HEIGHT);
	}

	private void drawAnchors(Graphics graphics) {
		Graphics2D graphics2D = (Graphics2D) graphics;
		Color originalColor = graphics2D.getColor();
		graphics2D.setColor(Color.BLACK);

		// 앵커 그리기
		for (Ellipse2D anchor : this.anchors) {
			graphics2D.draw(anchor);
			graphics2D.fill(anchor);
		}

		graphics2D.setColor(originalColor);
	}

	public void draw(Graphics graphics) {
	    Graphics2D graphics2D = (Graphics2D) graphics;
	    graphics2D.setStroke(new BasicStroke(lineWidth)); // 테두리 굵기 설정
	    graphics2D.setColor(color);
	    graphics2D.draw(shape);
	    if (fillColor != null) {
	        graphics2D.setColor(fillColor);
	        graphics2D.fill(shape);
	    }
	    if (this.anchors != null) {
	        drawAnchors(graphics);
	    }
	}

	public void setOrigin(int x1, int y1) {
		this.x1 = x1;
		this.y1 = y1; // 원점

		this.ox2 = x1;
		this.oy2 = y1; // 현재 과거의 점

		this.x2 = x1;
		this.y2 = y1; // 새로운 점
	}

	public void movePoint(int x2, int y2) {
		this.ox2 = this.x2;
		this.oy2 = this.y2;
		this.x2 = x2;
		this.y2 = y2;
	}

	public void addPoint(int x2, int y2) {
		this.x2 = x2;
		this.y2 = y2;
	}

	public boolean onShape(int x, int y) {
		this.eSelectedAnchor = null;
		if (this.anchors != null) {
			for (int i = 0; i < EAnchors.values().length - 1; i++) {
				if (anchors[i].contains(x, y)) {
					this.eSelectedAnchor = EAnchors.values()[i];
					return true;
				}
			}
		}
		boolean isOnShape = this.shape.contains(x, y);
		if (isOnShape) {
			this.eSelectedAnchor = EAnchors.eMM;
		}
		return isOnShape;
	}

//Transform
	public void keepTransformation(Graphics graphics, int x, int y) {
		if (getSelectedAnchor() == EAnchors.eMM) {
			keepMove(graphics, x, y);
		} else if (getSelectedAnchor() == EAnchors.eRR) {
			keepRotate(graphics, x, y);
		} else {
			keepResize(graphics, x, y);
		}
	}

	public void finishTransformation(Graphics graphics) {
		if (bSelected) {
			drawAnchors(graphics);
		}
	}

//move
	public void startMove(Graphics graphics, int x, int y) {
		this.ox2 = x;
		this.oy2 = y;

		this.x2 = x;
		this.y2 = y;
	}

	public void keepMove(Graphics graphics, int x, int y) {
		this.ox2 = this.x2;
		this.oy2 = this.y2;
		this.x2 = x;
		this.y2 = y;

		Graphics2D graphics2D = (Graphics2D) graphics;
		graphics2D.setXORMode(graphics2D.getBackground());
		graphics2D.draw(this.shape);

		int dx = x2 - ox2;
		int dy = y2 - oy2;
		AffineTransform affineTransform = new AffineTransform();
		affineTransform.setToTranslation(dx, dy);
		this.shape = affineTransform.createTransformedShape(this.shape);

		graphics2D.draw(shape);
	}

	public void stopMove(Graphics graphics, int x, int y) {
	}

	// resize
	public void startResize(Graphics graphics, int x, int y) {
		this.ox2 = x;
		this.oy2 = y;

		this.x2 = x;
		this.y2 = y;
	}

	private Point2D getResizeFactor() {
		sx = 1.0;
		sy = 1.0;
		dx = 0.0;
		dy = 0.0;
		double cx = this.shape.getBounds().getX();
		double cy = this.shape.getBounds().getY();
		double w = this.shape.getBounds().getWidth();
		double h = this.shape.getBounds().getHeight();

		switch (this.eSelectedAnchor) {
		case eEE:
			sx = (w + x2 - ox2) / w;
			cx = this.shape.getBounds().getX();
			dx = cx - cx * sx;
			break;
		case eWW:
			sx = (w + ox2 - x2) / w;
			cx = this.shape.getBounds().getX() + w;
			dx = cx * (1 - sx);
			break;
		case eSS:
			sy = (h + y2 - oy2) / h;
			cy = this.shape.getBounds().getY();
			dy = cy - cy * sy;
			break;
		case eNN:
			sy = (h + oy2 - y2) / h;
			cy = this.shape.getBounds().getY() + h;
			dy = cy * (1 - sy);
			break;
		case eSE:
			sx = (w + x2 - ox2) / w;
			sy = (h + y2 - oy2) / h;
			cx = this.shape.getBounds().getX();
			cy = this.shape.getBounds().getY();
			dx = cx - cx * sx;
			dy = cy - cy * sy;
			break;
		case eSW:
			sx = (w + ox2 - x2) / w;
			sy = (h + y2 - oy2) / h;
			cx = this.shape.getBounds().getX() + w;
			cy = this.shape.getBounds().getY();
			dx = cx * (1 - sx);
			dy = cy - cy * sy;
			break;
		case eNE:
			sx = (w + x2 - ox2) / w;
			sy = (h + oy2 - y2) / h;
			cx = this.shape.getBounds().getX();
			cy = this.shape.getBounds().getY() + h;
			dx = cx - cx * sx;
			dy = cy * (1-  sy);
			break;
		case eNW:
			sx = (w + ox2 - x2) / w;
			sy = (h + oy2 - y2) / h;
			cx = this.shape.getBounds().getX() + w;
			cy = this.shape.getBounds().getY() + h;
			dx = cx * (1 - sx);
			dy = cy * (1 - sy);
			break;
		default:
			break;
		}
		return new Point2D.Double(sx, sy);
	}

	public void keepResize(Graphics graphics, int x, int y) {
		this.ox2 = this.x2;
		this.oy2 = this.y2;
		this.x2 = x;
		this.y2 = y;

		Graphics2D graphics2D = (Graphics2D) graphics;
		graphics2D.setXORMode(graphics2D.getBackground());
		graphics2D.draw(this.shape);

		Point2D resizeFactor = getResizeFactor();

		double scaleX = resizeFactor.getX();
		double scaleY = resizeFactor.getY();

		// 현재 도형의 크기
		double currentWidth = this.shape.getBounds().getWidth();
		double currentHeight = this.shape.getBounds().getHeight();

		// 최소 크기
		double minWidth = 10; // 최소 폭
		double minHeight = 10; // 최소 높이

		// 새로운 크기
		double newWidth = currentWidth * scaleX;
		double newHeight = currentHeight * scaleY;

		// 최소 크기보다 작아지지 않도록 제어
		if (newWidth < minWidth) {
			scaleX = minWidth / currentWidth;
		}
		if (newHeight < minHeight) {
			scaleY = minHeight / currentHeight;
		}

		AffineTransform affineTransform = new AffineTransform();
		affineTransform.translate(dx, dy);
		affineTransform.scale(scaleX, scaleY);
		this.shape = affineTransform.createTransformedShape(this.shape);

		graphics2D.draw(this.shape);
	}

	public void stopResize(Graphics graphics, int x, int y) {
	}

	// rotation
	public void startRotate(Graphics graphics, int x, int y) {
		Rectangle bounds = this.shape.getBounds();
		double centerX = bounds.getCenterX();
		double centerY = bounds.getCenterY();
		this.startAngle = Math.atan2(y - centerY, x - centerX);

		// 앵커의 초기 위치 저장
		this.saveAnchorPositions();
	}

	private void saveAnchorPositions() {
		if (this.anchors != null) {
			for (int i = 0; i < this.anchors.length; i++) {
				double anchorX = this.anchors[i].getBounds2D().getCenterX();
				double anchorY = this.anchors[i].getBounds2D().getCenterY();
				this.anchors[i].setFrame(anchorX, anchorY, 0, 0);
			}
		}
	}

	public void keepRotate(Graphics graphics, int x, int y) {
		Rectangle bounds = this.shape.getBounds();
		double centerX = bounds.getCenterX();
		double centerY = bounds.getCenterY();
		double currentAngle = Math.atan2(y - centerY, x - centerX);
		this.rotationAngle = currentAngle - startAngle;

		Graphics2D graphics2D = (Graphics2D) graphics;
		graphics2D.draw(this.shape);

		// 회전 변환 적용
		AffineTransform affineTransform = new AffineTransform();
		affineTransform.rotate(rotationAngle, centerX, centerY);
		this.shape = affineTransform.createTransformedShape(this.shape);

		// 앵커도 회전
		if (this.anchors != null) {
			for (int i = 0; i < this.anchors.length; i++) {
				double anchorX = this.anchors[i].getBounds2D().getCenterX();
				double anchorY = this.anchors[i].getBounds2D().getCenterY();
				Point2D rotatedAnchor = affineTransform.transform(new Point2D.Double(anchorX, anchorY), null);
				this.anchors[i].setFrame(rotatedAnchor.getX() - Constants.GDrawingPanel.ANCHOR_WIDTH / 2,
						rotatedAnchor.getY() - Constants.GDrawingPanel.ANCHOR_HEIGHT / 2,
						Constants.GDrawingPanel.ANCHOR_WIDTH, Constants.GDrawingPanel.ANCHOR_HEIGHT);
			}
		}
		graphics2D.draw(shape);
		this.startAngle = currentAngle;
	}

	public void stopRotate(Graphics graphics, int x, int y) {

	}

}