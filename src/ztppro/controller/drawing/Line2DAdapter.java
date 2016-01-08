/* 
 * Copyright 2016 Damian Terlecki.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ztppro.controller.drawing;

import java.awt.geom.*;
import java.util.Iterator;

public class Line2DAdapter extends Line2D.Double implements Iterable<Point2D> {

    public Line2DAdapter() {
    }

    public Line2DAdapter(double x1, double y1, double x2, double y2) {
        super(x1, y1, x2, y2);
    }

    public Line2DAdapter(Point2D p1, Point2D p2) {
        super(p1, p2);
    }

    @Override
    public Iterator<Point2D> iterator() {
        return new LineIterator();
    }

    /**
     * Bresenham's algorithm
     * https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
     *
     */
    private class LineIterator implements Iterator<Point2D> {

        private final double ax;
        private final double ay;
        private final double dx;
        private final double dy;
        private double x;
        private double y;
        private double error;

        public LineIterator() {
            if (getX1() < getX2()) {
                ax = 1;
            } else {
                ax = -1;
            }
            if (getY1() < getY2()) {
                ay = 1;
            } else {
                ay = -1;
            }

            dx = Math.abs(getX2() - getX1());
            dy = Math.abs(getY2() - getY1());

            error = dx - dy;

            y = getY1();
            x = getX1();
        }

        @Override
        public boolean hasNext() {
            return Math.abs(x - getX2()) >= 1 || (Math.abs(y - getY2()) >= 1);
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
}
