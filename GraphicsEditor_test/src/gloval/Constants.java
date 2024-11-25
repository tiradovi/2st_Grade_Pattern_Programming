package gloval;

import java.io.File;

import shapetools.GLine;
import shapetools.GOval;
import shapetools.GPolygon;
import shapetools.GRectangle;
import shapetools.GShape;
import shapetools.GTriangle;
import shapetools.GArrow;
import shapetools.GTrapezoid;
import shapetools.GRhombus;
import shapetools.GParallelogram;

public class Constants {

    public enum EShapeButtons {
        eRectangle("rectangle", new GRectangle(), "rectangle_icon"),
        eOval("oval", new GOval(), "oval_icon"),
        eLine("line", new GLine(), "line_icon"),
        ePolygon("polygon", new GPolygon(), "polygon_icon"),
        eTriangle("triangle", new GTriangle(), "triangle_icon"),
        eArrow("arrow", new GArrow(), "arrow_icon"),
        eTrapezoid("trapezoid", new GTrapezoid(), "trapezoid_icon"),
        eRhombus("rhombus", new GRhombus(), "rhombus_icon"),
        eParallelogram("parallelogram", new GParallelogram(), "parallelogram_icon");



        private String text;
        private GShape shapeTool;
        private String imageFileName;

        EShapeButtons(String text, GShape shapeTool, String imageFileName) {
            this.text = text;
            this.shapeTool = shapeTool;
            this.imageFileName = imageFileName;
        }

        public String getText() {
            return this.text;
        }

        public GShape getShapeTool() {
            return this.shapeTool;
        }

        public String getImageFileName() {
            return this.imageFileName;
        }
    }

    public static final String DEFAULT_FILE_PATH = System.getProperty("user.home") + File.separator + "Downloads"
            + File.separator + "output.ser";
    public static final int NUM_POINT = 20;


    public static class GMainFrame {
        public static final int WIDTH = 1000;
        public static final int HEIGHT = 800;
    }
    public static class GDrawingPanel {
    	  public static final int ANCHOR_WIDTH = 10;
    	  public static final int ANCHOR_HEIGHT = 10;
    }
    public static class GMenubar {
        public enum EMenu {
            eFile("파일"), eEdit("편집"), eDesign("디자인");

            private String text;

            private EMenu(String text) {
                this.text = text;
            }

            public String getText() {
                return this.text;
            }
        }
    }
}
