package com.purplefrog.knotwork;

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
        graph = new KnotPanel(13, 13);
        add(graph);
    }

    public static void main(String[] argv)
    {
        JFrame fr = new JFrame("knot editor");

        fr.getContentPane().add(new KnotApp());

        fr.pack();
        fr.setVisible(true);
    }
}
