package com.purplefrog.penrose;

import java.awt.geom.*;
import java.util.*;

/**
 * an Acute Robinson Triangle
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 5/20/13
 * Time: 2:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class TriangleA
    extends RobinsonTriangle
{

    public TriangleA()
    {
        this(new Point2D.Double(0,0), new Point2D.Double(Math.cos(d36), Math.sin(d36)), new Point2D.Double(1, 0));
    }

    public TriangleA(Point2D.Double a, Point2D.Double b, Point2D.Double c)
    {
        super(a, b, c);
    }

    @Override
    public void addToShapesAlpha(GeneralPath lines, GeneralPath colorA, GeneralPath colorO)
    {
        hippo(lines, colorA, a, b, c);
    }

    @Override
    public void addToShapesBeta(GeneralPath lines, GeneralPath colorA, GeneralPath colorO)
    {
        hippo(lines, colorA, c, a, b);
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
        double x4 =  interpolate(a.getX(), c.getX(), GR0);
        double y4 =  interpolate(a.getY(), c.getY(), GR0);

        Point2D.Double q = new Point2D.Double(x4,y4);

        RobinsonTriangle t1 = new TriangleA(b, c, q);
        RobinsonTriangle t2 = new TriangleO(q,a,b);

        return Arrays.asList(t1, t2);
    }

    @Override
    public Collection<RobinsonTriangle> decomposeBeta()
    {
        return Arrays.asList((RobinsonTriangle) this);
    }

    @Override
    public RobinsonTriangle composeAlpha(int[] detail)
    {
        double x4 = interpolate(c.getX(), b.getX(), -1-GR0);
        double y4 = interpolate(c.getY(), b.getY(), -1-GR0);
        Point2D.Double q = new Point2D.Double(x4, y4);

        detail[0] +=2;

        return new TriangleA(q, a, b);
    }

    @Override
    public RobinsonTriangle composeBeta(int[] detail)
    {
        double x4 = interpolate(a.getX(), b.getX(), 1/GR0);
        double y4 = interpolate(a.getY(), b.getY(), 1/GR0);
        Point2D.Double q = new Point2D.Double(x4, y4);

        detail[0] ++;

        return new TriangleO(c, q, a);
    }

    public RobinsonTriangle translated(double dx, double dy)
    {
        return new TriangleA(new Point2D.Double(a.x+dx, a.y+dy),
            new Point2D.Double(b.x+dx, b.y+dy),
            new Point2D.Double(c.x+dx, c.y+dy));
    }
}
