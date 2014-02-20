package com.purplefrog.knotwork.gui;

import java.awt.geom.*;

/**
* Created with IntelliJ IDEA.
* User: thoth
* Date: 5/6/13
* Time: 12:45 PM
* To change this template use File | Settings | File Templates.
*/
public class SimplePathIterator
    implements PathIterator
{
    private final double[] xs;
    private final double[] ys;
    int idx;
    private final boolean closed;

    public SimplePathIterator(double[] xs, double[] ys, boolean closed)
    {
        this.xs = xs;
        this.ys = ys;
        idx = 0;
        this.closed = closed;
    }

    public int getWindingRule()
    {
        return PathIterator.WIND_EVEN_ODD;
    }

    public boolean isDone()
    {
        if (closed) {
            return idx> xs.length;
        } else {
            return idx >=xs.length;
        }
    }

    public void next()
    {
        idx++;
    }

    public int currentSegment(float[] coords)
    {
        if (idx>=xs.length)
            return SEG_CLOSE;

        coords[0] = (float) xs[idx];
        coords[1] = (float) ys[idx];

        return idx>0 ? SEG_LINETO : SEG_MOVETO;
    }

    public int currentSegment(double[] coords)
    {
        if (idx>=xs.length)
            return SEG_CLOSE;

        coords[0] = xs[idx];
        coords[1] = ys[idx];

        return idx>0 ? SEG_LINETO : SEG_MOVETO;
    }
}
