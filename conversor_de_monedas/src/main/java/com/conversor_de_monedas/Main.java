package com.conversor_de_monedas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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
    private static final ArrayList<String> menuOptions = new ArrayList<>();

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

        String menu = makeMenu();

        try (Scanner input = new Scanner(System.in)) {

            while (true) {

                System.out.println(menu);
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

}