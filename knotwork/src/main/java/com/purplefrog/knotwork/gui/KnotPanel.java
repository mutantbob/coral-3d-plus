package com.purplefrog.knotwork.gui;

import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 5/6/13
 * Time: 10:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class KnotPanel
    extends JComponent
{
    private final int nColumns;
    private final int nRows;
    protected final BasicCross.KnotParams lineThickness = new BasicCross.KnotParams(5.0 / 32, 7.0 / 32);

    protected NodeGrid nodes;

    Point nodeCursor=null;

    public KnotPanel(int nColumns_, int nRows_)
    {
        this.nColumns = nColumns_;
        this.nRows = nRows_;
        nodes = new NodeGrid(nColumns, nRows);

        if (false) {
            nodes.set(6,5, new DoubleCurve(true));
            nodes.set(6,7, new DoubleCurve(true));
            nodes.set(5,6, new DoubleCurve(false));
            nodes.set(7,6, new DoubleCurve(false));
        } else {

            nodes.set(6,5, new DoubleCurve(false));
            nodes.set(6,7, new DoubleCurve(false));
            nodes.set(5,6, new DoubleCurve(true));
            nodes.set(7,6, new DoubleCurve(true));
        }

        setFocusable(true);

        addMouseMotionListener(new MouseMotionAdapter()
        {
            @Override
            public void mouseMoved(MouseEvent e)
            {
                if (!isFocusOwner())
                    requestFocus(); // XXX work around bugs in java, or fvwm, not sure which

                PaintParams p = new PaintParams();

                int u = (int) Math.round((e.getX()- p.x0)/(double) p.cellSize);
                int v  = (int) Math.round((e.getY()- p.y0)/(double) p.cellSize);
//                System.out.println(e.getX()+","+e.getY()+" => "+u+","+v);

                if (u<0 || u >= nColumns
                    || v<0 || v >=nRows)
                    return; //never mind

                if (nodeCursor != null) {
                    scheduleCellRepaint(p.cellSize, p.x0, p.y0, nodeCursor.x, nodeCursor.y);
                }
                nodeCursor = new Point(u, v);
                scheduleCellRepaint(p, u, v);
            }
        });

        addKeyListener(new KeyHandler());
    }


    private void scheduleCellRepaint(PaintParams p, int u, int v)
    {
        scheduleCellRepaint(p.cellSize, p.x0, p.y0, u, v);
    }

    private void scheduleCellRepaint(int u, int v)
    {
        scheduleCellRepaint(new PaintParams(), u, v);
    }

    private void scheduleCellRepaint(int cellSize, int x0, int y0, int u, int v)
    {
        repaint(50, x0 + cellSize * u - cellSize, y0 + cellSize * v - cellSize, cellSize * 2, cellSize * 2);
    }

    @Override
    public Dimension getPreferredSize()
    {
        int cellSize = Math.min(900/(nRows +3), 1200/(nColumns +3));

        cellSize = Math.min(100, cellSize);

        return new Dimension(cellSize* nColumns, cellSize* nRows);
    }

    @Override
    protected void paintComponent(Graphics g_)
    {
        super.paintComponent(g_);

        Graphics2D g2_ = (Graphics2D) g_;

        Dimension size = getSize();

        int cellSize = cellSize(size);

        int x0 = (size.width - nColumns *cellSize + 2*cellSize/2) /2;
        int y0 = (size.height - nRows *cellSize + 3*cellSize/2) /2;

        Graphics2D g2 = (Graphics2D) g2_.create();
        g2.translate(x0, y0);

        Graphics2D gRed = (Graphics2D) g2.create();
        gRed.setColor(new Color(150,0,0, 128));
        gRed.setStroke(new BasicStroke((float) Math.max(1, cellSize / 10.0)));

        Graphics2D gCursor = (Graphics2D) g2.create();
        gCursor.setColor(new Color(0,255,255));

        renderNodes(cellSize, g2);

        drawPointSquares(cellSize, g2, gCursor);

        gRed.draw(makeCrosses(cellSize));
    }

    private void renderNodes(int cellSize, Graphics2D g2)
    {
        GeneralPath over = new GeneralPath();
        GeneralPath under = new GeneralPath();
        GeneralPath overHalo = new GeneralPath();
        GeneralPath underHalo = new GeneralPath();
        collectNodeShapes(cellSize, over, overHalo, under, underHalo, lineThickness);

        Graphics2D gYellow = (Graphics2D) g2.create();
        gYellow.setColor(new Color(255, 255, 0));
        Graphics2D gBlack = (Graphics2D) g2.create();
        gBlack.setColor(new Color(0,0,0));
        gBlack.fill(underHalo);
        gYellow.fill(under);
        gBlack.fill(overHalo);
        gYellow.fill(over);
    }

    private int cellSize(Dimension size)
    {
        return Math.min(size.height / (nRows + 3), size.width / (nColumns + 3));
    }

    private void drawPointSquares(int cellSize, Graphics2D normal, Graphics2D cursor)
    {
        for (int v=0; v< nRows; v++) {
            for (int u=0; u< nColumns; u++) {
                int x = u * cellSize;
                int y = v * cellSize ;
                int bacon = Math.max(1, cellSize/10);
                boolean onCursor = nodeCursor != null && u == nodeCursor.x && v == nodeCursor.y;
                Graphics2D g = onCursor ? cursor : normal;
                g.fillRect(x-bacon/2, y-bacon/2, bacon, bacon);

            }
        }
    }

    public void collectNodeShapes(int cellSize, GeneralPath over, GeneralPath overHalo, GeneralPath under, GeneralPath underHalo, BasicCross.KnotParams knotParams)
    {
        for (int v=0; v< nRows; v++) {
            for (int u=0; u< nColumns; u++) {
                if (NodeGrid.echidna(u, v)) {
                    NodeShape n1 = nodes.get(u,v);
                    if (null != n1)
                        n1.apply(over, overHalo, under, underHalo,
                            (u-0.5)*cellSize, (u+0.5)*cellSize, (v-0.5)*cellSize, (v+0.5)*cellSize, knotParams);
                }
            }
        }
    }

    private GeneralPath makeCrosses(int cellSize)
    {
        GeneralPath crosses = new GeneralPath();

        for (int v=0; v< nRows; v++) {
            for (int u=0; u< nColumns; u++) {
                if (NodeGrid.echidna(u, v)) {
                    int x = u * cellSize;
                    int y = v * cellSize;
                    crosses.append(new Line2D.Double(x - cellSize / 4.0, y - cellSize / 4.0, x + cellSize / 4.0, y + cellSize / 4.0), false);
                    crosses.append(new Line2D.Double(x - cellSize / 4.0, y + cellSize / 4.0, x + cellSize / 4.0, y - cellSize / 4.0), false);
                }
            }
        }
        return crosses;
    }


    public void saveToImage()
    {
        int scale = 64;

        BufferedImage bi = new BufferedImage((nColumns)*scale, (nRows)*scale, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = bi.createGraphics();

        g2.translate(scale/2, scale/2);

        renderNodes(scale, g2);

        try {
            File of = new File("/tmp/knot.png");
            System.out.println("saving to "+of);
            ImageIO.write(bi, "PNG", of);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class PaintParams
    {
        public int cellSize;
        public int x0;
        public int y0;

        public PaintParams()
        {

            Dimension size = getSize();
            cellSize = cellSize(size);

            x0 = (size.width - nColumns * cellSize + 2 * cellSize / 2) / 2;
            y0 = (size.height - nRows * cellSize + 3 * cellSize / 2) / 2;
        }

    }

    public class KeyHandler
        extends KeyAdapter
    {
        @Override
        public void keyTyped(KeyEvent e)
        {
            char ch = e.getKeyChar();

            if (ch=='s') {
                saveToImage();
                return;
            }

            if (nodeCursor == null)
                return;

            int u = nodeCursor.x;
            int v = nodeCursor.y;

            if (nodes.echidna(u, v)) {
                NodeShape n2=null;
                NodeShape n1 = nodes.get(u, v);
                if (ch == 'c') {
                    if (n1 instanceof Corner) {
                        Corner corner = (Corner) n1;
                        n2 = new Corner(-corner.dy, corner.dx);
                    } else {
                        n2 = nodes.guessCorner(u,v);
                    }
                } else if (ch=='x') {
                    if (n1 instanceof BasicCross) {
                        BasicCross old = (BasicCross) n1;
                        n2 = new BasicCross(old.polarity);
                    } else {
                        n2 = new BasicCross(NodeGrid.crossPolarity(u));
                    }
                } else if (ch=='p') {

                    if (n1 instanceof PointArc) {
                        PointArc pointArc = (PointArc) n1;
                        n2 = new PointArc( (pointArc.arrangement+1) &7);
                    } else {
                        n2 = nodes.guessPointArc(u,v);
                    }

                } else if (ch=='n') {
                    nodes.set(u,v, null);
                    scheduleCellRepaint(u,v);
                }
                if (null != n2) {
                    nodes.set(u, v, n2);
                    scheduleCellRepaint(u, v);
                }
            }
        }
    }
}
