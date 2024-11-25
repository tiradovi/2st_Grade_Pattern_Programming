package shapetools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RectangularShape;

public class GOval extends GShape {
    private static final long serialVersionUID = 1L;

    public GOval() {
        super(EDrawingStyle.e2Pstyle, new Ellipse2D.Float());
    }

    public GShape clone() {
        return new GOval();
    }

    @Override
    public void drag(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;

        RectangularShape shape = (RectangularShape) this.shape;
        shape.setFrame(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
        graphics2D.draw(shape);
    }
}
