package com.purplefrog.knotwork.gui;

/**
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 5/6/13
 * Time: 11:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class Util
{
    public static double interp(double a, double b, double t)
    {
        return a + (b - a)*t;
    }
}
