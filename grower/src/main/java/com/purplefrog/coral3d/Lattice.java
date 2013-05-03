package com.purplefrog.coral3d;

/**
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 5/2/13
 * Time: 5:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class Lattice
{
    int minx, miny, minz;
    int sx, sy, sz;
    boolean[] lumps;

    public Lattice(int minx, int miny, int minz, int sx, int sy, int sz)
    {
        this.minx = minx;
        this.miny = miny;
        this.minz = minz;
        this.sx = sx;
        this.sy = sy;
        this.sz = sz;

        lumps = new boolean[sx*sy*sz];
    }

    public Lattice()
    {
        this(-4, -4, 0, 8, 8, 8);
    }

    public synchronized void addLump(int x, int y, int z)
    {
        if (! inBounds(x, y, z)) {
            int x0 = Math.min(minx, x - 4);
            int y0 = Math.min(miny, y-4);
            int z0 = Math.min(minz, z-4);
            int sx2 = Math.max(x + 4, minx + sx) - x0;
            int sy2 = Math.max(y + 4, miny + sy)-y0;
            int sz2 = Math.max(z+4, minz+sz)-z0;
            enlarge(x0, y0, z0, sx2, sy2, sz2);
        }

        lumps[pos_(x,y,z)] = true;
    }

    public boolean inBounds(int x, int y, int z)
    {
        return !( x<minx || y<miny || z<minz
            || x>= minx+sx || y >= miny+sy || z>= minz+sz);
    }

    private synchronized void enlarge(int x0, int y0, int z0, int sx2, int sy2, int sz2)
    {
        boolean[] newLumps = new boolean[sx2*sy2*sz2];

        for (int x=x0; x<x0+sx2; x++) {
            for (int y=y0; y<y0+sy2; y++) {
                for (int z=z0; z<z0+sz2; z++) {
                    int p2 = ((z - z0) * sy2 + y - y0) * sx2 + x - x0;
                    if (inBounds(x,y,z)) {
                        newLumps[p2] = lumps[pos_(x,y,z)];
                    } else {
                        newLumps[p2] = false;
                    }
                }
            }
        }

        lumps = newLumps;
        minx = x0;
        miny = y0;
        minz = z0;
        sx = sx2;
        sy = sy2;
        sz = sz2;
    }

    private int pos_(int x, int y, int z)
    {
        return ((z-minz)*sy +y-miny)*sx +x-minx;
    }

    public float[] coordsFor(int x, int y, int z)
    {
        if (0 == (z&1)) {
            return new float[] {x*4, y*4, z*2};
        } else {
            return new float[] {x*4+2, y*4+2, z*2};
        }
    }

    public int count()
    {
        int rval=0;
        for (boolean lump : lumps) {
            if (lump)
                rval++;
        }
        return rval;
    }

    public int afterMaxx()
    {
        return minx + sx;
    }

    public int afterMaxY()
    {
        return miny + sy;
    }

    public int afterMaxZ()
    {
        return minz + sz;
    }

    public boolean hasLumpAt(int x, int y, int z)
    {
        if (inBounds(x,y,z))
            return lumps[pos_(x, y, z)];
        else
            return false;
    }
}
