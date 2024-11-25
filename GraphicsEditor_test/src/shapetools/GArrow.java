package shapetools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class GArrow extends GShape {
    private static final long serialVersionUID = 1L;

    public GArrow() {
        super(EDrawingStyle.e2Pstyle, new Polygon());
    }

    public GShape clone() {
        return new GArrow();
    }

    @Override
    public void drag(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;

        Polygon polygon = (Polygon) this.shape;
        polygon.reset();
        int arrowWidth = Math.abs(x2 - x1) / 4;
        int arrowHeight = Math.abs(y2 - y1) / 2;
        
        polygon.addPoint(x1, y1 + arrowHeight);
        polygon.addPoint(x2 - arrowWidth, y1 + arrowHeight);
        polygon.addPoint(x2 - arrowWidth, y1);
        polygon.addPoint(x2, (y1 + y2) / 2);
        polygon.addPoint(x2 - arrowWidth, y2);
        polygon.addPoint(x2 - arrowWidth, y2 - arrowHeight);
        polygon.addPoint(x1, y2 - arrowHeight);
        graphics2D.draw(polygon);
    }
}
