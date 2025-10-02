package zad1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Read extends Thread{
    private String sciezka;
    private Magazyn magazyn;

    private static double Waga = 0;
    public Read(String sciezka, Magazyn magazyn) {
        this.sciezka = sciezka;
        this.magazyn = magazyn;
    }

    @Override
    public void run(){
        try (BufferedReader br = new BufferedReader(new FileReader(sciezka))){
            String line;
            int count = 0;
            while ((line = br.readLine()) != null){
                String[] parts = line.split(" ");
                int id_towar = Integer.parseInt(parts[0]);
                double waga = Double.parseDouble(parts[1]);
                Waga += waga;
                Towar towar = new Towar(id_towar, waga);
                magazyn.dodajTowar(towar);
                count++;

                if (count % 200 == 0){
                    Thread.sleep(10);
                }
            }
        } catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
    public static double getWaga() {
        return Waga;
    }

}
