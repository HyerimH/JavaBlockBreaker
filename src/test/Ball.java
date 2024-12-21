package test;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Ball {
    private int x, y, dx, dy, radius;
    private int speedFactor = 1;
    private GameScreen gameScreen;

    public Ball(int x, int y, GameScreen gameScreen) {
        this.x = x;
        this.y = y;
        this.radius = 10;
        this.dx = 2;
        this.dy = 2; // 기본적으로 아래로 가는 방향
        this.gameScreen = gameScreen;
    }

    public void move() {
        x += dx * speedFactor;
        y += dy * speedFactor;

        if (x <= 0 || x >= 800 - radius) {
            dx = -dx;
        }
        if (y <= 0) {
            dy = -dy;
        }
        if (y >= 600 - radius) {
            gameScreen.removeBall(this);
        }
    }

    public void checkCollision(Paddle paddle, CopyOnWriteArrayList<Block> blocks) {
        if (getBounds().intersects(paddle.getBounds())) {
            dy = -Math.abs(dy); // 위로 튕기도록
        }

        for (Block block : blocks) {
            if (getBounds().intersects(block.getBounds())) {
                blocks.remove(block);
                gameScreen.addScore(10); // 점수 추가
                dy = -dy;

                if (block.getColor() == Color.YELLOW) {
                    splitBall();
                }
            }
        }
    }

    private void splitBall() {
        // 공 2개 생성: 왼쪽, 오른쪽
        Ball leftBall = new Ball(x - 15, y, gameScreen);
        Ball rightBall = new Ball(x + 15, y, gameScreen);

        // 왼쪽 공: 왼쪽 아래로 이동
        leftBall.dx = -2;
        leftBall.dy = 2;

        // 오른쪽 공: 오른쪽 아래로 이동
        rightBall.dx = 2;
        rightBall.dy = 2;

        // 게임 화면에 공 추가
        gameScreen.addBall(leftBall);
        gameScreen.addBall(rightBall);
    }


    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, radius, radius);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, radius, radius);
    }

    public void increaseSpeed() {
        speedFactor++;
    }
}
