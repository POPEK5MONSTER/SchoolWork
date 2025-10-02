/**
 *
 *  @author Popowski Mateusz S27485
 *
 */

package zad1;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Currency;
import java.util.Locale;
import java.util.stream.Collectors;

public class Service {

    private Locale locale;
    public String country;
    public String currencyCodeCuntry;
    public String city;

    public Service(String country) {
        this.country = country;
        this.locale = getLocaleFromCountry(country);

    }

    private Locale getLocaleFromCountry(String country) {
        for (Locale locale : Locale.getAvailableLocales())
            if (country.equalsIgnoreCase(locale.getDisplayCountry(Locale.ENGLISH))) {
                return new Locale("", locale.getCountry());
            }
        return null;
    }

    public String getWeather(String cityName) {
        String apiKey = "d6573267629f4e82be6704386fa2265e";
        city = cityName;

        String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s,%s&appid=%s&units=metric",
                cityName, locale.getCountry(), apiKey);
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        new URI(url).toURL().openConnection().getInputStream())
                );
        ) {
            String json = reader.lines().collect(Collectors.joining());
            return json;
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Złe dane wejściowe!");
        }

    }

    public double getRateFor(String currencyCode) {
        currencyCodeCuntry = currencyCode;
        String url = String.format("https://v6.exchangerate-api.com/v6/78617881982257494e9fcf46/pair/%s/%s", getCurrencyCodeCuntry(), currencyCode);
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        new URI(url).toURL().openConnection().getInputStream())
                );
        ) {
            String json = reader.lines().collect(Collectors.joining());
            rateFor rate = new Gson().fromJson(json, rateFor.class);

            return rate.conversion_rate;
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Złe dane wejściowe!");
        }

    }

    public Double getNBPRate() {

        if (getCurrencyCodeCuntry().equals("PLN")) {
            return 1.0;
        }
        String tab = "a";
        String url = String.format("http://api.nbp.pl/api/exchangerates/rates/%s/%s/?format=json",tab ,getCurrencyCodeCuntry());
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        new URI(url).toURL().openConnection().getInputStream())
                );
        ) {
            String json = reader.lines().collect(Collectors.joining());
            NBP nbp = new Gson().fromJson(json, NBP.class);
            Rate rate = nbp.rates[0];
            return rate.mid;
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Złe dane wejściowe!");
        }
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getCurrencyCodeCuntry(){
        return Currency.getInstance(locale).getCurrencyCode();
    }

}
class Weather{
    WeatherMain main;
}
class WeatherMain{
    double temp;
}
class rateFor{
    double conversion_rate;
}

    class NBP{
        Rate[] rates;
    }
    class Rate{
        double mid;
    }


