package test;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // JFrame 생성
        JFrame frame = new JFrame("Java Block Breaker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        // 창을 화면 중앙에 배치
        frame.setLocationRelativeTo(null);

        // 창 크기 조정 불가
        frame.setResizable(false);

        // 타이틀 화면을 처음으로 표시
        TitleScreen titleScreen = new TitleScreen(frame);
        frame.add(titleScreen);
        frame.setVisible(true);
    }
}
