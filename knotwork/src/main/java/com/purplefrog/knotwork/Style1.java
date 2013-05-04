package com.purplefrog.knotwork;

/**
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 5/3/13
 * Time: 2:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class Style1
    extends Style0
{

    public Style1()
    {
        this(SVGLine.stroke("#cfc", "3px"), SVGLine.stroke("#66f", "4px"));
    }

    public Style1(String coreStyle, String haloStyle)
    {
        super(coreStyle, haloStyle);
    }

    public  void bounce(KnotLayers kl, double x3, double y3, double x0, double y0, double x2, double y2)
    {
        PathData.Line path = new PathData.Line(x3, y3, x0, y0, x2, y2);
        kl.underHalo.add(path);
        kl.under.add(path);
    }

    public void point(KnotLayers kl, double x1, double y1, double x0, double y0, double x2, double y2)
    {
        double x4 = x2-x1 +x0;
        double y4 = y2-y1 +y0;

        PathData path = new PathData.Line(x1,y1, x0,y0, x4,y4);

        kl.underHalo.add(path);
        kl.under.add(path);
    }
}
