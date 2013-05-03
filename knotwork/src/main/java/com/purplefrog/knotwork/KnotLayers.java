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
    public final List<SVGThing> underHalo;
    public final List<SVGThing> under;
    public final List<SVGThing> overHalo;
    public final List<SVGThing> over;

    public KnotLayers(List<SVGThing> underHalo, List<SVGThing> under, List<SVGThing> overHalo, List<SVGThing> over)
    {

        this.underHalo = underHalo;
        this.under = under;
        this.overHalo = overHalo;
        this.over = over;
    }

    public KnotLayers reversed()
    {
        return new KnotLayers(overHalo, over, underHalo, under);
    }

    public List<SVGThing>[] layers()
    {
        return new List[] {
            underHalo, under, overHalo, over
        };
    }
}
