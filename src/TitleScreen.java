import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TitleScreen extends JPanel {
    private JFrame frame;

    public TitleScreen(JFrame frame) {
        this.frame = frame;
        this.setBackground(Color.BLACK);

        // 스페이스바 입력 대기
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    startGame();
                }
            }
        });
    }

    // 게임 시작
    private void startGame() {
        frame.remove(this);
        GameScreen gameScreen = new GameScreen(frame);
        frame.add(gameScreen);
        frame.revalidate();
        frame.repaint();
    }

    // 타이틀 화면 그리기
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Press SPACE to Start", 250, 300);
    }
}
