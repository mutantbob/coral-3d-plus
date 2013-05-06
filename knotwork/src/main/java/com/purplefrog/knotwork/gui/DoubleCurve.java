package com.purplefrog.knotwork.gui;

import java.awt.geom.*;

/**
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 5/6/13
 * Time: 3:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class DoubleCurve
    extends AbstractShape
{
    protected Corner c1, c2;
    public DoubleCurve(int dx, int dy)
    {
        c1 = new Corner(dx, dy);
        c2 = new Corner(-dx, -dy);
    }

    public void apply(GeneralPath over, GeneralPath overHalo, GeneralPath under, GeneralPath underHalo, double west, double east, double north, double south, BasicCross.KnotParams knotParams)
    {
        c1.apply(over, overHalo, under, underHalo, west, east, north, south, knotParams);
        c2.apply(over, overHalo, under, underHalo, west, east, north, south, knotParams);
    }
}
