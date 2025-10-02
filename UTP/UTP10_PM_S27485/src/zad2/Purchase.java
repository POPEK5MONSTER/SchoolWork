/**
 *
 *  @author Popowski Mateusz S27485
 *
 */

package zad2;

import java.beans.*;

public class Purchase {

    private String prod;
    private String data;
    private Double price;

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private VetoableChangeSupport vetoableChangeSupport = new VetoableChangeSupport(this);

    public Purchase(String prod, String data, Double price) {
        this.prod = prod;
        this.data = data;
        this.price = price;
    }

    public String getProd() {
        return prod;
    }

    public void setProd(String prod) {
        String oldProd = this.prod;
        this.prod = prod;
        propertyChangeSupport.firePropertyChange("prod", oldProd, prod);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) throws PropertyVetoException {
        String oldData = this.data;
        vetoableChangeSupport.fireVetoableChange("data", oldData, data);
        this.data = data;
        propertyChangeSupport.firePropertyChange("data", oldData, data);
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) throws PropertyVetoException {
        Double oldPrice = this.price;
        vetoableChangeSupport.fireVetoableChange("price", oldPrice, price);
        this.price = price;
        propertyChangeSupport.firePropertyChange("price", oldPrice, price);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public void addVetoableChangeListener(VetoableChangeListener listener) {
        vetoableChangeSupport.addVetoableChangeListener(listener);
    }

    public void removeVetoableChangeListener(VetoableChangeListener listener) {
        vetoableChangeSupport.removeVetoableChangeListener(listener);
    }

    @Override
    public String toString() {
        return "Purchase [prod=" + prod + ", data=" + data + ", price=" + price + "]";
    }
}

