package com.purplefrog.penrose;

import java.awt.*;
import java.awt.image.*;

/**
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 5/20/13
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class PenroseRenderImage
    implements PenroseRender
{
    private final int levels;
    private final RobinsonTriangle[] triangles;
    private Dimension rememberedSize=null;
    protected BufferedImage image;
    protected Thread worker;
    private Dimension inProgressSz=null;

    public PenroseRenderImage(int levels, RobinsonTriangle... triangles)
    {
        this.levels = levels;
        this.triangles = triangles;
    }

    public synchronized void paintPenrose(Graphics2D g2, final Dimension sz, final ImageObserver obs)
    {
        if (!sz.equals(rememberedSize)) {

            debugPrint("lazy paint level "+levels);
            if (! sz.equals(inProgressSz)) {
                startBackgroundWork(sz, obs);
            } else {
                debugPrint("computation for "+levels+" already in progress");
            }
        }

        if (image!=null)
            g2.drawImage(image, 0,0, sz.width, sz.height, null);
    }

    private void debugPrint(String msg)
    {
        System.out.println(msg);
    }

    protected static final Object LOCK1 = new Object();

    protected synchronized void startBackgroundWork(final Dimension sz, final ImageObserver obs)
    {
        if (worker != null)
            worker.stop();

        inProgressSz = sz;

        Runnable r = new Runnable()
        {
            public void run()
            {
                BufferedImage img;
                synchronized(LOCK1) {
                    img = new BufferedImage(sz.width, sz.height, BufferedImage.TYPE_INT_RGB);

                    PenroseRenderVector v = new PenroseRenderVector(levels, PenroseRenderVector.decompose(levels, triangles));
                    v.paintPenrose(img.createGraphics(), sz, null);
                }
                BufferedImage oldImg;
                synchronized (PenroseRenderImage.this) {
                    oldImg = image;
                    image = img;
                    rememberedSize = sz;
                }

                if (null != oldImg)
                    oldImg.flush();

                if (null != obs) {
                    debugPrint("imageUpdate "+levels);
                    obs.imageUpdate(img, ImageObserver.ALLBITS, 0, 0, sz.width,  sz.height);
                }
            }
        };

        worker = new Thread(r);
        worker.start();
    }
}
