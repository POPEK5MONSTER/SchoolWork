/**
 *
 *  @author Popowski Mateusz S27485
 *
 */

package zad2;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

public class Main {
  public static void main(String[] args) {

    Purchase purch = new Purchase("komputer", "nie ma promocji", 3000.00);
    System.out.println(purch);

    purch.addPropertyChangeListener(new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("Change value of: " + evt.getPropertyName() +
                " from: " + evt.getOldValue() +
                " to: " + evt.getNewValue());
      }
    });

    purch.addVetoableChangeListener(new VetoableChangeListener() {
      @Override
      public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        if (evt.getPropertyName().equals("price") && (Double) evt.getNewValue() < 1000) {
          throw new PropertyVetoException("Price change to: " + evt.getNewValue() + " not allowed", evt);
        }
      }
    });

    try {
      purch.setData("w promocji");
      purch.setPrice(2000.00);
      System.out.println(purch);

      purch.setPrice(500.00);

    } catch (PropertyVetoException exc) {
      System.out.println(exc.getMessage());
    }
    System.out.println(purch);

  }
}
