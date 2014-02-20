package com.purplefrog.penrose;

import java.awt.geom.*;

/**
 * [ a b ]
 * <br>[ c d ]
 * <p>Created by thoth on 2/19/14.</p>
 */
public class Matrix2
{
    public final double a;
    public final double b;
    public final double c;
    public final double d;

    public Matrix2(double a, double b, double c, double d)
    {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public Matrix2 inverse()
    {
        double ad_bc = a*d-b*c;

        return new Matrix2(d/ad_bc, -b/ad_bc,
            -c/ad_bc, a/ad_bc);
    }

    public Point2D.Double postMult(double x, double y)
    {
        return new Point2D.Double(a*x + b*y,
            c*x+d*y);
    }

    public static Matrix2 rotation(double radians)
    {
        double c = Math.cos(radians);
        double s = Math.sin(radians);
        return new Matrix2(c, -s,
            s, c);
    }

    public Point2D.Double postMult(Point2D.Double p)
    {
        return postMult(p.x, p.y);
    }
}
