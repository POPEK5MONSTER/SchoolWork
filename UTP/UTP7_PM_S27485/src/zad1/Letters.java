package zad1;
import java.io.IOException;

public class Letters {

    Thread [] thread;
    String letter;
    public Letters(String letter){
        this.letter = letter;
        this.thread = new Thread[letter.length()];
        for (int i = 0; i< letter.length(); i++){
            String letter1 = String.valueOf(letter.charAt(i));
            thread[i] = new Thread("Thread " + letter.charAt(i)){
                @Override
                public void run(){
                    while (true){
                        System.out.print(letter1);
                        try{
                            Thread.sleep(1000);
                        } catch (InterruptedException e){
                            break;
                        }
                    }
                }
            };
        }
    }
    public java.lang.Thread[] getThreads() {
        return thread;
    }
}

