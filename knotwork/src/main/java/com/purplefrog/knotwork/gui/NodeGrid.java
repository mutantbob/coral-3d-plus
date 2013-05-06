package com.purplefrog.knotwork.gui;

/**
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 5/6/13
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class NodeGrid
{
    public final int nColumns;
    public final int nRows;
    protected NodeShape[] nodes;

    public NodeGrid(int nColumns, int nRows)
    {
        this.nColumns = nColumns;
        this.nRows = nRows;

        nodes = defaultNodeArray(nColumns, nRows);
    }

    public int pos_(int u, int v)
    {
        return u + v*nColumns;
    }

    public void set(int u, int v, NodeShape newShape)
    {
        nodes[pos_(u,v)] = newShape;
    }

    public NodeShape get(int u, int v)
    {
        return nodes[pos_(u,v)];
    }

    //
    //

    public static NodeShape[] defaultNodeArray(int nColumns, int nRows)
    {
        NodeShape[] rval = new NodeShape[nColumns * nRows];
        for (int u=0; u<nColumns; u++) {
            for (int v=0; v<nColumns; v++) {
                NodeShape n1 = pickShape(nColumns, nRows, u, v);
                rval[v*nColumns+u] = n1;
            }
        }
        return rval;
    }

    public static NodeShape pickShape(int nColumns, int nRows, int u, int v)
    {
        if (!echidna(u,v))
            return null;

        if (u==0) {
            if (v==1)
                return new PointArc(5);
            else if (v+2==nRows) {
                return new PointArc(6);
            } else {
                return new Corner(-1, 0);
            }
        } else if (v==0) {
            if (u==1) {
                return new PointArc(4);
            } else if (u + 2 == nColumns) {
                return new PointArc(3);
            } else {
                return new Corner(0, -1);
            }
        } else if (v + 1 == nRows) {
            if (u + 2 == nColumns) {
                return new PointArc(0);
            } else if (u == 1) {
                return new PointArc(7);
            } else {
                return new Corner(0, 1);
            }
        } else if (u + 1 == nColumns) {
            if (v + 2 == nRows) {
                return new PointArc(1);
            } else if (v==1) {
                return new PointArc(2);
            } else {
                return new Corner(1, 0);
            }
        }

        return new BasicCross((u&1)!=0);
    }

    public static boolean echidna(int u, int v)
    {
        return ((u+v)&1)!=0;
    }

}
