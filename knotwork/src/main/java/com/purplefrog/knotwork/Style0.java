package com.purplefrog.knotwork;

/**
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 5/3/13
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Style0
    implements Style
{
    protected final String coreStyle;
    protected String haloStyle;

    public Style0(String coreStyle, String haloStyle)
    {
        this.haloStyle = haloStyle;
        this.coreStyle = coreStyle;
    }

    public void basicOverUnder(KnotLayers kl, double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4)
    {

        PathData.Line overPath = new PathData.Line(x1, y1, x2, y2);
        kl.overHalo.add(new SVGThing(haloStyle, overPath));
        kl.over.add(new SVGThing(coreStyle, overPath));

        PathData.Line kludeExtension1 = new PathData.Line(x1, y1, interp(x1, x2, -0.1), interp(y1,y2, -0.1));
        kl.underHalo.add(new SVGThing(haloStyle, kludeExtension1));
        kl.under.add(new SVGThing(coreStyle, kludeExtension1));

        kludeExtension1 = new PathData.Line(x2, y2, interp(x1, x2, 1.1), interp(y1,y2, 1.1));
        kl.underHalo.add(new SVGThing(haloStyle, kludeExtension1));
        kl.under.add(new SVGThing(coreStyle, kludeExtension1));

        PathData.Line underPath = new PathData.Line(x3, y3, x4, y4);
        kl.underHalo.add(new SVGThing(haloStyle, underPath));
        kl.under.add(new SVGThing(coreStyle, underPath));
    }

    public static double interp(double a, double b, double t)
    {
        return Style2.interp(a, b, t);
    }

    public void nub(KnotLayers kl, double x3, double y3, double x0, double y0)
    {
        PathData.Line line = new PathData.Line(x3, y3, x0, y0);
        kl.underHalo.add(new SVGThing(haloStyle, line));
        kl.under.add(new SVGThing(coreStyle, line));
    }

}
