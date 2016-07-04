package view;

import model.MyShape;
import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;
import java.awt.Graphics;

/**
 *
 * @author Raffy
 */
public class MyPaint extends Canvas {

    private static MyPaint paint = null;
    public static ArrayList<MyShape> shapes = new ArrayList<>();
    public static Stack<ArrayList<MyShape>> Redo = new Stack<>();
    public static Stack<ArrayList<MyShape>> Undo = new Stack<>();
    public static Stack<ArrayList<MyShape>> history = new Stack<>();

    private MyPaint() {
        setSize(770, 770);
        setBackground(Color.white);
    }

    public static MyPaint getInstance() {
        if (paint == null) {
            paint = new MyPaint();
        }
        MyPaint p = paint;
        return p;
    }

    public void paint(Graphics g) {
        for (MyShape shape : shapes) {
            g.setColor(shape.getColor());
            shape.paint(g);
        }
    }

    public void unDo() {
        if (!Undo.isEmpty()) {
            Redo.push(Undo.pop());
        }
        if (!Undo.isEmpty()) {
            shapes = new ArrayList<>(Undo.peek());

        } else {
            shapes = new ArrayList<>();

        }

        this.repaint();
    }

    public void reDo() {
        if (!Redo.isEmpty()) {
            Undo.push(Redo.pop());
        }
        if (!Redo.isEmpty()) {
            shapes = new ArrayList<>(Redo.peek());

        } else {

        }

        this.repaint();
    }

}
