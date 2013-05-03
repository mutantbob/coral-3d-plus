package com.purplefrog.knotwork;

/**
 * Created with IntelliJ IDEA.
 * User: thoth
 * Date: 5/3/13
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PathData
{
    /**
     * a self-contained path@d string
     */
    String forSVG();

    boolean startsWith(double x, double y);

    /**
     * a continuation string for a path whose cursor is already at the start of this path
     * @return
     */
    String continuation();

    PathData appended(PathData other);

    double endX();
    double endY();

    PathData reversed();


    public static class Line
        implements PathData
    {

        public double[] coordPairs;

        public Line(double ... coordPairs)
        {
            if ((coordPairs.length&1) != 0)
                throw new IllegalArgumentException("coordinates must be specified in pairs; ["+coordPairs.length+"]");
            this.coordPairs = coordPairs;
        }

        public boolean startsWith(double x, double y)
        {
            return coordPairs[0] == x
                && coordPairs[1] == y;
        }

        public double endX()
        {
            return coordPairs[coordPairs.length-2];
        }

        public double endY()
        {
            return coordPairs[coordPairs.length-1];
        }

        public String forSVG()
        {
            return "M "+coordPairs[0]+","+coordPairs[1]+" "+continuation();
        }

        public String continuation()
        {
            StringBuilder rval = new StringBuilder();
            for (int i = 2; i < coordPairs.length; i+=2) {

                rval.append("L "+coordPairs[i]+","+coordPairs[i+1]+" ");
            }
            return rval.toString();
        }

        public PathData appended(PathData other)
        {
            return new Multi(this, other);
        }

        public PathData reversed()
        {
            double[] rCoords = new double[coordPairs.length];

            for (int i=0; i<coordPairs.length; i+=2) {
                int j = coordPairs.length-2-i;
                rCoords[i] = coordPairs[j];
                rCoords[i+1] = coordPairs[j+1];
            }
            return new Line(rCoords);
        }
    }

    public static class Curve
        implements PathData
    {

        private final double x0;
        private final double y0;
        private final double x1;
        private final double y1;
        private final double x2;
        private final double y2;
        private final double x3;
        private final double y3;

        public Curve(double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3)
        {
            this.x0 = x0;
            this.y0 = y0;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.x3 = x3;
            this.y3 = y3;
        }

        public String forSVG()
        {
            return "M "+x0+","+y0+" "+continuation();
        }

        public boolean startsWith(double x, double y)
        {
            return x==x0 && y==y0;
        }

        public double endX()
        {
            return x3;
        }

        public double endY()
        {
            return y3;
        }

        public String continuation()
        {
            return " C "+x1+","+y1+" "+x2+","+y2+" "+x3+","+y3;
        }

        public PathData appended(PathData other)
        {
            return new Multi(this, other);
        }

        public PathData reversed()
        {
            return new Curve(x3,y3, x2,y2, x1,y1, x0,y0);
        }
    }

    public static class Multi
        implements PathData
    {
        PathData[] parts;

        public Multi(PathData... parts)
        {
            this.parts = parts;
        }

        public String forSVG()
        {

            StringBuilder rval = new StringBuilder();

            for (int i = 0; i < parts.length; i++) {
                PathData part = parts[i];
                rval.append(i==0 ? part.forSVG()
                    :(" "+part.continuation()));
            }
            return rval.toString();
        }

        public boolean startsWith(double x, double y)
        {
            return parts[0].startsWith(x,y);
        }

        public double endX()
        {
            return parts[parts.length-1].endX();
        }

        public double endY()
        {
            return parts[parts.length-1].endY();
        }

        public String continuation()
        {
            StringBuilder rval = new StringBuilder();

            for (PathData part : parts) {
                rval.append(part.continuation());
            }
            return rval.toString();
        }

        public PathData appended(PathData other)
        {
            PathData[] newParts;
            if (other instanceof Multi) {
                Multi arg = (Multi) other;
                newParts = concat(parts, arg.parts);
            } else {
                newParts = concat(parts, other);
            }
            return new Multi(newParts);
        }

        public static PathData[] concat(PathData[] a, PathData... b)
        {
            PathData[] newParts;
            newParts = new PathData[a.length + b.length];
            System.arraycopy(a, 0, newParts, 0, a.length);
            System.arraycopy(b, 0, newParts, a.length, b.length);
            return newParts;
        }

        public PathData reversed()
        {
            PathData[] newParts = new PathData[parts.length];

            for (int i = 0; i < parts.length; i++) {
                PathData part = parts[i];
                newParts[parts.length-i-1] = part.reversed();
            }

            return new Multi(newParts);
        }
    }
}
