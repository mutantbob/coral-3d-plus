package com.purplefrog.penrose;

import java.awt.geom.*;
import java.util.*;
import java.util.List;

/**
 * Created by thoth on 2/20/14.
 */
public class FillRectangle
{

    /**
     * Based on the seed {@link com.purplefrog.penrose.RobinsonTriangle}
     * and a {@link com.purplefrog.penrose.FillRectangle.CoinFlipper},
     * expand the penrose tiling until it covers the rectangle,
     * then decompose it back down to the level of the seed triangle.
     * @param seed
     * @param rand
     * @param detail is populated with the number of decompositions performed after expansion.
     * @param bounds
     * @return
     */
    public static List<RobinsonTriangle> fillRectangle(RobinsonTriangle seed, CoinFlipper rand, int[] detail, Rect bounds)
    {
        RobinsonTriangle macro = expandToCoverBounds(seed, rand, detail, bounds);

        if (true) {
            filled(macro, bounds);
        }

        return PenroseRenderVector.decompose(detail[0], Arrays.asList(macro), bounds);
    }

    public static RobinsonTriangle expandToCoverBounds(RobinsonTriangle seed, CoinFlipper rand, int[] detail, Rect bounds)
    {
        RobinsonTriangle macro = seed;
        detail[0] = 0;
        while (! filled(macro, bounds)) {
            boolean b = rand.nextBoolean();
            System.out.print(detail[0]+" "+b+" ");
            if (b)
                macro = macro.composeAlpha(detail);
            else
                macro = macro.composeBeta(detail);

        }
        return macro;
    }

    public static boolean filled(RobinsonTriangle triangle, Rect bounds)
    {
        return inside(triangle, bounds.minX, bounds.minY)
            && inside(triangle, bounds.minX, bounds.maxY)
            && inside(triangle, bounds.maxX, bounds.minY)
            && inside(triangle, bounds.maxX, bounds.maxY);
    }

    public static boolean inside(RobinsonTriangle triangle, double x1, double y1)
    {
        Point2D.Double p2 = triangle.toTriangleSpace(x1, y1);

//        Point2D.Double p3 = triangle.fromTriangleSpace(p2.x, p2.y);
//        System.out.println(x1+","+y1+" -> "+p2 +" -> "+p3);

        return p2.x >=0 && p2.y >=0
            && p2.x+p2.y <=1;
    }

    public static class Rect
    {
        public final double minX;
        public final double maxX;
        public final double minY;
        public final double maxY;

        public Rect(double minX, double maxX, double minY, double maxY)
        {
            this.minX = minX;
            this.maxX = maxX;
            this.minY = minY;
            this.maxY = maxY;
        }
    }

    public static class CoinFlipper
    {
        Random rand;

        public CoinFlipper()
        {
            this(new Random());
        }

        public CoinFlipper(Random rand)
        {
            this.rand = rand;
        }

        public boolean nextBoolean() {
            return rand.nextBoolean();
        }
    }

    public static class FakeCoinFlipper
    extends CoinFlipper
    {
        long state;

        public FakeCoinFlipper(long state)
        {
            super(null);
            this.state = state;
        }

        @Override
        public boolean nextBoolean()
        {
            boolean rval = 0 != (state&1);
            state = state >> 1;
            return rval;
        }
    }
}
