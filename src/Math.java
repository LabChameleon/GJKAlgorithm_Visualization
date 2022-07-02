import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Julian on 23.04.2015.
 */
public class Math
{
    public static double dotProduct(Point p1, Point p2)
    {
        return p1.getX() * p2.getX() + p1.getY() * p2.getY();
    }

    public static Point addPoints(Point p1, Point p2)
    {
        Point point = new Point();
        point.setLocation(p1.getX() + p2.getX(), p1.getY() + p2.getY());
        return point;
    }

    public static Point substractPoints(Point p1, Point p2)
    {
        Point point = new Point();
        point.setLocation(p1.getX() - p2.getX(), p1.getY() - p2.getY());
        return point;
    }

    public static Point invertVector(Point p1)
    {
        return substractPoints(new Point(0,0), p1);
    }

    public static Point getNormal(Point vec)
    {
        Point p1 = new Point();
        p1.setLocation(vec.getY(), -vec.getX());
        return p1;
    }

    public static ArrayList getConvexHull(Polygon polygon)
    {
        ArrayList <Point> newPolygon = new ArrayList();

        int[] xPoints = polygon.xpoints;
        int[] yPoints = polygon.ypoints;

        Point leftestPoint = new Point(xPoints[0], yPoints[0]);
        Point rightestPoint = new Point(xPoints[0], yPoints[0]);

        for(int i = 0; i < polygon.npoints; i++)
        {
            if(xPoints[i] < leftestPoint.x)
            {
                leftestPoint = new Point(xPoints[i], yPoints[i]);
            }

            if(xPoints[i] > rightestPoint.x)
            {
                rightestPoint = new Point(xPoints[i], yPoints[i]);
            }
        }

        for(int i = 0; i < polygon.npoints; i++)
        {
            newPolygon.add(new Point(xPoints[i], yPoints[i]));
        }

        ArrayList convexHull = new ArrayList();

        convexHull.add(leftestPoint);
        convexHull.add(rightestPoint);

        convexHull.addAll(getPoints(leftestPoint, rightestPoint, newPolygon));
        convexHull.addAll(getPoints(rightestPoint, leftestPoint, newPolygon));

        convexHull = sortPolygon(convexHull);

        return convexHull;
    }

    public static ArrayList getConvexHull(ArrayList <Point> polygon)
    {
        Point leftestPoint = polygon.get(0);
        Point rightestPoint = polygon.get(0);

        for(int i = 0; i < polygon.size(); i++)
        {
            if(polygon.get(i).x < leftestPoint.x)
            {
                leftestPoint = new Point(polygon.get(i));
            }

            if(polygon.get(i).x > rightestPoint.x)
            {
                rightestPoint = new Point(polygon.get(i));
            }
        }

        ArrayList convexHull = new ArrayList();

        convexHull.add(leftestPoint);
        convexHull.add(rightestPoint);

        convexHull.addAll(getPoints(leftestPoint, rightestPoint, polygon));
        convexHull.addAll(getPoints(rightestPoint, leftestPoint, polygon));

        convexHull = sortPolygon(convexHull);

        return convexHull;
    }

    private static ArrayList getPoints(Point pointA, Point pointB, ArrayList <Point> polygon)
    {
        Point vecAB = substractPoints(pointA, pointB);
        Point normalVecAB = getNormal(vecAB);
        ArrayList <Point> newPolygon = new ArrayList();

        Point vecLocal = substractPoints(polygon.get(0), pointA);
        double biggestScalar = dotProduct(vecLocal, normalVecAB);
        Point biggestScalarPoint = new Point(polygon.get(0));

        for(int i = 1; i < polygon.size(); i++)
        {
            vecLocal = substractPoints(polygon.get(i), pointA);

            double temp = dotProduct(vecLocal, normalVecAB);

            if(temp > 0 && temp > biggestScalar)
            {
                biggestScalar = temp;
                biggestScalarPoint = polygon.get(i);
                newPolygon.add(biggestScalarPoint);
            }
            else if(temp > 0)
            {
                newPolygon.add(polygon.get(i));
            }
        }

        ArrayList <Point> convexHull = new ArrayList();

        if(!newPolygon.isEmpty())
        {
            convexHull.addAll(getPoints(biggestScalarPoint, pointB, newPolygon));
            convexHull.addAll(getPoints(pointA, biggestScalarPoint, newPolygon));
        }

        if(biggestScalar > 0)
        {
            convexHull.add(biggestScalarPoint);
        }

        return convexHull;
    }

    private static ArrayList sortPolygon(ArrayList <Point> polygon)
    {
        Point maxPoint = polygon.get(0);
        Point minPoint = polygon.get(0);

        for(int i = 1; i < polygon.size(); i++)
        {
            if(polygon.get(i).x > maxPoint.x)
            {
                maxPoint = polygon.get(i);
            }

            if(polygon.get(i).x < minPoint.x)
            {
                minPoint = polygon.get(i);
            }
        }

        Point vecAB = substractPoints(maxPoint, minPoint);
        Point normalVecAB = getNormal(vecAB);
        ArrayList <Point> aboveList = new ArrayList();
        ArrayList <Point> belowList = new ArrayList();

        for(int i = 0; i < polygon.size(); i++)
        {
            Point vecLocal = substractPoints(polygon.get(i), minPoint);

            double temp = dotProduct(vecLocal, normalVecAB);

            if(temp > 0)
            {
                belowList.add(polygon.get(i));
            }
            else if(temp < 0)
            {
                aboveList.add(polygon.get(i));
            }
        }

        aboveList.sort(new Comparator<Point>()
        {
            @Override
            public int compare(Point p1, Point p2)
            {
                if(p1.x < p2.x)
                    return -1;
                else if (p1.x==p2.x)
                    return 0;
                else
                    return 1;
            }
        });

        belowList.sort(new Comparator<Point>()
        {
            @Override
            public int compare(Point p1, Point p2)
            {
                if(p1.x > p2.x)
                    return -1;
                else if (p1.x==p2.x)
                    return 0;
                else
                    return 1;
            }
        });

        aboveList.add(0, minPoint);
        aboveList.add(maxPoint);
        aboveList.addAll(belowList);
        return aboveList;
    }

    public static ArrayList<Point> getDifferencePoints(ArrayList <Point> setA, ArrayList <Point> setB)
    {
        ArrayList <Point> minkowskiSet = new ArrayList();

        for (int i = 0; i < setA.size(); i++)
        {
            for (int j = 0; j < setB.size(); j++)
            {
                Point difference = Math.substractPoints(setA.get(i), setB.get(j));
                minkowskiSet.add(new Point(difference.x, difference.y));
            }
        }

        return minkowskiSet;
    }
}
