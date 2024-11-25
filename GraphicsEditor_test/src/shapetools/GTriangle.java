package shapetools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class GTriangle extends GShape {
    private static final long serialVersionUID = 1L;

    public GTriangle() {
        super(EDrawingStyle.e2Pstyle, new Polygon());
    }

    public GShape clone() {
        return new GTriangle();
    }

    @Override
    public void drag(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;

        Polygon polygon = (Polygon) this.shape;
        polygon.reset();
        polygon.addPoint(x1, y2);
        polygon.addPoint((x1 + x2) / 2, y1);
        polygon.addPoint(x2, y2);
        graphics2D.draw(polygon);
    }
}
