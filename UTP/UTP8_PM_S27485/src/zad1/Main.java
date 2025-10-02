/**
 *
 *  @author Popowski Mateusz S27485
 *
 */

package zad1;


public class Main {

  public static void main(String[] args) {
    Magazyn magazyn = new Magazyn();
    Read read = new Read("../Towary.txt", magazyn);
    CountTowar countTowar = new CountTowar(magazyn);

    read.start();
    countTowar.start();

    try{
      read.join();
      countTowar.join();

    } catch (InterruptedException e){
      e.printStackTrace();
    }
    double sumaWag = Read.getWaga();
    System.out.println("Sumaryczna waga wszystkich towarow: " + sumaWag);
  }
}
