import com.sun.glass.ui.*;

import java.awt.*;
import java.awt.Robot;
import java.util.ArrayList;

/**
 * Created by Julian on 25.04.2015.
 */
public class ConvexPolygon
{
    ArrayList <Point> vertices = new ArrayList();
    Polygon convexPolygon = new Polygon();

    public ConvexPolygon(ArrayList <Point> polygon, boolean isConvex)
    {
        if(isConvex)
        {
            vertices = polygon;

            for (int i = 0; i < polygon.size(); i++)
            {
                convexPolygon.addPoint(polygon.get(i).x, polygon.get(i).y);
            }
        }
        else
        {
            for (int i = 0; i < polygon.size(); i++)
            {
                convexPolygon.addPoint(polygon.get(i).x, polygon.get(i).y);
            }

            vertices = Math.getConvexHull(convexPolygon);

            convexPolygon.reset();
            for (int i = 0; i < vertices.size(); i++) {
                convexPolygon.addPoint(vertices.get(i).x, vertices.get(i).y);
            }
        }
    }

    public void move(Point movement)
    {
        ArrayList <Point> temp = new ArrayList();

        for(int i = 0; i < vertices.size(); i++)
        {
            Point currentPoint = vertices.get(i);
            Point diff = Math.addPoints(movement, currentPoint);

            if((diff.x < -344 && movement.x < 0) || (diff.x > 344 && movement.x > 0))
            {
                return;
            }
            else if((diff.y < -331 && movement.y < 0) || (diff.y > 331 && movement.y > 0))
            {
                return;
            }

            temp.add(diff);
        }

        convexPolygon.reset();
        for(int i = 0; i < temp.size(); i++)
        {
            convexPolygon.addPoint(temp.get(i).x, temp.get(i).y);
        }

        vertices = temp;
    }

    public boolean contains(Point point)
    {
        return convexPolygon.contains(point);
    }

    public Polygon getPolygon()
    {
        return convexPolygon;
    }

    public ArrayList <Point> getPointList()
    {
        return vertices;
    }
}
