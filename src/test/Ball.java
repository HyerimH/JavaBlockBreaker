package test;

import java.awt.*;
import java.util.List;

public class Ball {
    private double x, y; // 공의 위치
    private double dx, dy; // 공의 이동 방향
    private final int radius = 4; // 공의 반지름
    private final double speed; // 공의 고정 속도 (라운드 동안 일정)
    private GameScreen gameScreen;

    public Ball(double x, double y, GameScreen gameScreen, double initialSpeed) {
        this.x = x;
        this.y = y;
        this.gameScreen = gameScreen;
        this.speed = initialSpeed; // 라운드 시작 시 공의 속도 설정

        // 초기 속도 및 각도 설정
        double angle = Math.toRadians(45 + Math.random() * 45); // 45도 ~ 90도 사이 랜덤 각도
        dx = Math.cos(angle) * speed;
        dy = Math.abs(Math.sin(angle) * speed); // 아래로 가도록 설정
    }

    public void move() {
        x += dx;
        y += dy;

        // 벽과 충돌 처리
        if (x <= 0 || x >= 800 - radius * 2) {
            reflectX(); // x 방향 반사
        }

        if (y <= 0) { // 천장에 닿으면
            dy = Math.abs(dy); // 아래로 반사
        }

        if (y >= 600 - radius * 2) { // 화면 아래로 벗어나면
            gameScreen.removeBall(this);
        }

        normalizeSpeed(); // 항상 동일한 속도 유지
    }

    private void reflectX() {
        dx = -dx;
        normalizeSpeed(); // 속도 유지
    }

    public void checkCollision(Paddle paddle, List<Block> blocks) {
        // 패들과의 충돌
        if (getBounds().intersects(paddle.getBounds())) {
            dy = -Math.abs(dy); // 위로 튕기도록 설정

            // 패들 위치에 따라 공의 dx 값을 조정
            int paddleX = paddle.getX();
            int paddleWidth = paddle.getWidth();
            int ballCenter = (int) (x + radius);

            double hitPosition = (double) (ballCenter - paddleX) / paddleWidth; // 0.0 ~ 1.0 범위
            dx = (hitPosition - 0.5) * 2 * speed; // -speed ~ +speed 범위로 dx 설정

            normalizeSpeed(); // 속도 유지
        }

        // 블록과의 충돌
        for (Block block : blocks) {
            if (getBounds().intersects(block.getBounds())) {
                blocks.remove(block);
                gameScreen.addScore(10); // 점수 추가

                // 노란 블록의 경우 공 분열
                if (block.isYellow()) {
                    splitBall();
                }

                // 항상 아래로 반사
                dy = Math.abs(dy); // 아래로 반사
                break;
            }
        }
    }

    private void splitBall() {
        // 기존 공의 이동 각도 계산
        double currentAngle = Math.atan2(dy, dx); // 기존 공의 이동 각도

        // 왼쪽 공과 오른쪽 공의 각도 설정
        double leftAngle = currentAngle - Math.toRadians(15); // -15도
        double rightAngle = currentAngle + Math.toRadians(15); // +15도

        // 위치 오프셋 설정 (공 간격을 둠으로써 겹치지 않게)
        double offset = radius + 2; // 공의 반지름 + 약간의 간격

        // 중앙 공: 기존 각도 그대로 유지
        this.dx = Math.cos(currentAngle) * speed;
        this.dy = Math.sin(currentAngle) * speed;
        normalizeSpeed(); // 속도 유지

        // 왼쪽 공 생성
        Ball leftBall = new Ball(x - offset, y, gameScreen, speed);
        leftBall.setAngle(leftAngle);
        leftBall.normalizeSpeed(); // 속도 유지

        // 오른쪽 공 생성
        Ball rightBall = new Ball(x + offset, y, gameScreen, speed);
        rightBall.setAngle(rightAngle);
        rightBall.normalizeSpeed(); // 속도 유지

        // 게임 화면에 공 추가
        gameScreen.addBall(leftBall);
        gameScreen.addBall(rightBall);
    }

    private void normalizeSpeed() {
        // 현재 속도를 계산하고, speed에 맞게 방향 벡터 조정
        double currentSpeed = Math.sqrt(dx * dx + dy * dy);
        if (currentSpeed != speed) { // 속도가 다르면 조정
            dx = (dx / currentSpeed) * speed;
            dy = (dy / currentSpeed) * speed;
        }
    }

    public void setAngle(double angle) {
        dx = Math.cos(angle) * speed;
        dy = Math.sin(angle) * speed;
        normalizeSpeed(); // 속도 유지
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval((int) x, (int) y, radius * 2, radius * 2);
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, radius * 2, radius * 2);
    }
}
