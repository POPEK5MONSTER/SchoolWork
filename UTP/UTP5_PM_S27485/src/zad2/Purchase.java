/**
 *
 *  @author Popowski Mateusz S27485
 *
 */

package zad2;


public class Purchase {

    private String id;
    private String name;
    private String product;
    private double howMany;
    private double price;

    public Purchase(String id, String name, String product, double howMany, double price) {
        this.id = id;
        this.name = name;
        this.product = product;
        this.howMany = howMany;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getHowMany() {
        return howMany;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return   id + ";" + name + ";" + product + ";" + howMany + ";" + price ;
    }
}
