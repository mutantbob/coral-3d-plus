package com.purplefrog.penrose;

import junit.framework.*;

import java.awt.geom.*;

/**
 * Created by thoth on 2/19/14.
 */
public class Test1
    extends TestCase
{
    public void testTriangleCoord()
    {
        TriangleA ta = new TriangleA();
        TriangleO to = new TriangleO();
        checkVertices(ta);
        checkVertices(to);

        checkVertices(ta.translated(10,0));
        checkVertices(ta.translated(0,10));
        checkVertices(to.translated(10,0));
        checkVertices(to.translated(0,10));
    }

    public void checkVertices(RobinsonTriangle ta)
    {
        checkCoord(ta, ta.b, 1, 0, "for b: ");
        checkCoord(ta, ta.a, 0, 0, "for a: ");
        checkCoord(ta, ta.c, 0, 1, "for c: ");
    }

    public void checkCoord(RobinsonTriangle ta, Point2D.Double worldCoord, double expectedX, double expectedY, String errPrefix)
    {
        Point2D.Double xy1 = ta.toTriangleSpace(worldCoord.x, worldCoord.y);
        double epsilon = 0.00001;
        assertEquals(errPrefix +"x is wrong", expectedX, xy1.x, epsilon);
        assertEquals(errPrefix +"y is wrong", expectedY, xy1.y, epsilon);
    }
}
