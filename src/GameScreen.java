import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GameScreen extends JPanel implements Runnable {
    private JFrame frame;
    private Paddle paddle;
    private Ball ball;
    private List<Block> blocks;
    private boolean gameOver = false;
    private int score = 0;
    private int round = 1;
    private static int highestScore = 0; // 최고 점수
    private static int lastScore = 0; // 이전 점수

    public GameScreen(JFrame frame) {
        this.frame = frame;
        this.setBackground(Color.BLACK);

        // 초기화
        paddle = new Paddle(350, 500); // 라켓 위치 초기화
        ball = new Ball(400, 450,this);     // 공 위치 초기화
        blocks = new ArrayList<>();

        // 블록 생성 (처음엔 3x3 블록)
        createBlocks(round);

        // 키 입력 처리 (방향키로 라켓 이동)
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    paddle.moveLeft();
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    paddle.moveRight();
                }
            }
        });

        // 마우스 입력 처리 (마우스 이동으로 라켓 이동)
        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                paddle.moveTo(e.getX());
            }
        });

        // 게임 스레드 시작
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (!gameOver) {
            ball.move();
            ball.checkCollision(paddle, blocks);
            repaint();

            // 게임이 진행될 때 라운드가 끝나면 점수를 갱신하고 라운드 증가
            if (blocks.isEmpty()) {
                round++;
                score += 100;  // 블록 모두 없애면 추가 점수
                ball.increaseSpeed();  // 속도 증가
                createBlocks(round);   // 새로운 라운드 블록 생성
            }

            // 최고 기록 갱신
            lastScore = score;
            updateHighestScore();  // 최고 기록 갱신 함수 호출

            try {
                Thread.sleep(10); // 10ms마다 화면 갱신
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void createBlocks(int round) {
        blocks.clear();
        int blockSize = 60; // 블록 크기
        int blockCount = round * 3; // 라운드가 진행될수록 블록이 늘어남
        int cols = 800 / blockSize; // 800px 너비에 맞는 블록 개수

        // 라운드에 따라 블록의 높이를 조정
        int rows = Math.max(1, blockCount / cols); // 블록 개수를 열에 맞게 나누고 최소 1줄이 되도록 설정
        int blockHeight = Math.max(30, 600 / rows); // 블록 높이를 줄여가며 배치, 최소 높이는 30px

        // 블록 배치
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (col + row * cols < blockCount) {
                    // 블록 색상 지정 (확률에 따라 다르게)
                    Color blockColor = Color.GREEN; // 기본 색상은 초록색
                    if (Math.random() < 0.1) {
                        blockColor = Color.YELLOW; // 노란 블록
                    } else if (Math.random() < 0.2) {
                        blockColor = Color.MAGENTA; // 보라 블록
                    }

                    // 각 블록의 x, y 위치와 크기 설정
                    Block block = new Block(col * blockSize, row * blockHeight, blockColor);
                    blocks.add(block);
                }
            }
        }
    }

    // 게임 오버 처리
    public void gameOver() {
        gameOver = true;
        setGameOver();  // 게임 오버 메서드 호출
    }

    // 게임 오버 처리
    public void setGameOver() {
        gameOver = true;
        updateHighestScore();  // 최고 기록 갱신
    }

    // 최고 기록 업데이트
    public static void updateHighestScore() {
        if (highestScore < lastScore) {
            highestScore = lastScore;
        }
    }

    // 게임 화면 그리기
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paddle.draw(g);
        ball.draw(g);

        for (Block block : blocks) {
            block.draw(g);
        }

        // 점수 표시
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.drawString("Score: " + score, 10, 30);

        // 최고 기록 표시
        g.drawString("Highest Score: " + highestScore, 10, 60);

        // 라운드 표시
        g.drawString("Round: " + round, 10, 90);

        // 게임 오버 처리
        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.setColor(Color.RED);
            g.drawString("GAME OVER", 300, 300);
        }
    }
}
