package com.purplefrog.penrose;

import java.io.*;
import java.util.*;

/**
 * This creates an SVG diagram that illustrates how {@link com.purplefrog.penrose.RobinsonTriangle}s
 * are composed and decomposed by our algorithms.
 * <p>
 * Created by thoth on 2/18/14.
 */
public class CreateSVGDiagram
{

    public static void main(String[] argv)
        throws FileNotFoundException
    {

        PrintWriter pw = new PrintWriter("/tmp/robinson.svg");

        pw.println(svgHeader(744.09448819, 1052.3622047));

        String[] fills = { "#fdd", "#dfd" };
        String origFill = "#ddf";

        {
            pw.println("<g >");
            diagramDecomposition(pw, fills, origFill);
            pw.println("</g>");
        }

        {
            pw.println("<g transform=\"translate(0,400)\">");


            int y1 = 130;
            int y2 = 250;
            int x1 = 50;
            int x2 = 200;
            int x3 = 500;

            pw.println("<text style=\"fill:#000;stroke:none;font-size:24px;font-family:Serif\" x=\"100\" y=\"25\">composition</text>");

            pw.println("<text style=\"fill:#000;stroke:none;font-size:12px;font-family:Serif\" x=\"" + (x2 + 20) + "\" y=\"50\">alpha</text>");
            pw.println("<text style=\"fill:#000;stroke:none;font-size:12px;font-family:Serif\" x=\"" + (x3 + 20) + "\" y=\"50\">beta</text>");

            RobinsonTriangle ta = new TriangleA();
            TriangleO to = new TriangleO();

            pw.println(triangleAt(ta, x1, y1, 100, origFill));
            pw.println(triangleAt(to, x1, y2, 100, origFill));

            multiTriangle(pw, ta.composeAlpha(new int[1]).decomposeAlpha(), x2,y1, fills);
            multiTriangle(pw, ta.composeBeta(new int[1]).decomposeBeta(), x3,y1, fills);

            multiTriangle(pw, to.composeAlpha(new int[1]).decomposeAlpha(), x2,y2, fills);
            multiTriangle(pw, to.composeBeta(new int[1]).decomposeBeta(), x3,y2, fills);
            pw.println("</g>");
        }

        pw.print("</svg>\n");

        pw.close();
    }

    public static String svgHeader(double width, double height)
    {
        return "<svg\n" +
            "   xmlns:dc=\"http://purl.org/dc/elements/1.1/\"\n" +
            "   xmlns:cc=\"http://creativecommons.org/ns#\"\n" +
            "   xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n" +
            "   xmlns:svg=\"http://www.w3.org/2000/svg\"\n" +
            "   xmlns=\"http://www.w3.org/2000/svg\"\n" +
            "   xmlns:sodipodi=\"http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd\"\n" +
            "   xmlns:inkscape=\"http://www.inkscape.org/namespaces/inkscape\"\n" +
            "   width=\"" + width + "\"\n" +
            "   height=\"" + height + "\"\n" +
            "   id=\"svg2\"\n" +
            "   version=\"1.1\"\n" +
            "   inkscape:version=\"0.48.4 r9939\"\n" +
            "   sodipodi:docname=\"New document 1\">";
    }

    public static void diagramDecomposition(PrintWriter pw, String[] fills, String origFill)
    {
        pw.println("<text style=\"fill:#000;stroke:none;font-size:24px;font-family:Serif\" x=\"100\" y=\"25\">decomposition</text>");

        pw.println("<text style=\"fill:#000;stroke:none;font-size:12px;font-family:Serif\" x=\"220\" y=\"50\">alpha</text>");
        pw.println("<text style=\"fill:#000;stroke:none;font-size:12px;font-family:Serif\" x=\"370\" y=\"50\">beta</text>");

        TriangleA ta = new TriangleA();
        TriangleO to = new TriangleO();

        int y1 = 130;
        int y2 = 250;
        pw.println(triangleAt(ta, 50, y1, 100, origFill));
        pw.println(triangleAt(to, 50, y2, 100, origFill));


        {
            Collection<RobinsonTriangle> ts = ta.decomposeAlpha();
            multiTriangle(pw, ts, 200, y1, fills);
        }
        {
            Collection<RobinsonTriangle> ts = ta.decomposeBeta();
            multiTriangle(pw, ts, 350, y1, fills);
        }

        {
            Collection<RobinsonTriangle> ts = to.decomposeAlpha();
            multiTriangle(pw, ts, 200, y2, fills);
        }
        {
            Collection<RobinsonTriangle> ts = to.decomposeBeta();
            multiTriangle(pw, ts, 350, y2, fills);
        }

    }

    public static void multiTriangle(PrintWriter pw, Collection<RobinsonTriangle> ts, int x, int y, String[] fills)
    {
        int i=0;
        for (RobinsonTriangle t : ts) {
            pw.println(triangleAt(t, x, y, 100, fills[i%fills.length]));
            i++;
        }
    }

    public static String triangleAt(RobinsonTriangle ta, int x, int y, int scale, String fill)
    {
        return "<path transform=\" translate(" + x + "," + y + ") scale(" + scale + ",-"+ scale +")\" " +
            "style=\"fill:" + fill + ";stroke:#000;stroke-width:0.03px\" " +
            "d=\"" + svgPathFor(ta, true) + "\" />";
    }

    public static String svgPathFor(RobinsonTriangle ta, boolean dartsAndKites)
    {
        if (dartsAndKites) {
            return "M " + ta.a.getX() + ","+ta.a.getY()
                +" " + ta.b.getX() + ","+ta.b.getY()
                +" " + ta.c.getX() + ","+ta.c.getY();
        } else {
            return "M " + ta.c.getX() + ","+ta.c.getY()
                +" " + ta.a.getX() + ","+ta.a.getY()
                +" " + ta.b.getX() + ","+ta.b.getY();

        }
    }
}
