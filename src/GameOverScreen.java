import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameOverScreen extends JPanel {
    private JFrame frame;

    public GameOverScreen(JFrame frame) {
        this.frame = frame;
        this.setBackground(Color.BLACK);

        // 게임 오버 텍스트
        JLabel gameOverLabel = new JLabel("GAME OVER", JLabel.CENTER);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 50));
        gameOverLabel.setForeground(Color.WHITE);

        // 재시작 버튼
        JButton restartButton = new JButton("Restart");
        restartButton.setFont(new Font("Arial", Font.PLAIN, 30));
        restartButton.setPreferredSize(new Dimension(200, 50));
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });

        // 레이아웃 설정
        this.setLayout(new BorderLayout());
        this.add(gameOverLabel, BorderLayout.CENTER);
        this.add(restartButton, BorderLayout.SOUTH);
    }

    // 게임을 재시작하는 메서드
    private void restartGame() {
        GameScreen gameScreen = new GameScreen(frame);
        frame.remove(this);
        frame.add(gameScreen);
        frame.revalidate();
        frame.repaint();
    }
}
