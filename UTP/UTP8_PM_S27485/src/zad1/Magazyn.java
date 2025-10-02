package zad1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Magazyn {
    private List<Towar> towary = new ArrayList<>();
    public synchronized void dodajTowar(Towar towar){
            towary.add(towar);
            int liczbaTowarow = towary.size();

            if (liczbaTowarow % 200 == 0){
                System.out.println("Utworzono " + liczbaTowarow + " obiektow");
            }
            if (liczbaTowarow % 100 == 0){
                System.out.println("Policzono wage " + liczbaTowarow + " towarow");
            }
    }
    public int getLiczbaTowarow() {
            return towary.size();
    }
}
