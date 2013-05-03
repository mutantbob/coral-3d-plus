package com.purplefrog.knotwork;

/**
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 5/3/13
 * Time: 12:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class SVGThing
{
    public String style;
    public PathData path;

    public SVGThing(String style, PathData path)
    {
        this.style = style;
        this.path = path;
    }

    public String asSVG()
    {
        return
            "<path\n" +
            "       style=\"" + style + "\"\n" +
            "       d=\"" + path.forSVG() + "\"\n" +
            " />" +
                "\n";
    }
}
