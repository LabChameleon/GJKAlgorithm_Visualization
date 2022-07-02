import sun.security.provider.certpath.Vertex;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Julian on 23.04.2015.
 */
public class CollisionResolver
{
    MainFrame mainFrame;

    public CollisionResolver(MainFrame mainFrame)
    {
        this.mainFrame = mainFrame;
    }

    public boolean GJK(ArrayList convexSetA, ArrayList convexSetB)
    {
        Point direction = new Point(0,1);
        ArrayList <Point> simplex = new ArrayList<Point>();

        simplex.add(Math.substractPoints(pointInDirection(convexSetA, direction), pointInDirection(convexSetB, Math.invertVector(direction))));

        mainFrame.setSiplex(simplex);
        mainFrame.repaint();

        try {
            Thread.sleep(1000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        while(true)
        {
            direction = originDirection(simplex);
            Point newPoint = Math.substractPoints(pointInDirection(convexSetA, direction), pointInDirection(convexSetB, Math.invertVector(direction)));

            if(!simplexPassedOrigin(direction, newPoint))
            {
                addToSimplex(simplex, newPoint);

                mainFrame.setSiplex(simplex);
                mainFrame.repaint();

                try {
                    Thread.sleep(1000);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

                return false;
            }
            else
            {
                addToSimplex(simplex, newPoint);

                mainFrame.setSiplex(simplex);
                mainFrame.repaint();

                try {
                    Thread.sleep(1000);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }

            if(includingOrigin(simplex))
            {
                return true;
            }
        }
    }

    private void addToSimplex(ArrayList <Point> simplex, Point newPoint)
    {
        if(simplex.size() >= 2)
        {
            Point vecAB = Math.substractPoints(simplex.get(1), simplex.get(0));
            Point vecBNewPoint = Math.substractPoints(newPoint, simplex.get(1));
            Point normalVecAB = Math.getNormal(vecAB);

            if(Math.dotProduct(normalVecAB, vecBNewPoint) > 0)
            {
                simplex.add(1,newPoint);
            }
            else
            {
                simplex.add(newPoint);
            }
        }
        else
        {
            simplex.add(newPoint);
        }
    }

   private Point originDirection(ArrayList <Point> simplex)
    {
        if(simplex.size() == 1)
        {
            return Math.substractPoints(new Point(0,0), simplex.get(0));
        }
        else if(simplex.size() == 2)
        {
            Point vecAB = Math.substractPoints(simplex.get(1), simplex.get(0));
            Point originDirection = Math.substractPoints(new Point(0, 0), simplex.get(1));
            Point normalVecAB = Math.getNormal(vecAB);

            if (Math.dotProduct(normalVecAB, originDirection) > 0)
            {
               return normalVecAB;
            }
            else
            {
                normalVecAB.setLocation(normalVecAB.getX() * (-1), normalVecAB.getY() * (-1));
                return normalVecAB;
            }
        }
        else
        {
            int j = 0;

            for(int i = 0; i < 3; i++)
            {
                if (j == 2)
                    j = 0;
                else
                    j = i + 1;

                Point vecAB = Math.substractPoints(simplex.get(j), simplex.get(i));
                Point originDirection = Math.substractPoints(new Point(0, 0), simplex.get(i));
                Point normalVecAB = Math.getNormal(vecAB);

                if (Math.dotProduct(normalVecAB, originDirection) > 0)
                {
                    if(i == 0)
                    {
                        simplex.remove(2);
                    }
                    else
                        simplex.remove(i - 1);

                    return normalVecAB;
                }
            }
        }

        return null;
    }

    private boolean includingOrigin(ArrayList <Point> simplex)
    {
        if(simplex.size() == 2)
        {
            Point vecAB = Math.substractPoints(simplex.get(1), simplex.get(0));
            Point originDirection = Math.substractPoints(new Point(0,0), simplex.get(1));
            Point normalVecAB = Math.getNormal(vecAB);

            return Math.dotProduct(normalVecAB, originDirection) == 0;
        }
        else
        {
            int j = 0;

            for(int i = 0; i < 3; i++)
            {
                if(j == 2)
                    j = 0;
                else
                    j = i+1;

                Point vecAB = Math.substractPoints(simplex.get(j), simplex.get(i));
                Point originDirection = Math.substractPoints(new Point(0, 0), simplex.get(i));
                Point normalVecAB = Math.getNormal(vecAB);

                if(Math.dotProduct(normalVecAB, originDirection) > 0)
                {
                    return false;
                }
            }
            int i = 0;
            return true;
        }
    }

    private Point pointInDirection(ArrayList <Point> convexSet, Point direction)
    {
        Point currentPointA = new Point();

        if (!convexSet.isEmpty())
        {
            currentPointA = convexSet.get(0);
            double scalar = Math.dotProduct(convexSet.get(0), direction);

            for (int i = 0; i < convexSet.size(); i++)
            {
                double temp = Math.dotProduct(convexSet.get(i), direction);

                if (scalar < temp)
                {
                    scalar = temp;
                    currentPointA = convexSet.get(i);
                }
            }

            return currentPointA;
        }
        else
        {
            return null;
        }
    }

    private boolean simplexPassedOrigin(Point direction, Point D)
    {
        Point vecDU = Math.invertVector(D);
        return Math.dotProduct(vecDU, direction) < 0;

    }
}
