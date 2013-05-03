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
        KnotLayers kl = new KnotLayers();

        double cellSize=4;

        int uSize = 5;
        int vSize = 3;

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
                        cornerStyle.nub(kl, x2,y2, x0, y0);
                    else if (v + 1 >= vSize)
                        cornerStyle.nub(kl, x2,y1, x0, y0);
                    else
                        cornerStyle.bounce(kl, x2, y1,
                            x0, y0,
                            x2, y2);

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
                        x2, y2);
                } else if (v+1>=vSize) {
                    cornerStyle.bounce(kl, x1, y1,
                        x0, y0,
                        x2, y1);
                } else {
                    cornerStyle.basicOverUnder(reverse ? kl.reversed() : kl, x1, y1, x2, y2,
                        x1, y2, x2, y1);
                }
            }
        }

        Writer w = new FileWriter("/tmp/knot.svg");
        writeSVG(w, uSize*cellSize, vSize*cellSize, kl.layers(SVGLine.stroke("#cfc", "3px"), SVGLine.stroke("#cfc", "3px")));
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
            for (SVGThing svgThing : merged(layer)) {
                w.write(svgThing.asSVG());
            }
            w.write("</g>\n");
        }
        w.write("</svg>\n");
    }

    public static List<SVGThing> merged(List<SVGThing> things_)
    {
        if (false)
            return things_;


        List<SVGThing> things = new ArrayList<SVGThing>(things_);

        List<SVGThing> rval = new ArrayList<SVGThing>();

        for (int i=0; i<things.size(); i++) {
            String style = things.get(i).style;
            PathData curr = things.get(i).path;

            PathData r= curr.reversed();

            for (int j=i+1; j<things.size(); ) {
                SVGThing other = things.get(j);
                if (!style.equals(other.style)) {
                    continue;
                }

                if (other.path.startsWith(curr.endX(), curr.endY())) {
                    System.out.println("merged");
                    curr = curr.appended(other.path);
                    r = curr.reversed();
                    things.remove(j);
                    j=i+1;
                } else if (curr.startsWith(other.path.endX(), other.path.endY())) {
                    System.out.println("merged R");
                    curr = other.path.appended(curr);
                    r = curr.reversed();
                    things.remove(j);
                    j=i+1;
                } else if (other.path.startsWith(r.endX(), r.endY())) {
                    System.out.println("merged x");
                    curr = r.appended(other.path);
                    r = curr.reversed();
                    things.remove(j);
                    j=i+1;
                } else if (r.startsWith(other.path.endX(), other.path.endY())) {
                    System.out.println("merged y");
                    curr = other.path.appended(r);
                    r = curr.reversed();
                    things.remove(j);
                    j=i+1;
                } else {
                    j++;
                }
            }

            if (curr instanceof PathData.Multi)
                style = style.replaceAll("stroke:#66f", "stroke:#f66").replaceAll("stroke:#cfc", "stroke:#fcc");

            rval.add(new SVGThing(style, curr));
        }

        return rval;
    }

}
