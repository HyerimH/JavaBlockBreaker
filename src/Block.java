import java.awt.*;

public class Block {
    private int x, y, width, height;
    private Color color;

    public Block(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.width = 100;  // 블록의 너비
        this.height = 30;  // 블록의 높이
        this.color = color;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public Color getColor() {
        return color;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }
}
