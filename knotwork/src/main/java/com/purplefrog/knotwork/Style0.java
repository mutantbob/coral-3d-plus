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
        kl.overHalo.add(new SVGLine(haloStyle, x1, y1, x2,y2));
        kl.over.add(new SVGLine(coreStyle, x1, y1, x2,y2));
        kl.underHalo.add(new SVGLine(haloStyle, x3,y3, x4,y4));
        kl.under.add(new SVGLine(coreStyle, x3,y3, x4,y4));
    }

    public void nub(KnotLayers kl, double x3, double y3, double x0, double y0)
    {
        kl.underHalo.add(new SVGLine(haloStyle, x3,y3, x0,y0));
        kl.under.add(new SVGLine(coreStyle, x3,y3, x0,y0));
    }

}
