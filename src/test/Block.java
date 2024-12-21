package test;

import java.awt.*;

public class Block {
    private int x, y, width, height;
    private Color color;

    public Block(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
    }
}
