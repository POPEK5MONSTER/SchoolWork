package zad1;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.text.NumberFormat;


public class TravelData{

    private File dataDir;

    public TravelData(File dataDir) {
        this.dataDir = dataDir;
    }

    public List<String> getOffersDescriptionsList(String loc, String dateFormat) {
        List<String> descriptions = new ArrayList<>();
        File[] files = dataDir.listFiles();
        if (files != null) {
            for (File file : files) {
                try (Scanner scanner = new Scanner(file)) {
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] parts = line.split("\t");
                        if (parts[5].contains(",") && !parts[5].contains(".")){
                            String withComma = parts[5].replace(",", ".");
                            parts[5] = withComma;
                        }
                        String formattedDescription = formatOfferDescription(parts, loc, dateFormat);
                        descriptions.add(formattedDescription);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return descriptions;
    }

    private String formatOfferDescription(String[] parts, String loc, String dateFormat) {
        Locale locale = new Locale(loc);
        SimpleDateFormat inputDateFormat = new SimpleDateFormat(dateFormat);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat(dateFormat, locale);

        try {
            Date departureDate = inputDateFormat.parse(parts[2]);
            Date returnDate = inputDateFormat.parse(parts[3]);
            String formattedDepartureDate = outputDateFormat.format(departureDate);
            String formattedReturnDate = outputDateFormat.format(returnDate);

            if (loc.equals("pl_PL")){
                switch (parts[1]){
                    case "Japan":
                        parts[1] = "Japan";
                        break;
                    case "Italy":
                        parts[1] = "Włochy";
                        break;
                    case "United States":
                        parts[1] = "Stany Zjednoczone Ameryki";
                        break;

                }
                switch (parts[4]){
                    case "sea":
                        parts[4] = "morze";
                        break;
                    case "lake":
                        parts[4] = "jezioro";
                        break;
                    case "mountains":
                        parts[4] = "góry";
                        break;
                }
                NumberFormat numberFormat = NumberFormat.getInstance(Locale.ROOT);
                Number parsedNumber = numberFormat.parse(parts[5]);
                double price = parsedNumber.doubleValue();

                return String.format(locale, "%s %s %s %s %.2f %s",
                        parts[1], formattedDepartureDate, formattedReturnDate,
                        parts[4], price, parts[6]);
            }
            else if (loc.equals("en_GB")){
                switch (parts[1]){
                    case "Japonia":
                        parts[1] = "Japan";
                        break;
                    case "Włochy":
                        parts[1] = "Italy";
                        break;
                    case "Stany Zjednoczone Ameryki":
                        parts[1] = "United States";
                        break;

                }
                switch (parts[4]){
                    case "morze":
                        parts[4] = "sea";
                        break;
                    case "jezioro":
                        parts[4] = "lake";
                        break;
                    case "góry":
                        parts[4] = "mountains";
                        break;
                }
                NumberFormat numberFormat = NumberFormat.getInstance(Locale.UK);
                Number parsedNumber = numberFormat.parse(parts[5]);
                double price = parsedNumber.doubleValue();

                return String.format(locale, "%s %s %s %s %.2f %s",
                        parts[1], formattedDepartureDate, formattedReturnDate,
                        parts[4], price, parts[6]);
            }
            else {
                return "";
            }
        } catch (ParseException | NumberFormatException e) {
            e.printStackTrace();
        }

        return "";
    }
}