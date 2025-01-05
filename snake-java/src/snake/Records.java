package snake;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;



// format wyników to 3-znakowa sekwencja wyniku np. 021 
public class Records {
    List<Integer> recordsList = new ArrayList<>();
    File rec = new File("C:\\Users\\ikswe\\studia\\sem 3\\java\\projekt\\src\\snake\\records.txt");
    
    public List<Integer> getRecords() throws IOException{ // funkcja zczytująca poprzednie wyniki, zwraca ich listę
        
        FileReader fr = new FileReader(rec);
        int x = 0;
        int res = 0;
        recordsList.clear();

        while(x != -49){
            for(int i = 0; i < 3; i++){
                x = fr.read();
                x -= 48;
                if(x >= 0){
                    res *= 10;
                    
                    res+=x;
                    
                }
            }
            if(res != 0){
                this.recordsList.add(res);
                res = 0;
            }
            
            
        }
        fr.close();

        Collections.sort(recordsList, Collections.reverseOrder());
        return recordsList;
    }

    public void saveRecord(int rec) throws IOException{ // zapisanie wyniku do pliku
        File file = new File("C:\\Users\\ikswe\\studia\\sem 3\\java\\projekt\\src\\snake\\records.txt");
        FileWriter fw = new FileWriter(file, true);
        int s = 0;
        
        
        fw.write((s = rec/100)+48);
        rec -= s*100;
        fw.write((s = rec/10)+48);
        rec -= s*10;
        fw.write(s=rec + 48);

        fw.close();
    }

}
