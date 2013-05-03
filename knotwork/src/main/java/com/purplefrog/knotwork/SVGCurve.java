package com.purplefrog.knotwork;

/**
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 5/3/13
 * Time: 2:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class SVGCurve
{
    public String style;
    public double x0;
    public double y0;
    public double x1;
    public double y1;
    public double x2;
    public double y2;
    public double x3;
    public double y3;

    public SVGCurve(String style, double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3)
    {
        super();
        this.style = style;
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
    }


    public String asSVG()
    {
        String path = "M "+x0+","+y0+" C "+x1+","+y1+" "+x2+","+y2+" "+x3+","+y3;

             return
                 "<path\n" +
                 "       style=\"" + style + "\"\n" +
                 "       d=\"" + path + "\"\n" +
                 " />" +
                     "\n";
    }
}
