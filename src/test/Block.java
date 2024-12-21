package test;

import java.awt.*;
import java.util.Random;

public class Block implements Runnable {
    private int x, y, width, height;
    private Color color;
    private boolean isYellow; // 노란 블록 여부
    private boolean blinking; // 깜빡임 여부
    private Thread blinkThread; // 깜빡임 쓰레드

    public Block(int x, int y, int width, int height, boolean isYellow) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isYellow = isYellow;

        // 색상 설정
        if (isYellow) {
            this.color = new Color(250, 250, 0); // 노란 블록 기본 색상
            blinking = true;
            startBlinking(); // 깜빡임 쓰레드 시작
        } else {
            this.color = new Color(150, 100, 150); // 마젠타 블록 색상
        }
    }

    private void startBlinking() {
        blinkThread = new Thread(this);
        blinkThread.start();
    }

    @Override
    public void run() {
        try {
            Random random = new Random();
            int initialDelay = random.nextInt(3000); // 0 ~ 3초 랜덤 초기 딜레이
            Thread.sleep(initialDelay); // 초기 딜레이

            while (blinking) {
                // 밝은 노란색 ↔ 어두운 노란색으로 전환
                if (color.equals(new Color(250, 250, 0))) {
                    color = new Color(200, 200, 0); // 어두운 노란색
                } else {
                    color = new Color(250, 250, 0); // 밝은 노란색
                }

                Thread.sleep(3000); // 3초 간격으로 깜빡임
            }
        } catch (InterruptedException e) {
            // 쓰레드 중단 시 처리
            Thread.currentThread().interrupt();
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public Color getColor() {
        return color;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // 블록 내부 채우기
        g2d.setColor(color);
        g2d.fillRect(x, y, width, height);

        // 유리 블록처럼 보이게 테두리 효과 추가
        GradientPaint gradient = new GradientPaint(
                x, y, new Color(100, 100, 100), // 테두리 색상: 진한 색
                x + width, y + height, new Color(50, 50, 50) // 테두리 색상: 밝은 색
        );
        g2d.setPaint(gradient);
        g2d.setStroke(new BasicStroke(3)); // 테두리 두께
        g2d.drawRect(x, y, width, height);
    }

    // 블록이 제거되면 깜빡임을 멈춤
    public void stopBlinking() {
        blinking = false;
        if (blinkThread != null) {
            blinkThread.interrupt();
        }
    }

    // 노란 블록 여부 반환
    public boolean isYellow() {
        return isYellow;
    }
}
