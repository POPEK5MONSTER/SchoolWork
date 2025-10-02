/**
 *
 *  @author Popowski Mateusz S27485
 *
 */

package zad1;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Time {

    public static String passed(String from, String to){
        String result = "";
        DateTimeFormatter dataf = DateTimeFormatter.ofPattern("d MMMM yyyy (EEEE)");
        DateTimeFormatter datat = DateTimeFormatter.ofPattern("d MMMM yyyy (EEEE) 'godz.' HH:mm");
        try{
            if (!from.contains("T") && !to.contains("T")){
                LocalDate dateFrom = LocalDate.parse(from);
                LocalDate dateTo = LocalDate.parse(to);
                int days = (int) ChronoUnit.DAYS.between(dateFrom , dateTo);
                double mouths = Math.round(ChronoUnit.DAYS.between(dateFrom , dateTo) / 7.0 * 100.0) / 100.0;

                result += "Od "+ dateFrom.format(dataf) + " do " + dateTo.format( dataf) + "\n";
                result += "- mija: " + days + " " + (days != 1 ? "dni" : "dzień")
                        + ", tygodni " + mouths + "\n";
                result += Kalendarz(dateFrom, dateTo);

            } else {
                LocalDateTime dateFrom = LocalDateTime.parse(from);
                LocalDateTime dateTo = LocalDateTime.parse(to);

                ZonedDateTime startZonedDateTime = ZonedDateTime.of(dateFrom, ZoneId.of("Europe/Warsaw"));
                ZonedDateTime endZonedDateTime = ZonedDateTime.of(dateTo, ZoneId.of("Europe/Warsaw"));

                int days = (int) Math.round(ChronoUnit.HOURS.between(startZonedDateTime, endZonedDateTime) / 24.0);
                double mouths = Math.round(Math.round(ChronoUnit.HOURS.between(startZonedDateTime, endZonedDateTime) / 24.0) / 7.0 * 100) / 100.0;
                int hours = (int) ChronoUnit.HOURS.between(startZonedDateTime, endZonedDateTime);
                int minutes = (int) ChronoUnit.MINUTES.between(startZonedDateTime, endZonedDateTime);

                result += "Od "+ dateFrom.format(datat) + " do " + dateTo.format(datat) + "\n";
                result += "- mija: " + days + " " + (days != 1 ? "dni" : "dzień")
                        + ", tygodni " + mouths + "\n";
                result += "- godzin: " + hours + ", minut: " + minutes + "\n";
                result += Kalendarz(dateFrom.toLocalDate(), dateTo.toLocalDate());


            }
        } catch (DateTimeException e){
            return "*** " + e;
        }
        return result;
    }

    private static String Kalendarz(LocalDate dateFrom, LocalDate dateTo) {
        if (ChronoUnit.DAYS.between(dateFrom, dateTo) >= 1) {
            Period period = Period.between(dateFrom, dateTo);
            StringBuilder result = new StringBuilder("- kalendarzowo: ");

            if (period.getYears() > 0) {
                result.append(period.getYears()).append(" ");
                if (period.getYears() == 1) {
                    result.append("rok");
                } else if (period.getYears() > 1 && period.getYears() < 5) {
                    result.append("lata");
                } else {
                    result.append("lat");
                }
                if (period.getMonths() > 0 || period.getDays() > 0) {
                    result.append(", ");
                }
            }

            if (period.getMonths() > 0) {
                result.append(period.getMonths()).append(" ");
                if (period.getMonths() == 1) {
                    result.append("miesiąc");
                } else if (period.getMonths() > 1 && period.getMonths() < 5) {
                    result.append("miesiące");
                } else {
                    result.append("miesięcy");
                }
                if (period.getDays() > 0) {
                    result.append(", ");
                }
            }

            if (period.getDays() > 0) {
                result.append(period.getDays()).append(" ").append((period.getDays() == 1) ? "dzień" : "dni");
            }

            return result.toString();
        }

        return "";
    }




}
