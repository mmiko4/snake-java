package snake;


import javax.swing.*;

public class GameFrame extends JFrame {
    GameFrame(){ // ustawienia okna wyświetlania
        
        this.add(new GamePanel());
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack(); 
        this.setVisible(true);
        this.setLocationRelativeTo(null); // na środku ekranu
    }
}
