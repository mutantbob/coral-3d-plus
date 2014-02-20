package com.purplefrog.voronoiTile;

import be.humphreys.simplevoronoi.*;

import java.awt.geom.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 6/20/13
 * Time: 4:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class FaceSet
{
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(FaceSet.class);

    private Map<Integer, Face> faces = new TreeMap<Integer, Face>();

    public void add(GraphEdge e)
    {
        getFace(e.site1).add(e);
        getFace(e.site2).add(e);
    }

    private Face getFace(int idx)
    {
        Face rval = faces.get(idx);
        if (null==rval) {
            rval = new Face(idx);
            faces.put(idx, rval);
        }
        return rval;
    }

    public Collection<Face> allFaces()
    {
        return faces.values();
    }

    public void color(Random rand)
    {
        int nextColor=0;
        while (true) {
            Face f = pickUncoloredFace();
            if (f==null)
                break;

            Set<Integer> neighborColors = neighborColors(f);

            int picked = pickColor(rand, nextColor, neighborColors);
            if (picked >= nextColor)
                nextColor = picked+1;

            f.color = picked;
        }
    }

    private Face pickUncoloredFace()
    {
        Face rval = null;
        int coloredNeighborCount = -1;

        for (Face face : faces.values()) {
            if (face.color !=null)
                continue;
            int count = countColoredNeighbors(face);
            if (count > coloredNeighborCount) {
                rval = face;
                coloredNeighborCount = count;
            }
        }

        return rval;
    }

    public int countColoredNeighbors(Face face)
    {
        int rval=0;
        for (Face f2 : faces.values()) {
            if (face != f2 && f2.adjoins(face.site) && f2.color != null) {
                rval++;
            }
        }
        return rval;
    }

    public Set<Integer> neighborColors(Face f0)
    {
        Set<Integer> rval = new TreeSet<Integer>();
        for (Face f2 : faces.values()) {
            if (f2==f0)
                continue;
            if (f2.adjoins(f0.site)){
                if (f2.color!=null) {
                    rval.add(f2.color);
                }
            }
        }

        return rval;
    }

    private static int pickColor(Random rand, int nextColor, Set<Integer> forbidden)
    {
        int picked =0;
        int div = 1;
        for (int i=0; i<nextColor; i++) {
            if (forbidden.contains(i))
                continue;
            if (rand.nextInt(div)==0) {
                picked = i;
                div++;
            }
        }
        if (div==1) {
            picked=nextColor;
        }
        return picked;
    }

    public static class Face
    {

        private List<GraphEdge> edges = new ArrayList<GraphEdge>();
        public Integer color=null;

        public int site;

        public Face(int site)
        {
            this.site = site;
        }

        public void add(GraphEdge e)
        {
            edges.add(e);
        }

        public List<Point2D.Double> asLoop()
        {
            List<Point2D.Double> rval = new ArrayList<Point2D.Double>();

            List<GraphEdge> unseated = new ArrayList<GraphEdge>(edges);

            {
                GraphEdge e = unseated.remove(0);
                rval.add(new Point2D.Double(e.x1, e.y1));
                rval.add(new Point2D.Double(e.x2, e.y2));
            }

            while (!unseated.isEmpty()) {
                Point2D.Double other = consumeEdge(unseated, rval.get(0));
                if (other==null) {
                    other = consumeEdge(unseated, rval.get(rval.size()-1));
                    if (other==null) {
                        logger.error("sadness!");
                        break;
                    }
                    rval.add(other);
                } else {
                    rval.add(0, other);
                }


            }

            return rval;
        }

        public static Point2D.Double consumeEdge(List<GraphEdge> candidates, Point2D.Double anchor)
        {

            for (Iterator<GraphEdge> iterator = candidates.iterator(); iterator.hasNext(); ) {
                GraphEdge candidate = iterator.next();
                if (close(candidate.x1, anchor.x) &&
                    close(candidate.y1, anchor.y)) {
                    iterator.remove();
                    return new Point2D.Double(candidate.x2, candidate.y2);
                }
                if (close(candidate.x2, anchor.x) &&
                    close(candidate.y2, anchor.y)) {
                    iterator.remove();
                    return new Point2D.Double(candidate.x1, candidate.y1);
                }
            }

            return null;
        }

        public static boolean close(double a, double b)
        {
            return Math.abs(a - b) < 1e-10;
        }

        public boolean adjoins(int site)
        {
            for (GraphEdge edge : edges) {
                if (edge.site1==site || edge.site2==site)
                    return true;
            }
            return false;
        }
    }
}
