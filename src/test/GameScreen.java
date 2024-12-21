package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Random;

public class GameScreen extends JPanel implements Runnable {
    private JFrame frame;
    private Paddle paddle;
    private CopyOnWriteArrayList<Ball> balls;
    private CopyOnWriteArrayList<Block> blocks;
    private boolean gameOver = false;
    private int score = 0;
    private int round = 1;
    private static int highestScore = 0;
    public static int lastScore = 0;

    public GameScreen(JFrame frame) {
        this.frame = frame;
        this.setBackground(Color.BLACK);

        paddle = new Paddle(350, 500);
        balls = new CopyOnWriteArrayList<>();
        resetBalls();
        blocks = new CopyOnWriteArrayList<>();
        createBlocks(round);

        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    paddle.moveLeft();
                    repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    paddle.moveRight();
                    repaint();
                }
            }
        });

        SwingUtilities.invokeLater(this::requestFocusInWindow);

        new Thread(this).start();
    }

    private void createBlocks(int round) {
        blocks.clear();
        int cols = round * 3;
        int rows = round * 3;
        int blockWidth = 800 / cols;
        int blockHeight = (600 / 2) / rows;
        Random random = new Random();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = col * blockWidth;
                int y = row * blockHeight;
                Color blockColor = random.nextBoolean() ? Color.YELLOW : Color.MAGENTA;
                blocks.add(new Block(x, y, blockWidth - 5, blockHeight - 5, blockColor));
            }
        }
    }

    private void resetBalls() {
        balls.clear();
        balls.add(new Ball(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - 10, this));
    }

    public void addScore(int points) {
        score += points;
    }

    @Override
    public void run() {
        while (!gameOver) {
            for (Ball ball : balls) {
                ball.move();
                ball.checkCollision(paddle, blocks);
            }

            repaint();

            if (blocks.isEmpty()) {
                round++;
                for (Ball ball : balls) {
                    ball.increaseSpeed();
                }
                resetBalls();
                createBlocks(round);
            }

            lastScore = score;
            updateHighestScore();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateHighestScore() {
        if (score > highestScore) {
            highestScore = score;
        }
    }

    public void gameOver(int finalScore) {
        gameOver = true;
        lastScore = finalScore;
        round = 1;
        score = 0;

        GameOverScreen gameOverScreen = new GameOverScreen(frame, lastScore, highestScore);
        frame.remove(this);
        frame.add(gameOverScreen);
        frame.revalidate();
        frame.repaint();
    }

    public void addBall(Ball ball) {
        balls.add(ball);
    }

    public void removeBall(Ball ball) {
        balls.remove(ball);
        if (balls.isEmpty()) {
            gameOver(score);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!gameOver) {
            for (Ball ball : balls) {
                ball.draw(g);
            }
            paddle.draw(g);
            for (Block block : blocks) {
                block.draw(g);
            }
            g.setColor(Color.WHITE);
            g.drawString("Score: " + score, 10, 20);
            g.drawString("Round: " + round, 700, 20);
        }
    }
}
