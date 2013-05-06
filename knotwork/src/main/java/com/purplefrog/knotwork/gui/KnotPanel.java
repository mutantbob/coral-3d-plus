package com.purplefrog.knotwork.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

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

    protected NodeGrid nodes;


    public KnotPanel(int nColumns, int nRows)
    {
        this.nColumns = nColumns;
        this.nRows = nRows;
        nodes = new NodeGrid(nColumns, nRows);

        nodes.set(6,5, new DoubleCurve(0,1));
        nodes.set(6,7, new DoubleCurve(0,1));
        nodes.set(5,6, new DoubleCurve(1,0));
        nodes.set(7,6, new DoubleCurve(1,0));
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
        int xOver = size.width - size.height * (nColumns +3)/(nRows +3);
        int yOver = size.height - size.width * (nRows +3)/(nColumns +3);

        int cellSize = Math.min(size.height / (nRows + 3), size.width / (nColumns + 3));

        int x0 = (size.width - nColumns *cellSize + 2*cellSize/2) /2;
        int y0 = (size.height - nRows *cellSize + 3*cellSize/2) /2;

        Graphics2D g2 = (Graphics2D) g2_.create();
        g2.translate(x0, y0);

        Graphics2D gRed = (Graphics2D) g2.create();

        gRed.setColor(new Color(150,0,0, 128));
        gRed.setStroke(new BasicStroke((float) Math.max(1, cellSize / 10.0)));

        {
            GeneralPath over = new GeneralPath();
            GeneralPath under = new GeneralPath();
            GeneralPath overHalo = new GeneralPath();
            GeneralPath underHalo = new GeneralPath();
            renderNodes(cellSize, over, overHalo, under, underHalo, new BasicCross.KnotParams(5.0/32, 7.0/32));

            Graphics2D gYellow = (Graphics2D) g2.create();
            gYellow.setColor(new Color(255, 255, 0));
            Graphics2D gBlack = (Graphics2D) g2.create();
            gBlack.setColor(new Color(0,0,0));
            gBlack.fill(underHalo);
            gYellow.fill(under);
            gBlack.fill(overHalo);
            gYellow.fill(over);
        }

        drawPointSquares(cellSize, g2);

        gRed.draw(makeCrosses(cellSize));
    }

    private void drawPointSquares(int cellSize, Graphics2D g2)
    {
        for (int v=0; v< nRows; v++) {
            for (int u=0; u< nColumns; u++) {
                int x = u * cellSize;
                int y = v * cellSize ;
                int bacon = Math.max(1, cellSize/10);
                g2.fillRect(x-bacon/2, y-bacon/2, bacon, bacon);

            }
        }
    }

    public void renderNodes(int cellSize, GeneralPath over, GeneralPath overHalo, GeneralPath under, GeneralPath underHalo, BasicCross.KnotParams knotParams)
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

}
