package com.purplefrog.penrose;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

/**
 * Created by thoth on 2/18/14.
 */
public class PenrosePanel2
    extends JComponent
{

    protected PenroseRender pr;

    public PenrosePanel2(int l)
    {
        pr = new PenroseUpConvert(l, new FillRectangle.CoinFlipper());
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(400, 300);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        Dimension sz = getSize();
        pr.paintPenrose(g2, sz, new ImageObserver()
        {
            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height)
            {
                if (0 != (infoflags&ImageObserver.ALLBITS))
                    repaint(1);
                return false;
            }
        });
    }

    public static void main(String[] argv)
    {
        JFrame fr = new JFrame("penrose explorer");


        JPanel p0 = PenrosePanel.levelsArray(3, 2, 0, new PenrosePanel.ComputeContents()
        {
            public JComponent computeContents(int l)
            {
                return new PenrosePanel2(l);
            }
        });

        fr.getContentPane().add(p0);

        fr.pack();
        fr.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        fr.setVisible(true);
    }

}
