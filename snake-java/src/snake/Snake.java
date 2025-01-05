package snake;

import java.util.ArrayList;
import java.util.List;

public class Snake {

    int lenght=1;
    int head_pos;
    boolean alive = false;
    int minX;
    int maxX;
    
    char direction;

    List<Integer> snake_occupated_List = new ArrayList<>();
    
    boolean moved = true;
    
    public void init(char type){ // A -cała plansza ; L -lewa strona planszy ; R -prawa strona planszy
        this.alive = true;
        this.direction = 'D'; // ustawianie warunków startowych i zakresu ruchu węża
        switch(type){
            case 'A':
                this.minX = 1;
                this.maxX = 16;
                this.head_pos = 1;
                break;
            case 'L':
                this.minX = 1;
                this.maxX = 8;
                this.head_pos = 1;
                break;
            case 'R':
                this.minX = 9;
                this.maxX = 16;
                this.head_pos = 9;
                break;
            }
            this.lenght = 1;      
    }

    public void move(){ // ruch węża w wyznaczonym kierunku, oraz detekcja zderzenia z granicą planszy
        if(this.direction == 'W'){
            if(this.head_pos > 16){
                this.head_pos -= 16;    
            }
            else{
                this.alive = false;
            }
        }
    
        if(this.direction == 'D'){
            int y = this.head_pos;
                
            while(y>16){
                y -= 16;
            }
            if(y < this.maxX){
                this.head_pos += 1;  
            }
            else{
                this.alive = false;
            }
        }
    
        if(this.direction == 'S'){
            if(this.head_pos < 177){
                this.head_pos += 16;
                    
            }
            else{
                this.alive = false;
            }
        }
    
        if(this.direction == 'A'){
            int y = this.head_pos;
                
            while(y>16){
                y -= 16;
            }
            if(y > minX){
                this.head_pos -= 1;       
            }
            else{
                this.alive = false;
            }
        }
        
        this.moved = true;
    }
    
    public void set_dir(char dir){ // ustawienie kierunku węża
        this.direction = dir;
        this.moved = false;
    }
    
    public void update(){ // uaktualnienie pozycji węża i jego części 'ciała'
        this.snake_occupated_List.add(0,head_pos);

        while(this.snake_occupated_List.size() > this.lenght){
            this.snake_occupated_List.remove(this.snake_occupated_List.size() - 1);
        }            
    }

    public void selfEaten(){ // sprawdzanie, czy wąż się sam nie zjadł, oraz wyciąganie z tego konsekwencji
        for(int i : this.snake_occupated_List){
            if(i == this.head_pos){
                this.alive = false;
            }
        }
    }
    
    public void eat(){ // wydłużanie węża po zjedzeniu
        this.lenght++;
    }
}
