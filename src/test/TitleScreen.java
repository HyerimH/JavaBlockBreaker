package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TitleScreen extends JPanel {
    private JFrame frame;
    private boolean isFlashing = true;

    public TitleScreen(JFrame frame) {
        this.frame = frame;
        this.setBackground(Color.BLACK);

        // 스페이스바 입력 대기
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    startGame();
                }
            }
        });

        // 깜빡이는 텍스트를 위한 스레드 시작
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500); // 0.5초마다 깜빡임
                    isFlashing = !isFlashing;
                    repaint(); // 화면 갱신
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void startGame() {
        frame.remove(this);
        GameScreen gameScreen = new GameScreen(frame);
        frame.add(gameScreen);
        frame.revalidate();
        frame.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // "Java Block Breaker" 텍스트
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 60));
        g.drawString("Java Block Breaker", 100, 200);

        // "Press SPACE to Start" 텍스트
        if (isFlashing) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLACK);
        }
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("Press SPACE to Start", 150, 400);
    }
}
