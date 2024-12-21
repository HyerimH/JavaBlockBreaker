package test;

import java.awt.*;

public class Paddle {
    private int x, y;
    private final int WIDTH = 100, HEIGHT = 20;
    private int speed = 15; // 부드러운 이동 속도

    public Paddle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void moveLeft() {
        if (x > 0) {
            x -= speed;
        }
    }

    public void moveRight() {
        if (x + WIDTH < 800) {
            x += speed;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, WIDTH, HEIGHT);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return WIDTH;
    }
}
