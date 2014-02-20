package com.purplefrog.penrose;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 5/20/13
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class PenroseRenderVector
    implements PenroseRender
{

    protected float stroke;

    protected GeneralPath lines = new GeneralPath();
    protected GeneralPath colorA = new GeneralPath();
    protected GeneralPath colorO = new GeneralPath();

    public PenroseRenderVector(int levels, List<RobinsonTriangle> triangles)
    {
        stroke = strokeWidthFor(levels, 0.5f);

        for (RobinsonTriangle tri : triangles) {
            if (0 == (levels & 1))
                tri.addToShapesAlpha(lines, colorA, colorO);
            else
                tri.addToShapesBeta(lines, colorA, colorO);
        }
    }

    public static float strokeWidthFor(int levels, float atL0)
    {
        for (int i=1; i<levels; i++) {
            atL0 *= RobinsonTriangle.GR0;
        }
        return atL0;
    }

    public static List<RobinsonTriangle> decompose(int levels, RobinsonTriangle... triangles_)
    {

        List<RobinsonTriangle> triangles = Arrays.asList(triangles_);
        return decompose(levels, triangles);
    }

    private static List<RobinsonTriangle> decompose(int levels, List<RobinsonTriangle> triangles)
    {
        return decompose(levels, triangles, null);
    }

    public static List<RobinsonTriangle> decompose(int levels, List<RobinsonTriangle> triangles, FillRectangle.Rect bounds)
    {
        for (int i=0; i<levels; i++) {

            triangles = decompose1(triangles, i);

            if (bounds != null) {
                for (Iterator<RobinsonTriangle> iterator = triangles.iterator(); iterator.hasNext(); ) {
                    RobinsonTriangle tri = iterator.next();
                    if (!tri.overlaps(bounds)) {
                        iterator.remove();
                    }
                }
            }
        }
        return triangles;
    }

    public static List<RobinsonTriangle> decompose1(List<RobinsonTriangle> triangles, int level)
    {
        return decompose1(triangles, (level&1) != 0);
    }

    public static List<RobinsonTriangle> decompose1(List<RobinsonTriangle> triangles, boolean alphaNotBeta)
    {
        List<RobinsonTriangle> newList = new ArrayList<RobinsonTriangle>();

        for (RobinsonTriangle tri : triangles) {
            if (alphaNotBeta) {
                newList.addAll(tri.decomposeAlpha());
            } else {
                newList.addAll(tri.decomposeBeta());
            }
        }
        return newList;
    }


    public void paintPenrose(Graphics2D g2, Dimension sz, ImageObserver obs)
    {
        g2.translate(sz.width/2, sz.height/2);
        double ss = Math.min(sz.width, sz.height/Math.sin(RobinsonTriangle.d36)) ;
        g2.scale(ss, ss);
        g2.translate(-0.5, -0.25);

        g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        g2.setColor(Color.RED);
        g2.fill(colorA);

        g2.setColor(Color.GREEN);
        g2.fill(colorO);

        g2.setColor(Color.BLACK);
        g2.draw(lines);
    }

}
