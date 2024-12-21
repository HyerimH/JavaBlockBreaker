package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameOverScreen extends JPanel {
    private JFrame frame;

    public GameOverScreen(JFrame frame, int lastScore, int highScore) {
        this.frame = frame;
        this.setBackground(Color.BLACK);

        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.BLACK);

        JLabel gameOverLabel = new JLabel("GAME OVER", JLabel.CENTER);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 50));
        gameOverLabel.setForeground(Color.WHITE);
        gameOverLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lastScoreLabel = new JLabel("Last Score: " + lastScore, JLabel.CENTER);
        lastScoreLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        lastScoreLabel.setForeground(Color.WHITE);
        lastScoreLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel highScoreLabel = new JLabel("High Score: " + highScore, JLabel.CENTER);
        highScoreLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        highScoreLabel.setForeground(Color.WHITE);
        highScoreLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel restartLabel = new JLabel("PRESS SPACEBAR TO RESTART", JLabel.CENTER);
        restartLabel.setFont(new Font("Arial", Font.BOLD, 20));
        restartLabel.setForeground(Color.RED);
        restartLabel.setAlignmentX(CENTER_ALIGNMENT);

        Timer timer = new Timer(500, e -> restartLabel.setVisible(!restartLabel.isVisible()));
        timer.start();

        centerPanel.add(gameOverLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(lastScoreLabel);
        centerPanel.add(highScoreLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(restartLabel);

        this.add(centerPanel, BorderLayout.CENTER);

        // 스페이스바 이벤트 처리
        setFocusable(true);
        requestFocusInWindow();

        // Focus Issue 해결
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    restartGame();
                }
            }
        });
    }

    private void restartGame() {
        GameScreen gameScreen = new GameScreen(frame);
        frame.remove(this);
        frame.add(gameScreen);
        frame.revalidate();
        frame.repaint();
        gameScreen.requestFocusInWindow(); // 게임 화면으로 포커스 이동
    }
}
