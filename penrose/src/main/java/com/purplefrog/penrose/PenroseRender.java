package com.purplefrog.penrose;

import java.awt.*;
import java.awt.image.*;

/**
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 5/20/13
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PenroseRender
{
    void paintPenrose(Graphics2D g2, Dimension sz, ImageObserver obs);
}
