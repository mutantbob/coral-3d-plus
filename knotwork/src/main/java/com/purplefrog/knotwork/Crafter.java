package com.purplefrog.knotwork;

import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 5/3/13
 * Time: 12:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class Crafter
{

    public static void main(String[] argv)
        throws IOException
    {
        List<SVGThing> overHalo = new ArrayList<SVGThing>();
        List<SVGThing> underHalo = new ArrayList<SVGThing>();
        List<SVGThing> over = new ArrayList<SVGThing>();
        List<SVGThing> under = new ArrayList<SVGThing>();

        KnotLayers kl = new KnotLayers(underHalo, under, overHalo, over);

        double cellSize=4;

        int uSize = 10;
        int vSize = 12;

        Style cornerStyle = true ? new Style2(): new Style1();

        for (int u=0; u< uSize; u++) {
            for (int v=0; v< vSize; v++) {

                if (0 == (1&(u+v)))
                    continue;

                double x1 = u*cellSize;
                double y1 = v* cellSize;
                double x2 = x1 + cellSize;
                double y2 = y1 + cellSize;
                double x0 = (u+0.5)*cellSize;
                double y0 = (v+0.5)*cellSize;
                boolean reverse = 0==((u)&1);

                if (u<1) {
                    if (v < 1)
                        cornerStyle.nub(kl, x2 + 0.1, y2 + 0.1, x0, y0);
                    else if (v + 1 >= vSize)
                        cornerStyle.nub(kl, x2 + 0.1, y1-0.1, x0, y0);
                    else
                        cornerStyle.bounce(kl, x2 + 0.1, y1 - 0.1,
                            x0, y0,
                            x2 + 0.1, y2 + 0.1);

                } else if (u+1>= uSize) {
                    if (v < 1)
                        cornerStyle.nub(kl, x1, y2, x0, y0);
                    else if (v + 1 >= vSize)
                        cornerStyle.nub(kl, x1, y1, x0, y0);
                    else
                        cornerStyle.bounce(kl, x1, y1,
                            x0, y0,
                            x1, y2);

                } else if (v<1) {
                    cornerStyle.bounce(kl, x1, y2,
                        x0, y0,
                        x2 + 0.1, y2 + 0.1);
                } else if (v+1>=vSize) {
                    cornerStyle.bounce(kl, x1, y1,
                        x0, y0,
                        x2, y1);
                } else {
                    cornerStyle.basicOverUnder(reverse ? kl.reversed() : kl, x1, y1, x2 + 0.1, y2 + 0.1,
                        x1, y2, x2 + 0.1, y1-0.1);
                }
            }
        }

        Writer w = new FileWriter("/tmp/knot.svg");
        writeSVG(w, uSize*cellSize, vSize*cellSize, kl.layers());
        w.close();
    }

    public static void writeSVG(Writer w, double width, double height, List<SVGThing>... layers)
        throws IOException
    {
        w.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
        w.write("<svg\n" +
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
            "   sodipodi:docname=\"knotwork\">\n\n");

        for (List<SVGThing> layer : layers) {
            w.write("<g inkscape:groupmode=\"layer\" >\n");
            for (SVGThing svgThing : layer) {
                w.write(svgThing.asSVG());
            }
            w.write("</g>\n");
        }
        w.write("</svg>\n");
    }

}
