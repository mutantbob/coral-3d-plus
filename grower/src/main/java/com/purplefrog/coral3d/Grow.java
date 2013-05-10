package com.purplefrog.coral3d;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 5/2/13
 * Time: 5:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class Grow
{
    public static void main(String[] argv)
    {
        Lattice l = new Lattice();

        Random rand = new Random();
        Topology1 topology = new Topology1();

        registerLump(l, 0,0,0);
        for (int i=0; l.count()<1000; i++) {
            randomWalk(l, rand, topology, new Topology2());
        }
    }

    public static void randomWalk(Lattice l, Random rand, Topology1 t, Topology2 t2)
    {
        int x;
        int y;
        int z;

        Picker picker = new Picker(l.sx+2, l.sy+2, l.sz+1);

        int[] coords = picker.pick(rand.nextInt(picker.candidates()));
        x = coords[0]+l.minx-1;
        y = coords[1]+l.miny-1;
        z = coords[2]; // floor of zero

        while (true) {


            if (true) {
                if (touch(l, t2, x, y, z)) {
                    registerLump(l, x,y,z);
                    break;
                }
            }

            int dir = rand.nextInt(t.numDirections(x, y, z));

            int x2 = t.nextX(x, y, z, dir);
            int y2 = t.nextY(x, y, z, dir);
            int z2 = t.nextZ(x, y, z, dir);

            if (l.hasLumpAt(x2, y2, z2)) {
                registerLump(l, x,y,z);
                break;
            }

            if (x2 < l.minx-4 || y2 < l.miny-4 || z2<0
                || x2 >= l.afterMaxx()+4 || y2 >= l.afterMaxY()+4 || z2 >= l.afterMaxZ()+4)
                break; // a miss

            x = x2;
            y=y2;
            z=z2;
        }
    }

    public static boolean touch(Lattice l, Topology t, int x, int y, int z)
    {
        for (int dir=0; dir<t.numDirections(x,y,z); dir++) {
            int x2 = t.nextX(x, y, z, dir);
            int y2 = t.nextY(x, y, z, dir);
            int z2 = t.nextZ(x, y, z, dir);

            if (l.hasLumpAt(x2,y2,z2)) {
                return true;
            }

        }
        return false;
    }

    public static void registerLump(Lattice l, int x, int y, int z)
    {
        l.addLump(x,y,z);
        float[] coords = l.coordsFor(x,y,z);
        System.out.println("addLump("+coords[0]+", "+coords[1]+", "+coords[2]+",  "+l.count()+")");
    }

    interface Topology
    {

        int nextX(int x, int y, int z, int dir);

        int nextY(int x, int y, int z, int dir);

        int nextZ(int x, int y, int z, int dir);

        int numDirections(int x, int y, int z);
    }

    /**
     * topology for a truncated octahedron.  Movement is only permitted on diagonals, never on the X,Y,or Z axis.
     */
    public static class Topology1
        implements Topology
    {

        public int nextX(int x, int y, int z, int dir)
        {
            int shimmy = getShimmy(z);
            int dx = dir%2 +shimmy;
            return x+dx;
        }

        private int getShimmy(int z)
        {
            return (0==(z&1) ? -1:0);
        }

        public int nextY(int x, int y, int z, int dir)
        {
            int shimmy = getShimmy(z);
            int dy = (dir/2)%2 +shimmy;
            return y+dy;
        }

        public int nextZ(int x, int y, int z, int dir)
        {
            int dz = (0==dir/4)?-1:1;
            return z+dz;
        }

        public int numDirections(int x, int y, int z)
        {
            return 8;
        }
    }

    public static class Topology2
        implements Topology
    {
        Topology1 diagonals = new Topology1();

        public final static int[][] deltaList = {
            {1,0,0},
            {-1,0,0},
            {0,1,0},
            {0,-1,0},
            {0,0,-2},
            {0,0,2},
        };

        public int nextX(int x, int y, int z, int dir)
        {
            if (dir<diagonals.numDirections(x,y,z))
                return diagonals.nextX(x,y,z,dir);

            dir -= diagonals.numDirections(x,y,z);

            return x+ deltaList[dir][0];
        }

        public int nextY(int x, int y, int z, int dir)
        {
            if (dir<diagonals.numDirections(x,y,z))
                return diagonals.nextY(x, y, z, dir);

            dir -= diagonals.numDirections(x,y,z);

            return y+ deltaList[dir][1];
        }

        public int nextZ(int x, int y, int z, int dir)
        {
            if (dir<diagonals.numDirections(x,y,z))
                return diagonals.nextZ(x, y, z, dir);

            dir -= diagonals.numDirections(x,y,z);

            return z+ deltaList[dir][2];
        }

        public int numDirections(int x, int y, int z)
        {
            return diagonals.numDirections(x,y,z) + deltaList.length;
        }
    }

    private static class Picker
    {
        private int sx;
        private final int sy;
        private final int sz;

        Picker2[] nested;

        public Picker(int sx, int sy, int sz)
        {

            this.sx = sx;
            this.sy = sy;
            this.sz = sz;

            nested = new Picker2[5];
            nested[0] = new Picker2(0, 0, sz, sx, sy, 1);
            nested[1] = new Picker2(0, 0, 0, sx, 1, sz);
            nested[2] = new Picker2(0, sy, 0, sx, 1, sz);
            nested[3] = new Picker2(0, 0, 0, 1, sy, sz);
            nested[4] = new Picker2(sx, 0, 0, 1, sy, sz);
        }

        public int candidates()
        {
            int rval=0;
            for (Picker2 x : nested) {
                rval += x.candidates();
            }
            return rval;
        }

        public int[] pick(int i)
        {
            for (Picker2 x : nested) {
                int n = x.candidates();
                if (i< n)
                    return x.pick(i);
                i-= n;
            }
            throw new IllegalArgumentException();
        }
    }

    public static class Picker2
    {

        private final int sx;
        private final int sy;
        private final int sz;
        private final int x0, y0, z0;

        public Picker2(int x0, int y0, int z0, int sx, int sy, int sz)
        {
            this.x0 = x0;
            this.y0 = y0;
            this.z0 = z0;
            this.sx = sx;
            this.sy = sy;
            this.sz = sz;
        }

        public int candidates()
        {
            return sx*sy*sz;
        }

        public int[] pick(int i)
        {
            int x = i%sx;
            i/= sx;
            int y = i%sy;
            i/= sy;
            return new int[] {x+x0,y+y0,i+z0};
        }
    }
}
