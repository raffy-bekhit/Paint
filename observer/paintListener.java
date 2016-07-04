package observer;

import Factory.ShapeFactory;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.undo.UndoManager;
import model.*;
import view.*;
import static view.MyPaint.shapes;

/**
 *
 * @author Raffy
 */
public class paintListener implements MouseListener {

    private static MyPaint paint = MyPaint.getInstance();
  
    int x1, x2, y1, y2, x3, y3;

    static String type = "Line";
    static Color newColor = Color.black;
    static Color fillColor = null;
    int distance1, distance2;

    int clickCounter = 0;

    static String method = "Draw";

    private int selectedShapeIndex = -1;

    public static void setMethod(String s) {
        paintListener.method = s;
    }

    public static void setFillColor(Color c) {
        fillColor = c;
    }

    public static void setColor(Color c) {
        newColor = c;
    }

    public static void setType(String type) {
        paintListener.type = type;
    }

   

    @Override
    public void mouseClicked(MouseEvent e) {
        if (method.equals("Fill")) {
            for (MyShape shape : MyPaint.shapes) {

                if (shape.contains(new Point(e.getX(), e.getY()))) {
                    shape.setFillColor(fillColor);

                }

            }
           MyPaint.Undo.push(new ArrayList<MyShape>(shapes));
            paint.repaint();
        } else if (method.equals("Delete")) {
            int i = 0;
            for (MyShape shape : MyPaint.shapes) {

                if (shape.contains(new Point(e.getX(), e.getY())) || shape.isOnLine(new Point(shape.getX1(), shape.getY1()), new Point(shape.getX2(), shape.getY2()), new Point(e.getX(), e.getY()))) {
                    {
                        shapes.remove(i);
                        break;
                    }

                }
                i++;
            }
          MyPaint.Undo.push(new ArrayList<MyShape>(shapes));
            paint.repaint();
        } //modified
        else if (method.equals("Copy")) {

            MyShape s = null;
            int i = 0;
            for (MyShape shape : MyPaint.shapes) {

                if (shape.contains(new Point(e.getX(), e.getY()))
                        || shape.isOnLine(new Point(shape.getX1(), shape.getY1()), new Point(shape.getX2(), shape.getY2()), new Point(e.getX(), e.getY()))) {
                    {
                        selectedShapeIndex = i;
                        break;
                    }

                }
                i++;
            }
            if (selectedShapeIndex > -1) {
                s = shapes.get(selectedShapeIndex);

                MyShape s2 = null;
                if (!type.equals("Triangle")) {
                    s2 = ShapeFactory.createShape(s.getType(), s.getX1() + 50, s.getY1() + 50, s.getX2() + 50, s.getY2() + 50);
                } else {
                    s2 = new MyTriangle(s.getXT1() + 50, s.getXT2() + 50, s.getXT3() + 50, s.getYT1() + 50, s.getYT2() + 50, s.getYT3() + 50);

                }
                s2.setType(s.getType());
                s2.setColor(s.getColor());
                s2.setFillColor(s.getFillColor());
                MyPaint.shapes.add(s2);
             MyPaint.Undo.push(new ArrayList<MyShape>(shapes));
                paint.repaint();
            }
            selectedShapeIndex = -1;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (method.equals("Draw") && !type.equals("Triangle")) {
            x1 = e.getX();
            y1 = e.getY();
        }
        if (method.equals("Move")) {
            x1 = e.getX();
            y1 = e.getY();
            int i = 0;
            for (MyShape shape : MyPaint.shapes) {
                if (shape.contains(new Point(x1, y1))
                        || shape.isOnLine(new Point(shape.getX1(), shape.getY1()), new Point(shape.getX2(), shape.getY2()), new Point(x1, y1))) {
                    selectedShapeIndex = i;
                    break;
                }
                i++;
            }
        } else if (method.equals("Draw") && type.equals("Triangle")) {

            switch (clickCounter) {
                case 0:
                    x1 = e.getX();
                    y1 = e.getY();
                    clickCounter++;
                    break;
                case 1:
                    x2 = e.getX();
                    y2 = e.getY();
                    clickCounter++;
                    break;
                case 2:
                    x3 = e.getX();
                    y3 = e.getY();
                    MyShape s = null;
                    s = new MyTriangle(x1, x2, x3, y1, y2, y3);
                    s.setColor(newColor);
                    s.setType("Triangle");
                    clickCounter = 0;
                    MyPaint.shapes.add(s);
                    break;
            }
            MyPaint.Undo.push(new ArrayList<MyShape>(shapes));
            paint.repaint();
        } else if (method.equals("Resize")) {//modified
            x1 = e.getX();
            y1 = e.getY();

            int i = 0;
            for (MyShape shape : MyPaint.shapes) {
                if (shape.contains(new Point(x1, y1))
                        || shape.isOnLine(new Point(shape.getX1(), shape.getY1()), new Point(shape.getX2(), shape.getY2()), new Point(x1, y1))) {
                    selectedShapeIndex = i;
                    break;

                }

                i++;
            }
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        x2 = e.getX();
        y2 = e.getY();
        if (method.equals("Draw") && !type.equals("Triangle")) {
            MyShape s = null;
            s = ShapeFactory.createShape(type, x1, y1, x2, y2);
            s.setColor(newColor);
            s.setType(type);
            MyPaint.shapes.add(s);

            MyPaint.Undo.push(new ArrayList<MyShape>(shapes));
            paint.repaint();
        } else if (method.equals("Move") && selectedShapeIndex != -1) {

            shapes.get(selectedShapeIndex).respondToMove(new Point(x1, y1), new Point(x2, y2));
            System.out.println(selectedShapeIndex);
            selectedShapeIndex = -1;
            MyPaint.Undo.push(new ArrayList<MyShape>(shapes));
            paint.repaint();
        } else if (method.equals("Resize") && selectedShapeIndex > -1) {//modified
            x2 = e.getX();
            y2 = e.getY();

            shapes.get(selectedShapeIndex).respondToResize(new Point(x1, y1), new Point(x2, y2));
            selectedShapeIndex = -1;
            MyPaint.Undo.push(new ArrayList<MyShape>(shapes));
            paint.repaint();

        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
