package com.purplefrog.knotwork;

/**
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 5/3/13
 * Time: 2:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class Style2
    extends Style0
{

    public Style2()
    {
        this(SVGLine.stroke("#cfc", "3px"), SVGLine.stroke("#66f", "4px"));
    }

    public Style2(String coreStyle, String haloStyle)
    {
        super(coreStyle, haloStyle);
    }

    public void bounce(KnotLayers kl, double x0, double y0, double x9, double y9, double x3, double y3)
    {
        double x1 = interp(x0,x9, 0.5523);
        double y1 = interp(y0,y9, 0.5523);
        double x2 = interp(x3,x9, 0.5523);
        double y2 = interp(y3,y9, 0.5523);

        PathData c;
        if (true) {
            double x0b = interp(x0, x9, 0.02);
            double y0b = interp(y0, y9, 0.02);
            double x3b = interp(x3,x9, 0.02);
            double y3b = interp(y3,y9, 0.02);
            c = new PathData.Multi(
                new PathData.Line(x0,y0,x0b,y0b),
                new PathData.Curve(x0b,y0b, x1,y1, x2,y2, x3b,y3b),
                new PathData.Line(x3b,y3b, x3,y3)
            );
        } else {
            // inkscape has what I consider to be bugs
            c = new PathData.Curve(x0,y0, x1,y1, x2,y2, x3,y3);
        }

        kl.underHalo.add(c);
        kl.under.add(c);
    }

    public static double interp(double a, double b, double t)
    {
        return a + (b-a)*t;
    }

    public void point(KnotLayers kl, double x0, double y0, double x9, double y9, double x3, double y3)
    {
        double dx = x3 - x0;
        double dy = y3 - y0;
        double x4 = dx +x9;
        double y4 = dy +y9;


        double x1 = interp(x0,x9, 0.5523);
        double y1 = interp(y0,y9, 0.5523);
        double x2 = interp(x4,x9, 0.5523);
        double y2 = interp(y4,y9, 0.5523);

        PathData c;
        if (false) {
            // inkscape has what I consider to be bugs
            c = new PathData.Curve(x0,y0, x1,y1, x2,y2, x4,y4);
        } else {
            double x0b = interp(x0, x9, 0.02);
            double y0b = interp(y0, y9, 0.02);

            c = new PathData.Multi(
                new PathData.Line(x0,y0,x0b,y0b),
                new PathData.Curve(x0b,y0b, x1,y1, x2,y2, x4,y4)
            );
        }


        kl.underHalo.add(c);
        kl.under.add(c);
    }
}
