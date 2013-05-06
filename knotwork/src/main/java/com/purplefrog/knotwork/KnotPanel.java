package com.purplefrog.knotwork;

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
    private final int xSize;
    private final int ySize;



    public KnotPanel(int xSize, int ySize)
    {
        this.xSize = xSize;
        this.ySize = ySize;
    }

    @Override
    public Dimension getPreferredSize()
    {
        int cellSize = Math.min(900/(ySize+3), 1200/(xSize+3));

        cellSize = Math.min(100, cellSize);

        return new Dimension(cellSize*xSize, cellSize*ySize);
    }

    @Override
    protected void paintComponent(Graphics g_)
    {
        super.paintComponent(g_);

        Graphics2D g2 = (Graphics2D) g_;

        Dimension size = getSize();
        int xOver = size.width - size.height * (xSize+3)/(ySize+3);
        int yOver = size.height - size.width * (ySize+3)/(xSize+3);

        int cellSize = Math.min(size.height / (ySize + 3), size.width / (xSize + 3));

        int x0 = (size.width -xSize*cellSize + 2*cellSize/2) /2;
        int y0 = (size.height -ySize*cellSize + 3*cellSize/2) /2;

        Graphics2D gRed = (Graphics2D) g2.create();
        gRed.translate(x0,y0);
        gRed.setColor(new Color(150,0,0, 128));
        gRed.setStroke(new BasicStroke((float) Math.max(1, cellSize/10.0)));

        for (int v=0; v<ySize; v++) {
            for (int u=0; u<xSize; u++) {
                int x = u * cellSize + x0;
                int y = v * cellSize + y0;
                int bacon = Math.max(1, cellSize/10);
                g2.fillRect(x-bacon/2, y-bacon/2, bacon, bacon);

            }
        }

        gRed.draw(makeCrosses(cellSize));
    }

    private GeneralPath makeCrosses(int cellSize)
    {
        GeneralPath crosses = new GeneralPath();

        for (int v=0; v<ySize; v++) {
            for (int u=0; u<xSize; u++) {
                if (((u+v)&1)!=0) {
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
