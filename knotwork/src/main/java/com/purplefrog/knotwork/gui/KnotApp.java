package com.purplefrog.knotwork.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 5/6/13
 * Time: 10:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class KnotApp
    extends JPanel
{

    protected final KnotPanel graph;

    public KnotApp()
    {
        super(new BorderLayout());
        graph = new KnotPanel(25, 25);
        add(graph);
    }

    public static void main(String[] argv)
    {
        JFrame fr = new JFrame("knot editor");

//        fr.setFocusable(true);

        KnotApp x = new KnotApp();
        fr.getContentPane().add(x);

        fr.pack();

        fr.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        x.requestFocusInWindow();

        fr.setVisible(true);

    }
}
