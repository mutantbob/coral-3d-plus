package com.purplefrog.knotwork.gui;

import java.awt.*;
import java.awt.geom.*;

/**
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 5/6/13
 * Time: 11:22 AM
 * To change this template use File | Settings | File Templates.
 */
public interface NodeShape
{
    void apply(GeneralPath over, GeneralPath overHalo, GeneralPath under, GeneralPath underHalo, double west, double east, double north, double south, BasicCross.KnotParams knotParams);


    boolean connectsNE();
    boolean connectsNW();
    boolean connectsSE();
    boolean connectsSW();
}
