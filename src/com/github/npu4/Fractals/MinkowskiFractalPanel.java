package com.github.npu4.Fractals;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class MinkowskiFractalPanel extends JPanel {
    public static int sizeOfPanel = 590;

    public static boolean flagToContinue = true;
    public static java.util.List<MyLine> lines = new ArrayList<MyLine>();

    public static double[][] pattern = {
            {0,     0},
            {0.25,  0},
            {0.25,  -0.25},
            {0.5,   -0.25},
            {0.5,   0.25},
            {0.75,  0.25},
            {0.75,  0},
            {1,     0}
    };

    static  Point A = new Point(150,120);
    static int size = sizeOfPanel/2;

    public static int draw(Graphics g) {
        if(flagToContinue) {
            if (lines.size() == 0) {
                lines.add(new MyLine(A.x, A.y, A.x + size, A.y));
                lines.add(new MyLine(A.x + size, A.y, A.x + size, A.y + size));
                lines.add(new MyLine(A.x + size, A.y + size, A.x, A.y + size));
                lines.add(new MyLine(A.x, A.y + size, A.x, A.y));
                //выключаем необходимость продолжения
                flagToContinue = false;
                return 0;
            }
            java.util.List<MyLine> bufferLines = new ArrayList<MyLine>(); // буферный лист, сюда мы будем записывать новые линии
            java.util.List<Point> bufferPoints = new ArrayList<Point>(); // буферный лист точек, которые мы получаем

            // перебираем все линиии и преобразовываем их
            for (MyLine line :
                    lines) {
                bufferPoints.clear(); // очищаем лист точек, так как иначе он заполнится не нужными точками от предыдущих линий
                for (int i = 0; i < pattern.length; i++) {
                    double xRes = (line.X - line.x) * pattern[i][0] - (line.Y - line.y) * pattern[i][1] + line.x;
                    double yRes = (line.Y - line.y) * pattern[i][0] + (line.X - line.x) * pattern[i][1] + line.y;
                    bufferPoints.add(new Point((int) xRes, (int) yRes)); // получи точку запоминаем ее
                }
                //в этом цикле проходим по существующим точкам и создаем линии, добавляем их в буфер линий
                for (int i = 0; i < bufferPoints.size() - 1; i++) {
                    bufferLines.add(new MyLine(bufferPoints.get(i).x,
                            bufferPoints.get(i).y,
                            bufferPoints.get(i + 1).x,
                            bufferPoints.get(i + 1).y));
                }
            }
            flagToContinue = false; // отключаем флаг прохода
            lines = bufferLines; // забываем про старые линии, так как они не актуальны, и запоминаем новые
        }
        return 0;
    }

    @Override
    protected void paintComponent(Graphics g) { // этот медот вызывается у компонента при каждом обновлении кадра
        super.paintComponent(g);
        this.setBackground(Color.WHITE);
        draw(g);
        //цикл перебирает все существующие линии и отрисовывает их
        for (MyLine line:
                lines) {
            g.drawLine(line.x, line.y, line.X, line.Y);
        }
        repaint();
    }
}