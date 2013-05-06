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

        int uSize = 13;
        int vSize = 13;

        BitMatrix hippo = new BitMatrix(uSize,  vSize);

        hippo.setAt(7,9, false);
        hippo.setAt(5,3, false);
        hippo.setAt(3, 7, false);
        hippo.setAt(9, 5, false);

        hippo.setAt(5,5,false);
        hippo.setAt(5,7,false);
        hippo.setAt(6,6, false);
        hippo.setAt(7,5,false);
        hippo.setAt(7,7,false);
        if (false) {
            hippo.setAt(4,6, false);
            hippo.setAt(6,4, false);
            hippo.setAt(6,8, false);
            hippo.setAt(8,6, false);
        }

//        hippo.setAt(3,7, false);
//        hippo.setAt(2,6, false);

        Style cornerStyle = true ? new Style2(): new Style1();

        geometrize(kl, cellSize, uSize, vSize, hippo, cornerStyle);

        Writer w = new FileWriter("/tmp/knot.svg");
        List<SVGThing>[] layers = kl.layers(SVGLine.stroke("#fd4", "3px"), SVGLine.stroke("#000", "4px"));
        writeSVG(w, uSize*cellSize, vSize*cellSize, layers);
        w.close();
    }

    public static void geometrize(KnotLayers kl, double cellSize, int uSize, int vSize, BitMatrix hippo, Style cornerStyle)
    {
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


                boolean h_e = hippo.at(u + 1, v);
                boolean h_w = hippo.at(u - 1, v);
                boolean h_n = hippo.at(u, v - 1);
                boolean h_s = hippo.at(u, v + 1);

                if (!h_e) {
                    if (!h_w) {
                        // dead space
                    } else if (!hippo.at(u-1, v-2)) {
                        cornerStyle.point(kl, x1,y2, x0,y0, x1,y1);
                    } else if (!hippo.at(u-1, v+2)) {
                        cornerStyle.point(kl, x1,y1, x0,y0, x1,y2);
                    } else {
                        cornerStyle.bounce(kl, x1, y1,
                            x0, y0,
                            x1, y2);
                    }
                } else if (!h_n) {

                    if (!h_s) {
                        // dead space
                    } else if (!hippo.at(u + 2, v+1)) {
                        cornerStyle.point(kl, x1,y2, x0,y0, x2,y2);
                    } else if (!hippo.at(u-2, v+1)) {
                        cornerStyle.point(kl, x2,y2, x0,y0, x1,y2);
                    } else {
                        cornerStyle.bounce(kl, x1,y2, x0,y0, x2,y2);
                    }

                } else if (!h_w) {

                    if (!hippo.at(u+1, v-2)) {
                        cornerStyle.point(kl, x2,y2, x0,y0, x2,y1);
                    } else if (!hippo.at(u+1, v+2)) {
                        cornerStyle.point(kl, x2,y1, x0,y0, x2,y2);
                    } else {
                        cornerStyle.bounce(kl, x2,y2, x0,y0, x2,y1);
                    }
                } else if (!h_s) {

                    if (!hippo.at(u+2, v-1)) {
                        cornerStyle.point(kl, x1,y1, x0,y0, x2,y1);
                    } else if (!hippo.at(u-2, v-1)) {
                        cornerStyle.point(kl, x2,y1, x0,y0, x1,y1);
                    } else {
                        cornerStyle.bounce(kl, x1,y1, x0,y0, x2,y1);
                    }

                } else if (h_n&&h_s&&h_w&&h_e) {
                    cornerStyle.basicOverUnder(reverse ? kl.reversed() : kl, x1, y1, x2, y2,
                        x1, y2, x2, y1);
                } else {
                    // dead space
                }
            }
        }
    }

    public static class BitMatrix
    {

        private final int uSize;
        private final int vSize;
        boolean [] hippo;

        public BitMatrix(int uSize, int vSize)
        {
            this.uSize = uSize;
            this.vSize = vSize;
            hippo = new boolean[uSize*vSize];
            Arrays.fill(hippo, true);
        }

        public boolean at(int u, int v)
        {
            if (u<0 || u>= uSize
                || v<0 || v>=vSize)
                return false;

            return hippo[v*uSize +u];
        }

        public void setAt(int u,int v, boolean newVal)
        {
             hippo[v*uSize +u] = newVal;
        }
    }

    private static void giraffe(KnotLayers kl, int uSize, int vSize, Style cornerStyle, int u, int v, double x1, double y1, double x2, double y2, double x0, double y0, boolean reverse)
    {
        if (u<1) {
            if (v < 1)
                cornerStyle.nub(kl, x2,y2, x0, y0);
            else if (v + 1 >= vSize)
                cornerStyle.nub(kl, x2, y1, x0, y0);
            else if (v<2) {
                cornerStyle.point(kl, x2,y2, x0,y0, x2,y1);
            } else if (v+2 >= vSize) {
                cornerStyle.point(kl, x2,y1, x0,y0, x2,y2);
            } else
                cornerStyle.bounce(kl, x2, y1,
                    x0, y0,
                    x2, y2);

        } else if (u+1>= uSize) {
            if (v < 1)
                cornerStyle.nub(kl, x1, y2, x0, y0);
            else if (v<2) {
                cornerStyle.point(kl, x1,y2, x0,y0, x1,y1);
            } else if (v + 1 >= vSize)
                cornerStyle.nub(kl, x1, y1, x0, y0);
            else if (v+2 >= vSize) {
                cornerStyle.point(kl, x1,y1, x0,y0, x1,y2);
            } else
                cornerStyle.bounce(kl, x1, y1,
                    x0, y0,
                    x1, y2);

        } else if (v<1) {
            if (u<2) {
                cornerStyle.point(kl, x2,y2, x0,y0, x1,y2);
            } else if (u+2>=uSize) {
                cornerStyle.point(kl, x1,y2, x0,y0, x2,y2);
            } else {
                cornerStyle.bounce(kl, x1, y2,
                    x0, y0,
                    x2, y2);
            }
        } else if (v+1>=vSize) {
            if (u<2) {
                cornerStyle.point(kl, x2,y1, x0,y0, x1,y1);
            } else if (u+2>=uSize) {
                cornerStyle.point(kl, x1,y1, x0,y0, x2,y1);
            } else {
                cornerStyle.bounce(kl, x1, y1,
                    x0, y0,
                    x2, y1);
            }
        } else {
            cornerStyle.basicOverUnder(reverse ? kl.reversed() : kl, x1, y1, x2, y2,
                x1, y2, x2, y1);
        }
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

            if (false && curr instanceof PathData.Multi)
                style = style.replaceAll("stroke:#66f", "stroke:#f66").replaceAll("stroke:#cfc", "stroke:#fcc");

            rval.add(new SVGThing(style, curr));
        }

        return rval;
    }

}
