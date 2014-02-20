package com.purplefrog.voronoiTile;

import be.humphreys.simplevoronoi.*;
import org.apache.log4j.*;

import java.awt.geom.*;
import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 6/20/13
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class GenerateRandom
{
    private static final Logger logger = Logger.getLogger(GenerateRandom.class);


    public static void main(String[] argv)
        throws IOException
    {
        Voronoi  v = new Voronoi(0.00001);

        int count = 100;
        int max = 20;
        int min = -max;

        Random rand = new Random(4262);
        double[] xs = randomArray(count, min, max, rand);
        double[] ys = randomArray(count, min, max, rand);

        FaceSet faceSet = new FaceSet();

        List<GraphEdge> es = v.generateVoronoi(xs, ys, min, max, min, max);

        try {
            FileWriter w = new FileWriter("/tmp/voronoi.svg");
            writeSVG(w, es, xs, ys);
            w.close();
        } catch (IOException e) {
            logger.warn("", e);
        }

        for (GraphEdge e : es) {

            if (degenerate(e)) {
                continue; // skip this one
            }

            System.out.println(stringDump(e));

            faceSet.add(e);
        }

        faceSet.color(rand);

        dumpForBlender(new PrintWriter(new FileWriter("/tmp/voronoi.py")), xs, ys, faceSet);
    }

    public static void dumpForBlender(PrintWriter pw, double[] xs, double[] ys, FaceSet faceSet)
    {
        for (FaceSet.Face face : faceSet.allFaces()) {

            double x0 = xs[face.site];
            double y0 = ys[face.site];
            pw.print("makeTile(" + x0 + "," + y0 + ", [\n");
            List<Point2D.Double> verts = face.asLoop();
            for (Point2D.Double vert : verts) {
                pw.print("\t(" + (vert.x - x0) + "," + (vert.y - y0) + "), \n");
            }
            pw.println("\t] , thickness, " +face.color+")");
        }
        pw.flush();
    }

    public static boolean degenerate(GraphEdge e)
    {
        return e.x1 == e.x2 &&
            e.y1==e.y2;
    }

    public static void writeSVG(Writer w, List<GraphEdge> es, double[] xs, double[] ys)
        throws IOException
    {
        StringBuilder doc = new StringBuilder();

        doc.append("<svg width=\"20\" height=\"20\">\n");
        doc.append("<g transform=\"translate(10, 10)\">\n");

        for (int i=0; i<xs.length; i++) {
            doc.append("<path style=\"fill:#000\" d=\""+dotFor(xs[i], ys[i])+" z\"/>\n" );
        }

        for (GraphEdge e : es) {
            String d = "M "+e.x1+","+e.y1+" L "+e.x2+","+e.y2+" ";
            doc.append("<path style=\"stroke:#00f; stroke-width:0.1\" d=\""+d+"\" />\n");
        }

        doc.append("</g>\n</svg>\n");

        w.write(doc.toString());
    }

    public static String dotFor(double x, double y)
    {
        double x1 = x-0.1;
        double x2 = x+0.1;
        double y1 = y-0.1;
        double y2 = y+0.1;

        return
            "M " + x + "," + y1 + " "
            + "L " + x2 + "," + y + " "
            + "L " + x + "," + y2 + " "
            + "L " + x1 + "," + y + " "
            +"z";
    }

    public static String stringDump(GraphEdge e)
    {
        return e.x1+","+e.y1+" .. "+e.x2+","+e.y2+" between "+e.site1+" and "+e.site2;
    }

    public static double[] randomArray(int count, double min, double max, Random rand)
    {
        double[] rval = new double[count];

        for (int i = 0; i < rval.length; i++) {
            rval[i] = rand.nextDouble()*(max-min) +min;
        }

        return rval;
    }
}
