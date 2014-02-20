package com.purplefrog.penrose;

import com.purplefrog.knotwork.gui.*;

import java.awt.geom.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 5/20/13
 * Time: 2:43 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class RobinsonTriangle
{
    public static final double d36 = Math.PI /5;
    public static final double GR0 = (Math.sqrt(5)-1)/2;
    Point2D.Double a,b,c;
    private Matrix2 inverse_;

    protected RobinsonTriangle(Point2D.Double a, Point2D.Double b, Point2D.Double c)
    {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public static double interpolate(double a, double b, double t)
    {
        return a + (b - a)* t;
    }

    public Point2D.Double fromTriangleSpace(double u, double v)
    {
        Point2D.Double tmp = forwardMatrix().postMult(u, v);
        return new Point2D.Double(tmp.x+a.x, tmp.y+a.y);
    }

    public Point2D.Double toTriangleSpace(double x, double y)
    {
        Matrix2 inverse = getInverse();
        return inverse.postMult(x-a.x,y- a.y);
    }

    private Matrix2 getInverse()
    {
        if (inverse_==null) {
            inverse_ = forwardMatrix().inverse();
        }
        return inverse_;
    }

    public Matrix2 forwardMatrix()
    {
        return new Matrix2(b.x-a.x, c.x-a.x,
            b.y-a.y, c.y-a.y);
    }

    public static void hippo(GeneralPath lines, GeneralPath filler, Point2D.Double a1, Point2D.Double b1, Point2D.Double c1)
    {
        double[] xs = new double[] { a1.getX(), b1.getX(), c1.getX()};
        double[] ys = new double[] { a1.getY(), b1.getY(), c1.getY()};
        lines.append(new SimplePathIterator(xs, ys, false), false);

        filler.append(new SimplePathIterator(xs, ys, true), false);
    }

    /**
     * darts and kites
     */
    public abstract void addToShapesAlpha(GeneralPath lines, GeneralPath colorA, GeneralPath colorO) ;

    /**
     * rhombus
     */
    public abstract void addToShapesBeta(GeneralPath lines, GeneralPath colorA, GeneralPath colorO);

    /**
     * acute triangle becomes smaller accute + obtuse
     * @return
     */
    public abstract Collection<RobinsonTriangle> decomposeAlpha();

    /**
     * obtuse triangle becomes acute + smaller obtuse
     * @return
     */
    public abstract Collection<RobinsonTriangle> decomposeBeta();

    public abstract RobinsonTriangle composeAlpha(int[] detail);

    public abstract RobinsonTriangle composeBeta(int[] detail);

    public boolean overlaps(FillRectangle.Rect bounds)
    {
        if (a.x<bounds.minX
            && b.x<bounds.minX
            && c.x< bounds.minX)
            return false;
        if (a.x>bounds.maxX
            && b.x>bounds.maxX
            && c.x>bounds.maxX)
            return false;

        if (a.y<bounds.minY
            && b.y<bounds.minY
            && c.y< bounds.minY)
            return false;
        if (a.y>bounds.maxY
            && b.y>bounds.maxY
            && c.y>bounds.maxY)
            return false;

        return true;
    }

    public abstract Point2D gapCenterAlpha();

    public abstract Point2D gapCenterBeta();
}
