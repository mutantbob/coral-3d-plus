package com.purplefrog.penrose;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;
import java.util.List;

/**
* Created by thoth on 2/20/14.
*/
public class PenroseUpConvert
    implements PenroseRender
{

    float stroke;

    private GeneralPath colorA = new GeneralPath();
    private GeneralPath colorZ = new GeneralPath();
    private GeneralPath colorO = new GeneralPath();
    private GeneralPath lines = new GeneralPath();
    protected final int radius = 8;
    private List<Point2D> centers;

    public PenroseUpConvert(int levels, FillRectangle.CoinFlipper rand1)
    {
        int detail = 6;
        stroke = PenroseRenderVector.strokeWidthFor(detail, 0.5f);


        RobinsonTriangle seedTriangle = new TriangleA();
//            seedTriangle = new TriangleO();
        detail = randomComposition1(detail, seedTriangle, rand1);

        seedTriangle.addToShapesAlpha(new GeneralPath(), colorZ, colorZ);

        System.out.println(detail);
    }

    public int randomComposition1(int detail, RobinsonTriangle seedTriangle, FillRectangle.CoinFlipper rand1)
    {
        Random rand = new Random();

        List<RobinsonTriangle> triangles;

        int[] d2 = {0};
        triangles = FillRectangle.fillRectangle(seedTriangle, rand1, d2, new FillRectangle.Rect(-radius, radius, -radius, radius));
        detail = d2[0];

        boolean dartsAndKites = 0 != (detail & 1);
        centers = computeCenters(triangles, dartsAndKites);

        for (RobinsonTriangle tri : triangles) {
            if (dartsAndKites)
                tri.addToShapesAlpha(lines, colorA, colorO);
            else
                tri.addToShapesBeta(lines, colorA, colorO);
        }
        return detail;
    }

    public static List<Point2D> computeCenters(List<RobinsonTriangle> triangles, boolean dartsAndKites)
    {
        List<Point2D> centers = new ArrayList<Point2D>();
        for (RobinsonTriangle tri : triangles) {

            double x4 = TriangleA.interpolate(tri.a.x, tri.c.x, 0.5);
            double y4 = TriangleA.interpolate(tri.a.y, tri.c.y, 0.5);

            centers.add(dartsAndKites ? tri.gapCenterAlpha() : tri.gapCenterBeta());
        }
        return centers;
    }

    public int progressiveComposition2(int detail, RobinsonTriangle seedTriangle, FillRectangle.CoinFlipper rand1)
    {
        Random rand = new Random();

        List<RobinsonTriangle> triangles;

        int[] d2 = {0};
        triangles = FillRectangle.fillRectangle(seedTriangle, rand1, d2, new FillRectangle.Rect(-radius, radius, -radius, radius));
        detail = d2[0];

        for (RobinsonTriangle tri : triangles) {
            if (0 != (detail & 1))
                tri.addToShapesAlpha(lines, colorA, colorO);
            else
                tri.addToShapesBeta(lines, colorA, colorO);
        }
        return detail;
    }

    public int randomComposition2(int detail, RobinsonTriangle seedTriangle)
    {
        RobinsonTriangle t1 = seedTriangle;


        Random rand = new Random();

        List<RobinsonTriangle> triangles;
        int[] d2 = {0};
        while ( detail > d2[0] ) {
            t1 =rand.nextBoolean() ? t1.composeAlpha(d2):t1.composeBeta(d2);
        }

        detail = d2[0];
        System.out.println("detail at "+detail);

        triangles = PenroseRenderVector.decompose(detail, t1);

        for (RobinsonTriangle tri : triangles) {
            if (0 != (detail & 1))
                tri.addToShapesAlpha(lines, colorA, colorO);
            else
                tri.addToShapesBeta(lines, colorA, colorO);
        }
        return detail;
    }

    public List<RobinsonTriangle> composition1(List<RobinsonTriangle> triangles, RobinsonTriangle tmp, boolean option2)
    {
        List<RobinsonTriangle> rval = new ArrayList<RobinsonTriangle>(triangles);
        RobinsonTriangle t2;
        if (option2) {
            t2 = tmp.composeAlpha(new int[1]);
        } else {
            t2 = tmp.composeBeta(new int[1]);
        }
        rval.add(0, t2);
        return rval;
    }

    public void paintPenrose(Graphics2D g2, Dimension sz, ImageObserver obs)
    {
        g2.translate(sz.width/ 2, sz.height/ 2);
        double ss = Math.min(sz.width, sz.height
        //    /Math.sin(RobinsonTriangle.d36)
        ) ;
        double r3 = this.radius *3;
        ss = ss/r3;
        g2.scale(ss, ss);
        g2.translate(-0.5, -0.25);

        g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        g2.setColor(Color.RED);
        g2.fill(colorA);

        g2.setColor(Color.GREEN);
        g2.fill(colorO);

        g2.setColor(Color.BLACK);
        g2.draw(lines);

        g2.setColor(Color.cyan);
        g2.fill(colorZ);

        g2.setColor(Color.blue);
        if (false) {
            g2.drawLine(0, -10, 0, 10);
            g2.drawLine(-10, 0, 10,0);
        } else {
            g2.drawLine(-radius, -radius, radius, -radius);
            g2.drawLine(radius, -radius, radius, radius);
            g2.drawLine(radius, radius, -radius, radius);
            g2.drawLine(-radius, radius, -radius, -radius);

        }

        g2.setColor(Color.ORANGE);
        for (Point2D p1 : centers) {
            Shape rect = new Rectangle2D.Double(p1.getX()-0.1, p1.getY()-0.1, 0.2, 0.2);
            g2.draw(rect);
        }
    }
}
