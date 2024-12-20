import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Ball {
    private int x, y, dx, dy, radius;
    private int speedFactor = 1; // 공 속도 조절
    private int score = 0; // 점수 추가
    private int round = 1; // 현재 라운드
    private List<Ball> balls = new ArrayList<>(); // 공이 여러 개로 나누어질 때 사용
    private GameScreen gameScreen; // GameScreen 객체 참조

    public Ball(int x, int y, GameScreen gameScreen) {
        this.x = x;
        this.y = y;
        this.radius = 10;
        this.dx = 2;  // 초기 속도
        this.dy = -2;
        this.gameScreen = gameScreen; // GameScreen 객체 전달받아 저장
        balls.add(this); // 처음엔 하나의 공만 존재
    }

    public void move() {
        List<Ball> newBalls = new ArrayList<>(); // 공을 여러 개로 나눌 때 새로운 리스트

        for (Ball b : balls) {
            b.x += b.dx * speedFactor;
            b.y += b.dy * speedFactor;

            // 화면 밖으로 나가지 않도록 처리
            if (b.x <= 0 || b.x >= 800 - radius) { // 좌우 벽 충돌
                b.dx = -b.dx;
            }
            if (b.y <= 0) { // 상단 벽 충돌
                b.dy = -b.dy;
            }
            if (b.y >= 600 - radius) { // 하단 벽 (게임 오버 조건)
                gameScreen.gameOver(score); // GameScreen의 gameOver() 메서드 호출
            }
        }

        balls = newBalls; // 업데이트된 공 리스트 반영
    }

    public void checkCollision(Paddle paddle, List<Block> blocks) {
        // 공과 라켓의 충돌 체크
        for (Ball b : balls) {
            if (new Rectangle(b.x, b.y, radius, radius).intersects(paddle.getBounds())) {
                b.dy = -b.dy; // 공이 라켓에 튕겨나감
            }

            // 블록과의 충돌 처리
            for (int i = 0; i < blocks.size(); i++) {
                Block block = blocks.get(i);
                if (new Rectangle(b.x, b.y, radius, radius).intersects(block.getBounds())) {
                    blocks.remove(i); // 블록 제거
                    b.dy = -b.dy; // 공이 블록에 튕겨나감
                    score += 10; // 블록을 깨면 10점 추가

                    // 노란색 블록과 충돌 시, 공이 3개로 나뉨
                    if (block.getColor() == Color.YELLOW) {
                        createExtraBalls(b.x, b.y); // 공을 3개로 나누는 함수 호출
                    }
                }
            }
        }

        // 모든 블록을 깨면 라운드 업
        if (blocks.isEmpty()) {
            round++;
            resetBlocks(); // 새 라운드 블록 생성
            increaseSpeed(); // 라운드마다 공 속도 증가
        }
    }

    // 공이 3개로 나누어지는 함수
    private void createExtraBalls(int x, int y) {
        balls.add(new Ball(x - 5, y, gameScreen)); // 좌측으로 나가는 공
        balls.add(new Ball(x + 5, y, gameScreen)); // 우측으로 나가는 공
    }

    // 라운드가 바뀌면 새로운 블록을 생성
    private void resetBlocks() {
        // 라운드마다 블록의 크기와 개수가 달라짐
        int blockSize = 800 / (round * 3); // 가로 세로 크기
        int verticalBlocks = 600 / (round * 3); // 세로 블록 수
        Random rand = new Random();

        List<Block> newBlocks = new ArrayList<>();
        for (int i = 0; i < round * 3; i++) {
            for (int j = 0; j < round * 3; j++) {
                int blockX = i * blockSize;
                int blockY = j * (600 / (round * 3));

                // 블록의 색상 랜덤 설정 (50% 확률로 노란색과 보라색 블록 설정)
                Color blockColor = rand.nextBoolean() ? Color.YELLOW : Color.MAGENTA;

                newBlocks.add(new Block(blockX, blockY, blockSize, blockSize, blockColor));
            }
        }

        gameScreen.setBlocks(newBlocks); // GameScreen에서 블록 갱신
    }

    public void draw(Graphics g) {
        for (Ball b : balls) {
            g.setColor(Color.WHITE);
            g.fillOval(b.x, b.y, radius, radius);
        }
    }

    // 공 속도 증가 메서드
    public void increaseSpeed() {
        speedFactor++; // 공의 속도 증가
        dx *= 1.1; // 공의 수평 속도 증가
        dy *= 1.1; // 공의 수직 속도 증가
    }

    // 점수 반환
    public int getScore() {
        return score;
    }

    // 라운드 반환
    public int getRound() {
        return round;
    }
}
