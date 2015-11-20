package ztppro.view;

import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import java.awt.List;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Bresenham's algorithm
 * https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
 *
 * @author Damian Terlecki
 */
public class LineIterator implements Iterator<Point2D> {

    private final Line2D line;
    private final double ax;
    private final double ay;
    private final double dx;
    private final double dy;
    private double x;
    private double y;
    private double error;

    public LineIterator(Line2D line) {
        this.line = line;

        if (line.getX1() < line.getX2()) {
            ax = 1;
        } else {
            ax = -1;
        }
        if (line.getY1() < line.getY2()) {
            ay = 1;
        } else {
            ay = -1;
        }

        dx = Math.abs(line.getX2() - line.getX1());
        dy = Math.abs(line.getY2() - line.getY1());

        error = dx - dy;

        y = line.getY1();
        x = line.getX1();
    }

    @Override
    public boolean hasNext() {
        return Math.abs(x - line.getX2()) >= 1 || (Math.abs(y - line.getY2()) >= 1);
    }

    @Override
    public Point2D next() {
        Point2D approximation = new Point2D.Double(x, y);

        double dError = 2 * error;
        if (dError > -dy) {
            error -= dy;
            x += ax;
        } else if (dError < dx) {
            error += dx;
            y += ay;
        }

        return approximation;
    }

}
