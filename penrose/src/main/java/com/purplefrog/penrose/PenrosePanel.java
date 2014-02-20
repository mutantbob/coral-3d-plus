package com.purplefrog.penrose;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

/**
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 5/20/13
 * Time: 2:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class PenrosePanel
    extends JComponent
{
    PenroseRender pr;
    public PenrosePanel(int levels)
    {
        if (levels<17) {
            pr = new PenroseRenderVector(levels, PenroseRenderVector.decompose(levels, new TriangleA()));
        } else {
            pr = new PenroseRenderImage(levels, new TriangleA());
        }

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


        JPanel p0 = levelsArray(4, 3, 9, new ComputeContents() {
            public JComponent computeContents(int l)
            {
                return new PenrosePanel(l);
            }
        });
        
        fr.getContentPane().add(p0);

        fr.pack();
        fr.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        fr.setVisible(true);
    }

    public static JPanel levelsArray(int w, int h, int startLevel, ComputeContents gen)
    {
        JPanel p0 = new JPanel(new GridBagLayout());

        int l= startLevel;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx=1;
        gbc.weighty=1;
        for (int r=0; r<h; r++) {
            for (int c=0; c<w; c++, l++) {
                JComponent pp = gen.computeContents(l);
                gbc.gridx = c;
                gbc.gridy = r;
                p0.add(pp, gbc);
            }
        }
        return p0;
    }

    public interface ComputeContents
    {
        JComponent computeContents(int l);
    }
}
