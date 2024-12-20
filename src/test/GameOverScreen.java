import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameOverScreen extends JPanel {
    private JFrame frame;

    public GameOverScreen(JFrame frame, int lastScore, int highScore) {
        this.frame = frame;
        this.setBackground(Color.BLACK);

        // 레이아웃 설정
        this.setLayout(new BorderLayout());

        // 중앙 패널 (게임 오버 메시지와 점수 표시)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.BLACK);

        // 게임 오버 텍스트
        JLabel gameOverLabel = new JLabel("GAME OVER", JLabel.CENTER);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 50));
        gameOverLabel.setForeground(Color.WHITE);
        gameOverLabel.setAlignmentX(CENTER_ALIGNMENT);

        // 점수 표시
        JLabel lastScoreLabel = new JLabel("Last Score: " + lastScore, JLabel.CENTER);
        lastScoreLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        lastScoreLabel.setForeground(Color.WHITE);
        lastScoreLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel highScoreLabel = new JLabel("High Score: " + highScore, JLabel.CENTER);
        highScoreLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        highScoreLabel.setForeground(Color.WHITE);
        highScoreLabel.setAlignmentX(CENTER_ALIGNMENT);

        // 중앙 패널에 요소 추가
        centerPanel.add(gameOverLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20))); // 간격
        centerPanel.add(lastScoreLabel);
        centerPanel.add(highScoreLabel);

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

        // 패널에 요소 추가
        this.add(centerPanel, BorderLayout.CENTER);
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
