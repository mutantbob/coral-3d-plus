package com.purplefrog.knotwork.gui;

import java.awt.*;
import java.awt.geom.*;

/**
* Created with IntelliJ IDEA.
* User: thoth
* Date: 5/6/13
* Time: 12:34 PM
* To change this template use File | Settings | File Templates.
*/
public class BasicCross
    extends AbstractShape
{
    public final boolean polarity;

    public BasicCross(boolean polarity)
    {
        this.polarity = polarity;
    }

    public boolean connectsNE()
    {
        return true;
    }

    public boolean connectsNW()
    {
        return true;
    }

    public boolean connectsSE()
    {
        return true;
    }

    public boolean connectsSW()
    {
        return true;
    }

    public void apply(GeneralPath over, GeneralPath overHalo, GeneralPath under, GeneralPath underHalo, double west, double east, double north, double south, KnotParams knotParams)
    {

        (polarity ? over : under).append(lineBox2(west, north, east, south, knotParams.coreThickness), false);
        (polarity?overHalo:underHalo) .append(lineBox2(west, north, east, south, knotParams.haloThickness), false);

        (!polarity ? over : under).append(lineBox2(west, south, east, north, knotParams.coreThickness), false);
        (!polarity?overHalo:underHalo) .append(lineBox2(west, south, east, north, knotParams.haloThickness), false);
    }

    public static Shape lineBox2(double x8, double y8, double x9, double y9, double thick)
    {
        GeneralPath rval = basicPolySW(thick);

        rval.transform(mapToBox(x8, y8, x9, y9));

        return rval;
    }

    public static GeneralPath basicPolySW(double thick)
    {
        double x1 = thick;
        double y1 = -thick;
        double x2 = -thick;
        double y2 = thick;

        double x3 = 1-thick;
        double y3 = 1+thick;
        double x4 = 1+thick;
        double y4 = 1-thick;

        double[] xs = { x1,  x2,  x3,  x4};
        double[] ys = { y1,  y2,  y3,  y4};

        GeneralPath rval = new GeneralPath();
        rval.append(new SimplePathIterator(xs, ys), false);
        return rval;
    }

    public static GeneralPath basicPolyNW(double thick)
    {
        double x1 = thick;
        double y1 = 1+thick;
        double x2 = -thick;
        double y2 = 1-thick;

        double x3 = 1-thick;
        double y3 = -thick;
        double x4 = 1+thick;
        double y4 = thick;

        double[] xs = { x1,  x2,  x3,  x4};
        double[] ys = { y1,  y2,  y3,  y4};

        GeneralPath rval = new GeneralPath();
        rval.append(new SimplePathIterator(xs, ys), false);
        return rval;
    }

    public static Shape lineBox(double x8, double y8, double x9, double y9, double thick)
    {
        double x1 = Util.interp(x8, x9, thick);
        double y1 = Util.interp(y8, y9, -thick);
        double x2 = Util.interp(x8, x9, -thick);
        double y2 = Util.interp(y8, y9, thick);

        double x3 = Util.interp(x9, x8, thick);
        double y3 = Util.interp(y9, y8, -thick);
        double x4 = Util.interp(x9, x8, -thick);
        double y4 = Util.interp(y9, y8, thick);

        int[] xs = {(int) x1, (int) x2, (int) x3, (int) x4};
        int[] ys = {(int) y1, (int) y2, (int) y3, (int) y4};

        return new Polygon(xs, ys, 4);
    }



    public static class KnotParams
    {
        public double coreThickness = 3.0 / 16;
        public double haloThickness = 0.25;

        public KnotParams(double coreThickness, double haloThickness)
        {

            this.coreThickness = coreThickness;
            this.haloThickness = haloThickness;
        }
    }
}
