package com.purplefrog.knotwork;

/**
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 5/3/13
 * Time: 12:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class SVGLine
    extends SVGThing
{
    public double[] coordPairs;
    public String style;

    public SVGLine(String style, double... coordPairs)
    {
        super();
        if ((coordPairs.length&1) != 0)
            throw new IllegalArgumentException("coordinates must be specified in pairs; ["+coordPairs.length+"]");
        this.coordPairs = coordPairs;
        this.style = style;
    }

    public static String stroke(String color, String strokeWidth)
    {
        return "fill:none;" +
            "stroke:" + color + ";" +
            "stroke-width:" + strokeWidth + ";" +
            "stroke-linecap:butt;" +
            "stroke-linejoin:miter;" +
            "stroke-opacity:1";
    }

    @Override
    public String asSVG()
    {
        StringBuilder path = new StringBuilder();
        for (int i = 0; i < coordPairs.length; i+=2) {
            path.append(
                (i==0?"M ":"L ")+
                    coordPairs[i]+","+coordPairs[i+1]+" "
            );
        }

        return //"<g>" +
            "<path\n" +
            "       style=\"" + style + "\"\n" +
            "       d=\"" + path + "\"\n" +
            //"       id=\"path2987\"\n" +
            " />" +
//                "</g>" +
                "\n";
    }
}
