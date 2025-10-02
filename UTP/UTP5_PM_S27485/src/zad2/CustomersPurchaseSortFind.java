/**
 *
 *  @author Popowski Mateusz S27485
 *
 */

package zad2;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomersPurchaseSortFind {

    private List<Purchase> purchaseList;
    public void readFile(String fname)  {
        purchaseList = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(fname));
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String [] parts = line.split(";");
                if (parts.length == 5){
                    purchaseList.add(new Purchase(parts[0],parts[1],parts[2],Double.parseDouble(parts[3]),Double.parseDouble(parts[4])));
                }
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showPurchaseFor(String clientId) {
        System.out.println("Klient " + clientId);
        for (Purchase purchase : purchaseList) {
            if (purchase.getId().equals(clientId)){
                System.out.println(purchase);
            }
        }
        System.out.println();
    }

    public void showSortedBy(String sortBy) {
        List<Purchase> sortedList = new ArrayList<>(purchaseList);
        switch (sortBy){
            case "Nazwiska":
                sortedList.sort((p1, p2) -> {
                    if (p1.getName().equals(p2.getName())){
                        return p1.getId().compareTo(p2.getId());
                    }
                    return p1.getName().compareTo(p2.getName());
                });
                break;
            case "Koszty":
                sortedList.sort((p1, p2) -> {
                    if (p1.getPrice() * p1.getHowMany() == p2.getPrice() * p2.getHowMany()){
                        return p1.getId().compareTo(p2.getId());
                    }
                    return Double.compare(p2.getPrice() * p2.getHowMany(), p1.getPrice() * p1.getHowMany());
                });
                break;
        }

        System.out.println(sortBy);
        for (Purchase purchase : sortedList) {
            if (sortBy.equals("Koszty")) {
                System.out.println(String.join(" ", purchase.toString(), "(koszt: "+ (purchase.getPrice() * purchase.getHowMany())+")"));
            } else  {
                System.out.println(purchase);
            }
        }
        System.out.println();
    }
}
