package snake;


import java.util.Random;

public class Food {

    

    int minX;
    int maxX;
    int rangeX;
    int pos;
    boolean eaten = true;
    Random random;
    int timesEaten = 0;


    public void init(char type){ // A -cała plansza ; L -lewa strona planszy ; R -prawa strona planszy
        random = new Random();
        switch(type){
            case 'A':
                minX = 1;
                maxX = 16;
                rangeX = 16;
                break;
            case 'L':
                minX = 1;
                maxX = 8;
                rangeX = 8;
                break;
            case 'R':
                minX = 9;
                maxX = 16;
                rangeX = 8;
                break;
        }
    }



    public void create(Snake snake){ // tworzenie jedzenia dla konkretnego węża (głównie tryb PvP)
        boolean collision = true;

        while(collision){ // sprawdzanie, czy jedzenie nie jest tworzone na wężu
            pos = random.nextInt(192)+1;

            int y = pos;
            while(y > 16){
                y -= 16;
            }
            if( y>= minX && y <= maxX){
                collision = false;
            }
            if(snake.head_pos == pos){
                collision = true;
            }
            for(int snake_pos : snake.snake_occupated_List){
                if(pos == snake_pos){
                    collision = true;
                    
                }
            }
        }
        

    }

    public void checkEaten(Snake snake){ // sprawdzanie, czy jedzenie jest zjedzone (pokrywa się z głową węża)
        if(snake.head_pos == this.pos){
            timesEaten++;
            this.create(snake);
            snake.eat();

        }


    }

}
