package zad1;

public class CountTowar extends Thread{
    private Magazyn magazyn;

    public CountTowar(Magazyn magazyn) {
        this.magazyn = magazyn;
    }

    @Override
    public void run(){
        while (true){
            int liczbaTowarow = magazyn.getLiczbaTowarow();
            if (liczbaTowarow <= 10000){
                break;
            }
        }
    }
}
