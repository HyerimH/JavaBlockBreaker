package test;

import java.awt.*;

public class Paddle {
    private int x, y;
    private final int WIDTH = 150, HEIGHT = 25;
    private final int speed = 9; // 일정 속도 유지
    private final int SCREEN_WIDTH; // 화면 너비
    private final Color innerColor = new Color(150, 100, 100, 255); // 내부 색상
    private final Color borderColor = new Color(254, 201, 201, 255); // 테두리 색상

    public Paddle(int x, int y, int screenWidth) {
        this.x = x;
        this.y = y;
        this.SCREEN_WIDTH = screenWidth; // 화면 너비를 생성자로 받음
    }

    public void moveLeft() {
        if (x > 0) {
            x -= speed;
        }
    }

    public void moveRight() {
        if (x + WIDTH < SCREEN_WIDTH) {
            x += speed;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // 패들 내부 채우기
        g2d.setColor(innerColor);
        g2d.fillRect(x, y, WIDTH, HEIGHT);

        // 테두리 효과 추가
        g2d.setStroke(new BasicStroke(3)); // 테두리 두께
        g2d.setColor(borderColor);
        g2d.drawRect(x, y, WIDTH, HEIGHT);
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
