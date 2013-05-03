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
        kl.underHalo.add(new SVGLine(haloStyle, x3, y3, x0, y0, x2, y2));
        kl.under.add(new SVGLine(coreStyle, x3, y3, x0, y0, x2, y2));
    }

}
