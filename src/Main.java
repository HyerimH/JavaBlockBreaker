import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Java Block Breaker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // 타이틀 화면을 처음으로 표시
        TitleScreen titleScreen = new TitleScreen(frame);
        frame.add(titleScreen);
        frame.setVisible(true);
    }
}
