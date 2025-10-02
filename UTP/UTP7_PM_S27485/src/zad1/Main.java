/**
 *
 *  @author Popowski Mateusz S27485
 *
 */

package zad1;


public class Main {

  public static void main(String[] args) throws InterruptedException {
    Letters letters = new Letters("ABCD");
    for (Thread t : letters.getThreads()) System.out.println(t.getName());
    for (Thread thre : letters.getThreads())
      thre.start();

    Thread.sleep(5000);

    for (Thread thre : letters.getThreads())
      thre.interrupt();

    System.out.println("\nProgram skończył działanie");
  }

}
