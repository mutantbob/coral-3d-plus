package com.purplefrog.knotwork.gui;

import java.awt.geom.*;

/**
* Created with IntelliJ IDEA.
* User: thoth
* Date: 5/6/13
* Time: 12:34 PM
* To change this template use File | Settings | File Templates.
*/
public class PointArc
    implements NodeShape
{
    /**
     * depending on which half of the point you want to draw and which direction it is pointing, use something from this diagram.
     * <pre> 4 3</pre>
     * <pre>5   2</pre>
     *
     * <pre>6   1</pre>
     * <pre> 7 0 </pre>
     *
     * 0 and 1 point southeast.  2 and 3 point northeast.  4 and 5 point northwest.  6 and 7 point southwest.  y increases as you travel south (standard screen geometry).
     *
     */
    int arrangement = 0;

    public PointArc(int arrangement)
    {
        this.arrangement = arrangement;
    }

    public void apply(GeneralPath over, GeneralPath overHalo, GeneralPath under, GeneralPath underHalo, double west, double east, double north, double south, BasicCross.KnotParams knotParams)
    {
        double margin = 0.125 + S2 * knotParams.haloThickness;
        margin = 0;
        GeneralPath s1 = gorp(knotParams.coreThickness, margin);

        AffineTransform at0 = new AffineTransform( east-west, 0, 0, south - north, west, north);
        s1.transform(at0);
        over.append(s1, false);

        GeneralPath s2 = gorp(knotParams.haloThickness, margin);
        s2.transform(at0);
        overHalo.append(s2, false);
    }

    private GeneralPath gorp(double thick, double margin)
    {
        GeneralPath rval = basicPoly(thick, margin);
        rval.transform(arrangementTransform());
        return rval;
    }

    private AffineTransform arrangementTransform()
    {
        AffineTransform rval;
        if ( (arrangement&1) != 0) {
            rval = new AffineTransform(0, 1, 1, 0, 0,0);
        } else {
            rval = new AffineTransform();
        }

        switch (arrangement/2) {
            case 0:
                break;
            case 1:
                rval.preConcatenate(new AffineTransform(0, -1, 1, 0, 0, 1));
                break;
            case 2:
                rval.preConcatenate(new AffineTransform(-1, 0, 0, -1, 1, 1));
                break;
            case 3:
                rval.preConcatenate(new AffineTransform(0, 1, -1, 0, 1, 0));
                break;
            default:
                throw new IllegalArgumentException("bad arrangement value");
        }
        return rval;
    }

    public final double S2 = Math.sqrt(2);

    public GeneralPath basicPoly(double thick, double margin)
    {
        GeneralPath rval = new GeneralPath();

        double x5 = 1.5- margin;
        double y5 = 0.5- margin;
        {
            double x1 = -thick;
            double y1 = thick;
            double cx1 = x1+0.5*(1-thick);
            double cy1 = y1+0.5*(1-thick);


            double x2 = x5 +S2*thick;
            double y2 = y5 +S2*thick;
            double cx2 = x2-(1+thick);
            double cy2 = y2;

            rval.append(new CubicCurve2D.Double(x1, y1, cx1, cy1, cx2, cy2, x2, y2), false);
        }

//        rval.append(new SimplePathIterator(new double[]{1.5 - thick}, new double[]{1 - thick}), true);

        if (true) {
            double x1 = thick;
            double y1 = -thick;
            double cx1 = x1+0.5*(1-thick);
            double cy1 = y1+0.5*(1-thick);

            double x2 = x5 -S2*thick;
            double y2 = y5-S2*thick;
            double cx2 = x2-(1-thick);
            double cy2 = y2;
//            cx2 = cx1;
//            cy2 = cy1;

            rval.append(new CubicCurve2D.Double(x2, y2, cx2, cy2, cx1, cy1, x1, y1), true);
        }

        rval.closePath();

        return rval;
    }

    public boolean connectsNE()
    {
        return arrangement<1 || arrangement>4;
    }

    public boolean connectsNW()
    {
        return arrangement<3 || arrangement>6;
    }

    public boolean connectsSE()
    {
        return arrangement>2 && arrangement < 7;
    }

    public boolean connectsSW()
    {
        return arrangement>0 && arrangement < 5;
    }
}
