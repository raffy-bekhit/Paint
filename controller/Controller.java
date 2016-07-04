package controller;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import model.*;
import observer.paintListener;
import view.MyJFrame;
import view.MyPaint;

public class Controller {

    public static void main(String[] args) {

        MyJFrame jframe = new MyJFrame();
        MyPaint paint = MyPaint.getInstance();
        paintListener pl = new paintListener();
        paint.addMouseListener(pl);
        jframe.add(paint);
        jframe.setVisible(true);

    }

}
