package com.purplefrog.knotwork.gui;

import java.awt.geom.*;

/**
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 5/6/13
 * Time: 2:03 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractShape
    implements NodeShape
{
    public static AffineTransform mapToBox(double x8, double y8, double x9, double y9)
    {
        return new AffineTransform(x9 - x8, 0, 0, y9 - y8, x8, y8);
    }
}
