import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by Julian on 11.03.2015.
 */
public class MainFrame
{

    private JFrame mainFrame = new JFrame();
    private JPanel buttonPanel = new JPanel(null);
    private DrawPanel drawPanel = new DrawPanel();
    private ConvexPolygon convexPolygonA;
    private ConvexPolygon convexPolygonB;
    private ConvexPolygon minkowskiDifference;
    private CollisionResolver collisionResolver = new CollisionResolver(this);
    private startAnimation tempAnimation;
    private Polygon polySimplex = new Polygon();
    private boolean animateAble = true;
    JLabel areColliding = new JLabel("", SwingConstants.CENTER);

    public MainFrame()
    {
        mainFrame.setResizable(false);
        mainFrame.setTitle("GJK-Algorithm");
        mainFrame.setPreferredSize(new Dimension(900, 700));
        mainFrame.getContentPane().setLayout(null);
        mainFrame.addWindowListener(new TestWindowListener());

        initFrame();

        mainFrame.setLocationRelativeTo(null);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    public void initFrame()
    {
        buttonPanel.setBounds(0,0,200,700);
        drawPanel.setBounds(200,5,689,663);

        String[] chooseAbleStrings = { "Zuffalspolygon", "Kreis", "8-Eck", "7-Eck", "6-Eck" , "5-Eck", "4-Eck", "3-Eck" };

        final JComboBox PolygonChooserA = new JComboBox(chooseAbleStrings);
        PolygonChooserA.setBounds(12,25,175,35);

        final JComboBox PolygonChooserB = new JComboBox(chooseAbleStrings);
        PolygonChooserB.setBounds(12,70,175,35);

        buttonPanel.add(PolygonChooserA);
        buttonPanel.add(PolygonChooserB);

        JButton createScene = new JButton("Szene Erstellen");
        createScene.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if(tempAnimation != null)
                    animateAble = tempAnimation.isDone();

                if(animateAble)
                {
                    switch (PolygonChooserA.getSelectedIndex())
                    {
                        case 0:
                            convexPolygonA = new ConvexPolygon(PolygonCreator.createRandomPolygon(), true);
                            break;
                        case 1:
                            convexPolygonA = new ConvexPolygon(PolygonCreator.createShape(500), true);
                            break;
                        case 2:
                            convexPolygonA = new ConvexPolygon(PolygonCreator.createShape(8), true);
                            break;
                        case 3:
                            convexPolygonA = new ConvexPolygon(PolygonCreator.createShape(7), true);
                            break;
                        case 4:
                            convexPolygonA = new ConvexPolygon(PolygonCreator.createShape(6), true);
                            break;
                        case 5:
                            convexPolygonA = new ConvexPolygon(PolygonCreator.createShape(5), true);
                            break;
                        case 6:
                            convexPolygonA = new ConvexPolygon(PolygonCreator.createShape(4), true);
                            break;
                        case 7:
                            convexPolygonA = new ConvexPolygon(PolygonCreator.createShape(3), true);
                            break;
                    }

                    switch (PolygonChooserB.getSelectedIndex())
                    {
                        case 0:
                            convexPolygonB = new ConvexPolygon(PolygonCreator.createRandomPolygon(), true);
                            break;
                        case 1:
                            convexPolygonB = new ConvexPolygon(PolygonCreator.createShape(500), true);
                            break;
                        case 2:
                            convexPolygonB = new ConvexPolygon(PolygonCreator.createShape(8), true);
                            break;
                        case 3:
                            convexPolygonB = new ConvexPolygon(PolygonCreator.createShape(7), true);
                            break;
                        case 4:
                            convexPolygonB = new ConvexPolygon(PolygonCreator.createShape(6), true);
                            break;
                        case 5:
                            convexPolygonB = new ConvexPolygon(PolygonCreator.createShape(5), true);
                            break;
                        case 6:
                            convexPolygonB = new ConvexPolygon(PolygonCreator.createShape(4), true);
                            break;
                        case 7:
                            convexPolygonB = new ConvexPolygon(PolygonCreator.createShape(3), true);
                            break;
                    }

                    minkowskiDifference = new ConvexPolygon(Math.getDifferencePoints(convexPolygonA.getPointList(), convexPolygonB.getPointList()), false);
                    mainFrame.repaint();
                }
            }
        });
        createScene.setBounds(12, 115, 175, 35);
        buttonPanel.add(createScene);

        JButton searchForCollision = new JButton("Auf Kollision Testen");
        searchForCollision.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if(tempAnimation != null)
                    animateAble = tempAnimation.isDone();

                if(animateAble)
                {
                    tempAnimation = new startAnimation();
                    tempAnimation.execute();
                }
            }
        });
        searchForCollision.setBounds(12, 200, 175, 35);
        buttonPanel.add(searchForCollision);

        Font font = new Font("arial", Font.BOLD, 17 );

        areColliding.setOpaque(true);
        areColliding.setBounds(12, 250, 175, 35);
        areColliding.setFont(font);
        buttonPanel.add(areColliding);

        mainFrame.add(buttonPanel);
        mainFrame.add(drawPanel);
    }

    public void repaint()
    {
        mainFrame.repaint();
    }

    public void setSiplex(ArrayList<Point> simplex)
    {
        polySimplex.reset();

        for (int i = 0; i < simplex.size(); i++)
        {
            polySimplex.addPoint(-simplex.get(i).x, -simplex.get(i).y);
        }
    }


    class TestWindowListener extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {
            e.getWindow().dispose();
            System.exit(0);
        }
    }

    class DrawPanel extends JPanel
    {
        private Point oldPosition;
        private boolean selectAbleA = false;
        private boolean selectAbleB = false;

        public DrawPanel()
        {
            super(null);
            this.setDoubleBuffered(true);

            this.addMouseMotionListener(new MouseMotionAdapter()
            {
                public void mouseMoved(MouseEvent e)
                {
                    if (!SwingUtilities.isLeftMouseButton(e))
                    {
                        oldPosition = e.getPoint();
                    }
                }

                public void mouseDragged(MouseEvent e)
                {
                    if (SwingUtilities.isLeftMouseButton(e))
                    {
                        if(tempAnimation != null)
                            animateAble = tempAnimation.isDone();

                        if (selectAbleA && animateAble)
                        {
                            convexPolygonA.move(Math.substractPoints(e.getPoint(), oldPosition));
                            minkowskiDifference = new ConvexPolygon(Math.getDifferencePoints(convexPolygonA.getPointList(), convexPolygonB.getPointList()), false);
                        }
                        else if (selectAbleB  && animateAble)
                        {
                            convexPolygonB.move(Math.substractPoints(e.getPoint(), oldPosition));
                            minkowskiDifference = new ConvexPolygon(Math.getDifferencePoints(convexPolygonA.getPointList(), convexPolygonB.getPointList()), false);
                        }

                        oldPosition = e.getPoint();
                        mainFrame.repaint();
                    }
                }
            });

            this.addMouseListener(new MouseAdapter()
            {
                public void mousePressed(MouseEvent e)
                {
                    if(convexPolygonB != null)
                    {
                        if (SwingUtilities.isLeftMouseButton(e))
                        {
                            if (convexPolygonA.contains(Math.substractPoints(e.getPoint(), new Point(344, 331))))
                            {
                                selectAbleA = true;
                            }

                            if (convexPolygonB.contains(Math.substractPoints(e.getPoint(), new Point(344, 331))))
                            {
                                selectAbleB = true;
                            }
                        }
                    }
                }
            });

            this.addMouseListener(new MouseAdapter()
            {
                public void mouseReleased(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e))
                    {
                        selectAbleB = false;
                        selectAbleA = false;
                    }
                }
            });
        }

        public void paintComponent(Graphics g)
        {
            Graphics2D g2 = (Graphics2D) g;

            g2.setColor(Color.BLACK);
            g2.fillRect(-1,-1, 1000, 1000);

            g2.translate(344, 331);

            RenderingHints rh = new RenderingHints(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHints(rh);

            g2.setColor(Color.green);
            if(minkowskiDifference != null)
                g2.drawPolygon(minkowskiDifference.getPolygon());

            g2.setColor(Color.yellow);
            if(convexPolygonA != null)
                g2.drawPolygon(convexPolygonA.getPolygon());

            g2.setColor(new Color(50, 149, 255));
            if(convexPolygonB != null)
                g2.drawPolygon(convexPolygonB.getPolygon());

            g2.setColor(Color.white);
            g2.drawLine(-5000, 0, 5000, 0);
            g2.drawLine(0, -5000, 0, 5000);

            g2.setColor(Color.RED);
            g2.drawPolygon(polySimplex);
        }
    }

    class startAnimation extends SwingWorker
    {
        protected Integer doInBackground() throws Exception
        {
            if(collisionResolver.GJK(convexPolygonB.getPointList(), convexPolygonA.getPointList()))
            {
                areColliding.setText("Kollision");
                areColliding.setBackground(Color.GREEN);
            }
            else
            {
                areColliding.setText("Keine Kollision");
                areColliding.setBackground(Color.RED);
            }

            try {
                Thread.sleep(1000);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            areColliding.setText("");
            areColliding.setBackground(Color.white);

            polySimplex.reset();
            mainFrame.repaint();

            return 0;
        }
    }
}
