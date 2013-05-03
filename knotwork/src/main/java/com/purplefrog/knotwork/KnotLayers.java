package com.purplefrog.knotwork;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 5/3/13
 * Time: 1:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class KnotLayers
{
    public final List<PathData> underHalo;
    public final List<PathData> under;
    public final List<PathData> overHalo;
    public final List<PathData> over;

    public KnotLayers(List<PathData> underHalo, List<PathData> under, List<PathData> overHalo, List<PathData> over)
    {
        this.underHalo = underHalo;
        this.under = under;
        this.overHalo = overHalo;
        this.over = over;
    }

    public KnotLayers()
    {
        this(new ArrayList<PathData>(), new ArrayList<PathData>(),new ArrayList<PathData>(),new ArrayList<PathData>());
    }

    public KnotLayers reversed()
    {
        return new KnotLayers(overHalo, over, underHalo, under);
    }

    public List<SVGThing>[] layers(String coreStyle, String haloStyle)
    {

        return new List[] {
            convert(haloStyle, underHalo),
            convert(coreStyle, under),
            convert(haloStyle, overHalo),
            convert(coreStyle, over)
        };
    }

    private List<SVGThing> convert(String style, List<PathData> paths)
    {
        List<SVGThing> rval = new ArrayList<SVGThing>();
        for (PathData path : paths) {
            rval.add(new SVGThing(style, path));
        }
        return rval;
    }
}
