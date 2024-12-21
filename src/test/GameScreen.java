package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

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

    private final double baseBallSpeed = 5.0; // 공의 기본 속도
    private final int SCREEN_WIDTH = 800; // 화면 너비
    private final int SCREEN_HEIGHT = 800; // 화면 높이

    public GameScreen(JFrame frame) {
        this.frame = frame;
        this.setBackground(Color.BLACK);

        // 패들 초기화 (아래에서 100px 띄우고 화면 중앙에 위치)
        paddle = new Paddle((SCREEN_WIDTH - 200) / 2, SCREEN_HEIGHT - 100, SCREEN_WIDTH);

        // 공 초기화
        balls = new CopyOnWriteArrayList<>();
        resetBalls();

        // 블록 초기화
        blocks = new CopyOnWriteArrayList<>();
        createBlocks(round);

        setFocusable(true);
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        // 키보드 입력 처리
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    paddle.moveLeft(); // 패들 왼쪽 이동
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    paddle.moveRight(); // 패들 오른쪽 이동
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    // 스페이스바를 누르면 게임 초기화
                    resetGame();
                }
                repaint(); // 움직임 반영
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        new Thread(this).start(); // 게임 루프 실행
    }

    private void createBlocks(int round) {
        blocks.clear();
        int cols = round * 3; // 열의 개수 (3배수로 유지)
        int rows = round * 3; // 행의 개수 (3배수로 유지)
        int blockWidth = SCREEN_WIDTH / cols; // 블록 가로 크기
        int blockHeight = (SCREEN_HEIGHT / 2) / rows; // 화면 상단 절반 차지하도록 설정
        Random random = new Random();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = col * blockWidth;
                int y = row * blockHeight;
                boolean isYellow = random.nextInt(10) < 3; // 30% 비율로 노란 블록
                blocks.add(new Block(x, y, blockWidth - 5, blockHeight - 5, isYellow));
            }
        }
    }

    private void resetBalls() {
        balls.clear();
        balls.add(new Ball(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - 10, this, baseBallSpeed));
    }

    private void resetGame() {
        gameOver = false;
        score = 0;
        round = 1;
        resetBalls();
        createBlocks(round);
        repaint();
    }

    public void addScore(int points) {
        score += points; // 점수 추가
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
                resetBalls();
                createBlocks(round);
            }

            lastScore = score;
            updateHighestScore();

            try {
                Thread.sleep(10); // 게임 루프 딜레이
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

        GameOverScreen gameOverScreen = new GameOverScreen(frame, lastScore, highestScore);
        frame.remove(this);
        frame.add(gameOverScreen);
        frame.revalidate();
        frame.repaint();
        SwingUtilities.invokeLater(gameOverScreen::requestFocusInWindow); // 게임 오버 화면에 포커스 설정
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
