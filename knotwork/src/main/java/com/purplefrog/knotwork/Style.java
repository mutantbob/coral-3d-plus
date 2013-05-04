package com.purplefrog.knotwork;

/**
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 5/3/13
 * Time: 2:21 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Style
{
    void nub(KnotLayers kl, double x3, double y3, double x0, double y0);

    void bounce(KnotLayers kl, double x3, double y3, double x0, double y0, double x2, double y2);

    void basicOverUnder(KnotLayers kl, double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4);

    void point(KnotLayers kl, double x1, double y1, double x0, double y0, double x2, double y2);
}
