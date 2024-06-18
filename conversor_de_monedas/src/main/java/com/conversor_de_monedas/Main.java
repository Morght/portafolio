package com.conversor_de_monedas;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class Main {

    // To add currency codes keep the format "123 - Name1 Name2" or uncomment
    // Comment line for remove...
    private static final String[] CURRENCY_CODES = {
            "USD - Dolar",
            "BOB - Bolivian boliviano",
            "BRL - Real brasileño",
            // "CLP - Peso chileno",
            // "COP - Peso colombiano",
            "ARS - Peso argentino"
    };
    private static final String BASE_CODE = "USD";
    private static final ArrayList<String> menuOptions = new ArrayList<>();
    private static JsonObject conversionRates;

    // Visual reference
    // private static final String MENU = """
    // ==============================================
    // || Eliga el numero de la opcion deseada:
    // ||
    // || 1) Dolar --> Peso argentino
    // || 2) Peso argentino --> Dolar
    // || 3) Dolar --> Bolivian boliviano
    // || 4) Bolivian boliviano --> Dolar
    // || 5) Dolar --> Real brasileño
    // || 6) Real brasileño --> Dolar
    // || 7) Salir
    // ||
    // ==============================================
    // """;

    public static void main(String[] args) throws IOException, InterruptedException {

        conversionRates = getConversionRates();
        String menu = makeMenu();
        String userChoice;
        int optionSelected = 0;
        String userQuantity;

        try (Scanner input = new Scanner(System.in)) {

            while (true) {

                System.out.println(menu);
                userChoice = input.next();
                System.out.println("");

                try {
                    optionSelected = Integer.parseInt(userChoice);

                    if (optionSelected > 0 && optionSelected < (menuOptions.size() + 1)) {

                        String[] currencyNames = menuOptions.get(optionSelected - 1).split(" --> ");
                        System.out.println("++Ingresa la cantidad de " + currencyNames[0] + " a convertir:");
                        userQuantity = input.next();

                        try {
                            BigDecimal quantity = new BigDecimal(userQuantity);

                            // TODO convertion manager
                            // String result = convert(optionSelected, quantity);
                            System.out.println(
                                    "+++++ " + NumberFormat.getCurrencyInstance().format(quantity) + " "
                                            + currencyNames[0] + " son "
                                            // + result + " "
                                            + currencyNames[1] + "+++++");

                        } catch (NumberFormatException e) {
                            System.out.println("*---La cantidad no es valida---*");
                        }

                    } else if (optionSelected == menuOptions.size() + 1) {
                        System.out.print("""
                                **** Saliendo ****

                                """);
                        break;
                    } else {
                        System.out.println("*---Opcion no valida---*");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("*---Opcion no valida---*");
                }
            }
        }

    }

    private static String makeMenu() {

        String header = """
                ================================================
                || Eliga el numero de la opcion deseada:
                ||
                """;
        String ending = """
                ||
                ================================================
                """;

        String menu = header;
        String base = CURRENCY_CODES[0].substring(6);

        // Create menuOptions
        for (int i = 1; i < CURRENCY_CODES.length; i++) {
            menuOptions.add(base + " --> " + CURRENCY_CODES[i].substring(6));
            menuOptions.add(CURRENCY_CODES[i].substring(6) + " --> " + base);
        }

        // Add menuOptions to menu
        for (int i = 0; i < menuOptions.size(); i++) {
            menu += "|| " + (i + 1) + ") " + menuOptions.get(i) + "\n";
        }

        // Add exit option
        menu += "|| " + (menuOptions.size() + 1) + ") Salir \n";
        menu += ending;

        return menu;
    }

    private static JsonObject getConversionRates() throws IOException, InterruptedException {
        // URL
        String url_str = "https://v6.exchangerate-api.com/v6/536c388e34d2fdbf10b5e647/latest/" + BASE_CODE;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url_str))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject jsonObjectT = null;

        try (JsonReader jsonReader = new JsonReader(new StringReader(response.body()))) {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String fieldName = jsonReader.nextName();
                if (!fieldName.equals("conversion_rates")) {
                    jsonReader.skipValue();
                } else {
                    JsonElement jsonElementT = JsonParser.parseReader(jsonReader);
                    jsonObjectT = jsonElementT.getAsJsonObject();
                }
            }
            jsonReader.endObject();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

        return jsonObjectT;
    }
}