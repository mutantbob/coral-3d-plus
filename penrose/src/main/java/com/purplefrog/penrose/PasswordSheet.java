package com.purplefrog.penrose;

import java.awt.geom.*;
import java.io.*;
import java.util.*;

/**
 * Generate an SVG document with a bunch of Penrose tiles, each with a letter or digit in it
 * Created by thoth on 2/20/14.
 */
public class PasswordSheet
{
    public static void main(String[] argv)
        throws FileNotFoundException
    {
        PrintStream pw = new PrintStream(new FileOutputStream("/tmp/password.svg"));

        double w1 = 850;
        double h1 = 1100;

        pw.println(CreateSVGDiagram.svgHeader(w1, h1));

        pw.println("<defs>\n" + defaultStyleSheet() + "</defs>");

        Random rand = new Random();

        RobinsonTriangle seed = new TriangleA();
        Matrix2 rot = Matrix2.rotation(rand.nextDouble()*Math.PI*2);
        seed = new TriangleA(rot.postMult(seed.a), rot.postMult(seed.b), rot.postMult(seed.c));

        double scale = 50;

        double w2 = w1/scale /2;
        double h2 = h1/scale/2;

        int[] detail = {0};
        FillRectangle.Rect bounds = new FillRectangle.Rect(-w2, w2, -h2, h2);

        List<RobinsonTriangle> triangles = expand2(seed, detail, bounds);

        boolean dartsAndKites = 0 != (detail[0]&1);


        pw.println("<g transform=\" scale("+scale+") translate("+w2+","+h2+") \">");

        {
            pw.println("<g>");
            for (RobinsonTriangle triangle : triangles) {
                pw.println(triangleAt(triangle, dartsAndKites));
            }
            pw.println("</g>");
        }

        {
            List<Point2D> centers = PenroseUpConvert.computeCenters(triangles, dartsAndKites);
            pw.println("<g>");

            for (int i = 0; i < centers.size(); i++) {
                Point2D center = centers.get(i);
                if (!duplicate(center, centers, i)) {
                    char symbol = randomSymbol2(rand);

                    String textBlock = "<text class=\"symbol\" x=\"" + center.getX() + "\" y=\"" + center.getY() + "\" >" +
                        escXML(symbol)+
                        "</text>";
                    pw.println(textBlock);
                }
            }
            pw.println("</g>");
        }

        pw.println("</g>");

        pw.println("</svg>");
    }

    private static String escXML(char symbol)
    {
        if ('&'==symbol)
            return "&amp;";
        else if ('<'==symbol)
            return "&lt;";
        else if ('>'==symbol)
            return "&gt;";
        else
            return String.valueOf(symbol);
    }

    public static char[] candidateSymbols = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~"
    .toCharArray();

    public static char[] digits = "0123456789".toCharArray();
    public static char[] upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    public static char[] lower = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    public static char[] symbols = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~".toCharArray();

    public static char randomSymbol(Random rand)
    {
        return randomSymbol_(rand, candidateSymbols);
    }

    public static char randomSymbol_(Random rand, char[] dict)
    {
        return dict[rand.nextInt(dict.length)];
    }

    public static char randomSymbol2(Random rand)
    {
        switch (rand.nextInt(6)) {
            case 0:
                return randomSymbol_(rand, upper);
            case 1:
                return randomSymbol_(rand, digits);
            case 2:
                return randomSymbol_(rand, symbols);
            default:
                return randomSymbol_(rand, lower);
        }
    }

    private static boolean duplicate(Point2D candidate, List<Point2D> catalog, int catLen)
    {
        for (int i=0; i<catLen; i++) {
            if (tooClose(candidate, catalog.get(i)))
                return true;
        }
        return false;
    }

    private static boolean tooClose(Point2D a, Point2D b)
    {
        double epsilon = 0.01;
        double dx = a.getX() - b.getX();
        if (dx<-epsilon || dx> epsilon)
            return false;

        double dy = a.getY() - b.getY();
        if (dy<-epsilon || dy > epsilon)
            return false;

        return true;

    }

    private static String defaultStyleSheet()
    {
            return "<style type=\"text/css\">\n" +
                ".triangle { stroke: #888; \n" +
                "stroke-width: 0.03px;\n" +
                "fill:none;\n" +
                "}\n" +
                "\n" +
                ".symbol {\n" +
                "fill:#000;\n" +
                "stroke:none;\n" +
                "font-size:0.4px;\n" +
                "font-family:Serif;\n" +
                "text-anchor: middle;\n" +
                "dominant-baseline:middle;\n" +
                "}\n" +
                "</style>";
    }

    public static List<RobinsonTriangle> expand2(RobinsonTriangle seed, int[] detail, FillRectangle.Rect bounds)
    {
        List<RobinsonTriangle> triangles;
        {
            RobinsonTriangle macro = FillRectangle.expandToCoverBounds(seed, new FillRectangle.CoinFlipper(), detail, bounds);

            System.out.println(detail[0]);
            if ((detail[0]&1) !=0) {
                detail[0]--; // don't use darts&kites
            }

            triangles = PenroseRenderVector.decompose(detail[0], Arrays.asList(macro), bounds);
        }
        return triangles;
    }


    public static String triangleAt(RobinsonTriangle ta, boolean dartsAndKites)
    {
        return "<path  " +
            "class=\"triangle\" " +
            "d=\"" + CreateSVGDiagram.svgPathFor(ta, dartsAndKites) + "\" />";
    }

}
