package snake;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener{
    static final int FIELD_X = 16;
    static final int FIELD_Y = 12;
    static final int RECT_SIZE = 75;
    static final int WINDOW_X = FIELD_X*RECT_SIZE;
    static final int WINDOW_Y = FIELD_Y*RECT_SIZE;

    static final int DELAY = 200;

    int points;
    int foodpos;

    Records records = new Records();
        
    

    Snake snake1 = new Snake();
    Snake snake2 = new Snake();
    Food food1 = new Food();
    Food food2 = new Food();
    char game_type;

    public JPanel menu;

    public JButton soloButton, pvpButton;
    boolean bVisible;
    
    // 1    2    3    4    5    6    7    8    9    10   11   12
    // 13   14   15   16   17   18 ...
    boolean running;
    boolean run_backup;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        //utworzenie przycisków startu gry:
       
        soloButton = new JButton("Solo Mode");
        pvpButton = new JButton("PvP Mode");

        soloButton.addActionListener(this);
		pvpButton.addActionListener(this); 
            

        this.add(soloButton);
        this.add(pvpButton);
        
        bVisible = false; // zmienna urzywana do sprawdzania zmiany stanu gry

        random = new Random();
        this.setPreferredSize(new Dimension(WINDOW_X,WINDOW_Y));
        startGame();
    }
        
    public int convert(int x, int y){ //konwersja położenia x,y na wcześniej przyjęty format
        int result = 0;
            result += x;
            result += (y-1)*16;
        return result;
    }

    public int reconvert(int field, boolean xisTrue){ //odwrotna konwersja, zwraca x lub y w zależności od zmiennej bool
        int x = field;
        int y = 1;
        while (x > 16){
            x -= 16;
            y += 1;
        }
        if(xisTrue){
            return x;
        }
        else{
            return y;
        }
            
    }
  
    public void paintComponent (Graphics g) { // wyświetla na okno grę lub 'lobby'
        super.paintComponent(g);
        if(running){
            draw(g);
        }
        else{
            lobby(g);
        }
    }
        
    public void startGame() { // ustawianie danych na starcie
        running = false;
        timer= new Timer(DELAY,this);
        timer.start();
    }
            
    public void draw(Graphics g){ // elementy wyświetlane podczas gry
        if(running){

            g.setColor(Color.GRAY);

            for(int i = 0;i<FIELD_X;i++){
                g.drawLine(i*RECT_SIZE,0,i*RECT_SIZE,WINDOW_Y);
            }

            for(int i = 0;i<FIELD_X;i++){
                g.drawLine(0,i*RECT_SIZE,WINDOW_X,i*RECT_SIZE);
            }

            g.setColor(Color.CYAN);

            for(int i : snake1.snake_occupated_List){
                g.fillRect(RECT_SIZE*(reconvert(i, true)-1), RECT_SIZE*(reconvert(i, false)-1), RECT_SIZE+1, RECT_SIZE+1);
            }

            g.setColor(Color.RED);

            g.fillRect(RECT_SIZE*(reconvert(food1.pos, true)-1), RECT_SIZE*(reconvert(food1.pos, false)-1), RECT_SIZE+1, RECT_SIZE+1);


            if(game_type == 'P'){
                g.setColor(Color.GREEN);
                for(int i : snake2.snake_occupated_List){
                    g.fillRect(RECT_SIZE*(reconvert(i, true)-1), RECT_SIZE*(reconvert(i, false)-1), RECT_SIZE+1, RECT_SIZE+1);

                }
                g.setColor(Color.PINK);
                g.fillRect(RECT_SIZE*(reconvert(food2.pos, true)-1), RECT_SIZE*(reconvert(food2.pos, false)-1), RECT_SIZE+1, RECT_SIZE+1);
                g.setColor(Color.GRAY);
                g.fillRect((WINDOW_X/2)-2, 0, 5, WINDOW_Y);
            }
        }
    }

    public void lobby(Graphics g){ // elementy wyświetlane w lobby

        g.setFont( new Font("Tahoma",Font.BOLD, 40));
        g.setColor(Color.WHITE);
        g.drawString("Last score: "+(snake1.lenght-1), 0, g.getFont().getSize());
        g.drawString("Top Scores:", 0, g.getFont().getSize()*3);
        int j = 0;
        try { // wyświetlanie topowych wyników (ranking)
            for(int i : records.getRecords()){
                g.drawString("- "+i, 0, g.getFont().getSize()*(4+j));
                j++;
                if(j >= 10){
                    break;
                }
            }
        } 
        catch (IOException e){}

        if (game_type == 'P'){ // wyświetlanie wyniku poprzedniej gry PvP (jeśli była)
            if(snake1.lenght > snake2.lenght){
                g.setColor(Color.CYAN);
                g.drawString("Blue snake WON", WINDOW_X/2, g.getFont().getSize()*3);

            }
            if(snake1.lenght < snake2.lenght){
                g.setColor(Color.GREEN);
                g.drawString("Green snake WON", WINDOW_X/2, g.getFont().getSize()*3);

            }
            if(snake1.lenght == snake2.lenght){
                g.setColor(Color.LIGHT_GRAY);
                g.drawString("DRAW", WINDOW_X/2, g.getFont().getSize()*3);
            }
        }

    }


    
    



    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == soloButton){ // start trybu solo
            game_type = 'S';
            running = true;
            snake1.alive=true;
            snake1.init('A');
            food1.init('A');
            food1.create(snake1);
            
        } 

        if(e.getSource() == pvpButton){ // start trybu PvP
            game_type = 'P';
            running = true;
            snake1.alive=true;
            snake1.init('L');
            food1.init('L');
            food1.create(snake1);

            snake2.alive=true;
            snake2.init('R');
            food2.init('R');
            food2.create(snake2);

        }


        if(running){
            if(bVisible){ // znikanie przycisków startu gry podczas gry
                soloButton.setVisible(false);
                pvpButton.setVisible(false);
                bVisible = false;
            }

            if(snake1.alive){ // aktualizacja statusu gracza (jeśli jego wąż zyje)
                snake1.move();
                food1.checkEaten(snake1);
                snake1.selfEaten();
                snake1.update();
            }

            if(snake2.alive){ // aktualizacja statusu drugiego gracza (jeśli jego wąż zyje)
                snake2.move();
                food2.checkEaten(snake2);
                snake2.selfEaten();
                snake2.update();
            }
            
            if(snake1.alive == false && snake2.alive == false){ // warunki i zakończenie rozgrywki dla obu trybów
                running = false;
            }
        }

        if(running == false){
            
            if(bVisible == false){ // pojawianie się przycisków startu gry po jej zakończeniu
                
                soloButton.setVisible(true);
                pvpButton.setVisible(true);
                
                bVisible = true;
                
                if(snake1.lenght > 1 && game_type == 'S'){ // zapis wyniku z rozgrywki po trybie solo
                    try {
                        records.saveRecord(snake1.lenght-1);
                        
                    } catch (IOException e1) {
                        //e1.printStackTrace();
                    }
                }
                
            }
            
        }
        repaint(); // aktualizacja okna 
    }

    public class MyKeyAdapter extends KeyAdapter{ // sterowanie wężem tryb solo - strzałki i WSAD , pvp - zestaw WSAD - gracz po lewe strzałki - po prawej
        @Override
        public void keyPressed(KeyEvent k){
            switch (k.getKeyCode()){
                case KeyEvent.VK_A:
                    if(snake1.direction != 'D' && snake1.moved){
                        snake1.set_dir('A');
                    }
                    break;
                case KeyEvent.VK_D:
                    if(snake1.direction != 'A' && snake1.moved){
                        snake1.set_dir('D');
                    }
                    break;
                case KeyEvent.VK_W:
                    if(snake1.direction != 'S' && snake1.moved){
                        snake1.set_dir('W');
                    }
                    break;
                case KeyEvent.VK_S:
                    if(snake1.direction != 'W' && snake1.moved){
                        snake1.set_dir('S');
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if(game_type == 'S'){
                        if(snake1.direction != 'D' && snake1.moved){
                            snake1.set_dir('A');
                        }
                    }
                    if(game_type == 'P'){
                        if(snake2.direction != 'D' && snake2.moved){
                            snake2.set_dir('A');
                        }
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(game_type == 'S'){
                        if(snake1.direction != 'A' && snake1.moved){
                            snake1.set_dir('D');
                        }
                    }
                    if(game_type == 'P'){
                        if(snake2.direction != 'A' && snake2.moved){
                            snake2.set_dir('D');
                        }
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(game_type == 'S'){
                        if(snake1.direction != 'S' && snake1.moved){
                            snake1.set_dir('W');
                        }
                    }
                    if(game_type == 'P'){
                        if(snake2.direction != 'S' && snake2.moved){
                            snake2.set_dir('W');
                        }
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(game_type == 'S'){
                        if(snake1.direction != 'W' && snake1.moved){
                            snake1.set_dir('S');
                        }
                    }
                    if(game_type == 'P'){
                        if(snake2.direction != 'W' && snake2.moved){
                            snake2.set_dir('S');
                        }
                    }
                    break;
            }
        }
    }

    
}

