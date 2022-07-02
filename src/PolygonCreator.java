import java.awt.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Julian on 25.04.2015.
 */
public class PolygonCreator
{
    public static ArrayList createRandomPolygon()
    {
        Random r = new Random();

        Point center = new Point(r.nextInt(600) - 300, r.nextInt(600) - 300);

        int numberOfPoints = r.nextInt(20) + 10;

        ArrayList <Point> randomPolygon = new ArrayList<Point>();

        for(int i = 0; i < numberOfPoints; i++)
        {
            int x = r.nextInt(126) - 63;
            int y = r.nextInt(126) - 63;

            randomPolygon.add(new Point(center.x + x, center.y + y));
        }

        return Math.getConvexHull(randomPolygon);
    }

    public static ArrayList createShape(int numberCorners)
    {
        ArrayList <Point> shape = new ArrayList<Point>();
        Random r = new Random();

        Point center = new Point(r.nextInt(600) -  300, r.nextInt(600) - 300);
        float radius = r.nextInt(50) + 20;

        for(int i = 0; i < numberCorners; i++)
        {
            double theta = 2.0 * 3.1415926 * i / numberCorners;

            double x = radius * java.lang.Math.cos(theta);
            double y = radius * java.lang.Math.sin(theta);

            shape.add(new Point((int)(x+center.x), (int)(y+center.y)));
        }

        int i = 0;
        return shape;

    }
}
