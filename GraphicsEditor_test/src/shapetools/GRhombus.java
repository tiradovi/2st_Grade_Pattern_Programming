package shapetools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

public class GRhombus extends GShape {
    private static final long serialVersionUID = 1L;

    public GRhombus() {
        super(EDrawingStyle.e2Pstyle, new Path2D.Float());
    }

    @Override
    public GShape clone() {
        return new GRhombus();
    }

    @Override
    public void drag(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        Path2D rhombus = (Path2D) this.shape;

        int centerX = (x1 + x2) / 2;
        int centerY = (y1 + y2) / 2;
        int halfWidth = Math.abs(x2 - x1) / 2;
        int halfHeight = Math.abs(y2 - y1) / 2;

        rhombus.reset();
        rhombus.moveTo(centerX, y1);
        rhombus.lineTo(x2, centerY);
        rhombus.lineTo(centerX, y2);
        rhombus.lineTo(x1, centerY);
        rhombus.closePath();

        graphics2D.draw(rhombus);
    }
}
