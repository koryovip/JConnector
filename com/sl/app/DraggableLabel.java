package com.sl.app;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class DraggableLabel extends JLabel {
    Point pressPoint;
    Point releasePoint;
    DragProcessor dragProcessor = new DragProcessor();

    public DraggableLabel(String title) {
        super(title);
        setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.LOWERED), new EmptyBorder(1, 5, 1, 1)));
        addMouseListener(dragProcessor);
        addMouseMotionListener(dragProcessor);
    }

    protected class DragProcessor extends MouseAdapter implements MouseListener, MouseMotionListener {
        Window dragWindow = new JWindow() {
            public void paint(Graphics g) {
                super.paint(g);
                DraggableLabel.this.paint(g);
            }
        };

        public void mouseDragged(MouseEvent e) {
            Point dragPoint = e.getPoint();
            int xDiff = pressPoint.x - dragPoint.x;
            int yDiff = pressPoint.y - dragPoint.y;

            Rectangle b = e.getComponent().getBounds();
            Point p = b.getLocation();
            SwingUtilities.convertPointToScreen(p, e.getComponent().getParent());
            p.x -= xDiff;
            p.y -= yDiff;

            dragWindow.setLocation(p);
        }

        public void mouseMoved(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            pressPoint = e.getPoint();
            Rectangle b = e.getComponent().getBounds();
            Point p = b.getLocation();
            SwingUtilities.convertPointToScreen(p, e.getComponent().getParent());
            dragWindow.setBounds(b);
            dragWindow.setLocation(p);
            dragWindow.setVisible(true);
        }

        public void mouseReleased(MouseEvent e) {
            releasePoint = e.getPoint();
            dragWindow.setVisible(false);

            int xDiff = pressPoint.x - releasePoint.x;
            int yDiff = pressPoint.y - releasePoint.y;

            Rectangle b = e.getComponent().getBounds();
            Point p = b.getLocation();
            SwingUtilities.convertPointToScreen(p, e.getComponent().getParent());
            p.x -= xDiff;
            p.y -= yDiff;

            SwingUtilities.convertPointFromScreen(p, DraggableLabel.this.getParent());
            if (p.x <= 0) {
                p.x = 1;
            }
            if (p.x > DraggableLabel.this.getParent().getWidth() - b.width) {
                p.x = DraggableLabel.this.getParent().getWidth() - b.width;
            }
            if (p.y <= 0) {
                p.y = 1;
            }
            if (p.y > DraggableLabel.this.getParent().getHeight() - b.height) {
                p.y = DraggableLabel.this.getParent().getHeight() - b.height;
            }
            setLocation(p);
            getParent().repaint();
        }
    }
}
