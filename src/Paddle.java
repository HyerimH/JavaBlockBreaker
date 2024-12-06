import java.awt.*;

public class Paddle {
    private int x, y;
    private final int WIDTH = 100, HEIGHT = 20;
    private final int SPEED = 10;

    public Paddle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void moveLeft() {
        if (x > 0) {
            x -= SPEED;
        }
    }

    public void moveRight() {
        if (x + WIDTH < 800) {
            x += SPEED;
        }
    }

    // 마우스로 라켓 위치 이동
    public void moveTo(int mouseX) {
        // 마우스 위치에 따라 라켓이 이동
        if (mouseX > 0 && mouseX < 800 - WIDTH) {
            this.x = mouseX - WIDTH / 2; // 마우스 중앙에 라켓이 위치하게 조정
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, WIDTH, HEIGHT);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}
