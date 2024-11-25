package shapetools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class GTrapezoid extends GShape {
    private static final long serialVersionUID = 1L;

    public GTrapezoid() {
        super(EDrawingStyle.e2Pstyle, new Polygon());
    }

    public GShape clone() {
        return new GTrapezoid();
    }

    @Override
    public void drag(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;

        Polygon polygon = (Polygon) this.shape;
        polygon.reset();
        int topWidth = Math.abs(x2 - x1) / 2;
        
        polygon.addPoint(x1 + topWidth / 2, y1);
        polygon.addPoint(x2 - topWidth / 2, y1);
        polygon.addPoint(x2, y2);
        polygon.addPoint(x1, y2);
        graphics2D.draw(polygon);
    }
}
