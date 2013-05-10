package com.purplefrog.knotwork.gui;

import java.awt.geom.*;

/**
 *
 * west edge = 1,0
 * east edge = -1,0
 * north edge = 0,1
 * south edge = 0,-1
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 5/6/13
 * Time: 2:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class Corner
    extends AbstractShape
{
    public final int dx;
    public final int dy;

    public Corner(int dx, int dy)
    {
        this.dx = dx;
        this.dy = dy;
    }

    public void apply(GeneralPath over, GeneralPath overHalo, GeneralPath under, GeneralPath underHalo, double west, double east, double north, double south, BasicCross.KnotParams knotParams)
    {
        AffineTransform at = mapToBox(west, north, east, south);

        GeneralPath c1 = makeCurve(knotParams.coreThickness);
        c1.transform(at);
        over.append(c1, false);

        GeneralPath c2 = makeCurve(knotParams.haloThickness);
        c2.transform(at);
        overHalo.append(c2, false);
    }

    public GeneralPath makeCurve(double thick)
    {
        GeneralPath curve = new GeneralPath();
        {
            double x1 = thick;
            double y1 = -thick;
            double x2 = thick;
            double y2 = 1+thick;
            double x3 = x1+0.5523*(1+thick)/2;
            double y3 = y1+0.5523*(1+thick)/2;
            double x4 = x3;
            double y4 = y2-0.5523*(1+thick)/2;

            curve.append(new CubicCurve2D.Double(x1, y1, x3, y3, x4,y4, x2, y2), false);
        }
        {
            double x1 = -thick;
            double y1 = thick;
            double x2 = -thick;
            double y2 = 1-thick;
            double x3 = x1+0.5523*(1-thick)/2;
            double y3 = y1+0.5523*(1-thick)/2;
            double x4 = x3;
            double y4 = y2-0.5523*(1-thick)/2;

            curve.append(new CubicCurve2D.Double(x2, y2, x4, y4, x3,y3, x1, y1), true);
        }

        curve.transform(new AffineTransform(dx, dy, -dy, dx, (1-dx + dy)/2, (1-dx-dy)/2));

        return curve;
    }

    public boolean connectsNE()
    {
        return dy>0 || dx<0;
    }

    public boolean connectsNW()
    {
        return dy>0 || dx>0;
    }

    public boolean connectsSE()
    {
        return dy<0 || dx<0;
    }

    public boolean connectsSW()
    {
        return dy<0 || dx>0;
    }
}
