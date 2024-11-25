package shapetools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

public class GParallelogram extends GShape {
    private static final long serialVersionUID = 1L;

    public GParallelogram() {
        super(EDrawingStyle.e2Pstyle, new Path2D.Float());
    }

    @Override
    public GShape clone() {
        return new GParallelogram();
    }

    @Override
    public void drag(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        Path2D parallelogram = (Path2D) this.shape;

        int offset = Math.abs(x2 - x1) / 4;

        parallelogram.reset();
        parallelogram.moveTo(x1 + offset, y1);
        parallelogram.lineTo(x2, y1);
        parallelogram.lineTo(x2 - offset, y2);
        parallelogram.lineTo(x1, y2);
        parallelogram.closePath();

        graphics2D.draw(parallelogram);
    }
}
