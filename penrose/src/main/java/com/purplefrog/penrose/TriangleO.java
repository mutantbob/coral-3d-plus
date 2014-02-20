package com.purplefrog.penrose;

import java.awt.geom.*;
import java.util.*;

/**
 * Oblique Robinson Triangle.
 *
 * <p>This triangle is the alternate to {@link TriangleA},
 * and the relative sizes of each are such that a "beta" decomposition is appropriate.</p>
 *
 * <p>Created with IntelliJ IDEA.
 * User: thoth
 * Date: 5/20/13
 * Time: 3:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class TriangleO
    extends RobinsonTriangle
{
    public TriangleO(Point2D.Double a, Point2D.Double b, Point2D.Double c)
    {
        super(a, b, c);
    }

    public TriangleO()
    {
        this(new Point2D.Double(0,0), new Point2D.Double(Math.cos(TriangleA.d36*3), Math.sin(TriangleA.d36*3)), new Point2D.Double(1,0));
    }

    @Override
    public void addToShapesAlpha(GeneralPath lines, GeneralPath colorA, GeneralPath colorO)
    {
        hippo(lines, colorO, a, b, c);

    }

    @Override
    public void addToShapesBeta(GeneralPath lines, GeneralPath colorA, GeneralPath colorO)
    {
        hippo(lines, colorO, c, a, b);

    }

    public Point2D gapCenterAlpha()
    {
        return new Point2D.Double(
            interpolate(a.getX(), c.getX(), 0.5),
            interpolate(a.getY(), c.getY(), 0.5)
        );
    }

    public Point2D gapCenterBeta()
    {
        return new Point2D.Double(
            interpolate(b.getX(), c.getX(), 0.5),
            interpolate(b.getY(), c.getY(), 0.5)
        );
    }

    @Override
    public Collection<RobinsonTriangle> decomposeAlpha()
    {
        return Arrays.asList((RobinsonTriangle) this);
    }

    @Override
    public Collection<RobinsonTriangle> decomposeBeta()
    {
        double x4 = interpolate(c.getX(), b.getX(), GR0);
        double y4 = interpolate(c.getY(), b.getY(), GR0);

        Point2D.Double q = new Point2D.Double(x4,y4);

        RobinsonTriangle t1 = new TriangleA(c, q, a);
        RobinsonTriangle t2 = new TriangleO(q, a, b);

        return Arrays.asList(t1, t2);
    }


    @Override
    public RobinsonTriangle composeAlpha(int[] detail)
    {
        double x4 = interpolate(b.getX(), a.getX(), 1/GR0);
        double y4 = interpolate(b.getY(), a.getY(), 1/GR0);

        Point2D.Double q = new Point2D.Double(x4,y4);

        detail[0] ++;

        return new TriangleA(b, c, q);
    }

    @Override
    public RobinsonTriangle composeBeta(int[] detail)
    {

        double x4 = interpolate(a.getX(), c.getX(), -1 - GR0);
        double y4 = interpolate(a.getY(), c.getY(), -1 - GR0);

        Point2D.Double q = new Point2D.Double(x4,y4);

        detail[0]+=2;

        return new TriangleO(b, c, q);
    }

    public RobinsonTriangle translated(double dx, double dy)
    {
        return new TriangleO(new Point2D.Double(a.x+dx, a.y+dy),
            new Point2D.Double(b.x+dx, b.y+dy),
            new Point2D.Double(c.x+dx, c.y+dy));
    }
}
