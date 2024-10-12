package com.aws.view;

import com.aws.model.Aufgabe;
import com.aws.service.AufgabenService;

import java.util.List;
import java.util.Scanner;

public class KonsoleUI {
    private AufgabenService aufgabenService;
    private Scanner scanner;

    public KonsoleUI() {
        aufgabenService = new AufgabenService();
        scanner = new Scanner(System.in);
    }

    public void start() {
        boolean run = true;
        while (run) {
            menueAnzeigen();
            int wahl = leseIntegerEingabe();
            switch (wahl) {
                case 1:
                    aufgabeHinzufuegenUI();
                    break;
                case 2:
                    aufgabenAnzeigenUI();
                    break;
                case 3:
                    aufgabeErledigenUI();
                    break;
                case 4:
                    aufgabeSuchenUI();
                    break;
                case 5:
                    aufgabePriorisierenUI();
                    break;
                case 6:
                    aufgabeLoeschenUI();
                    break;
                case 0:
                    aufgabenService.datenSpeichern();
                    System.out.println("Anwendung wird beendet.");
                    run = false;
                    break;
                default:
                    System.out.println("Ungültige Auswahl. Bitte versuchen Sie es erneut.");
            }
        }
    }

    private void menueAnzeigen() {
        System.out.println("\n--- Aufgabenverwaltung ---");
        System.out.println("1. Aufgabe hinzufügen");
        System.out.println("2. Aufgaben anzeigen");
        System.out.println("3. Aufgabe als erledigt markieren");
        System.out.println("4. Aufgaben suchen");
        System.out.println("5. Priorität einer Aufgabe ändern");
        System.out.println("6. Aufgabe löschen");
        System.out.println("0. Beenden");
        System.out.print("Auswahl: ");
    }

    private void aufgabeHinzufuegenUI() {
        System.out.print("Titel: ");
        String titel = scanner.nextLine();
        System.out.print("Beschreibung: ");
        String beschreibung = scanner.nextLine();
        System.out.print("Priorität (1=Hoch, 2=Mittel, 3=Niedrig): ");
        int prioritaet = leseIntegerEingabe();

        Aufgabe aufgabe = new Aufgabe(titel, beschreibung, prioritaet);
        aufgabenService.aufgabeHinzufuegen(aufgabe);
    }

    private void aufgabenAnzeigenUI() {
        List<Aufgabe> liste = aufgabenService.getAufgabenListe();
        if (liste.isEmpty()) {
            System.out.println("Keine Aufgaben vorhanden.");
            return;
        }
        System.out.println("\n--- Aktuelle Aufgabenliste ---");
        aufgabenListeAnzeigen(liste);
    }

    private void aufgabeErledigenUI() {
        System.out.print("Titel der Aufgabe, die als erledigt markiert werden soll: ");
        String titel = scanner.nextLine();
        aufgabenService.aufgabeAlsErledigtMarkieren(titel);
    }

    private void aufgabeSuchenUI() {
        System.out.print("Suchbegriff: ");
        String suchbegriff = scanner.nextLine();
        List<Aufgabe> suchErgebnisse = aufgabenService.aufgabenSuchen(suchbegriff);
        if (suchErgebnisse.isEmpty()) {
            System.out.println("Keine Aufgaben gefunden.");
        } else {
            System.out.println("\n--- Suchergebnisse ---");
            aufgabenListeAnzeigen(suchErgebnisse);
        }
    }

    private void aufgabePriorisierenUI() {
        System.out.print("Titel der Aufgabe, deren Priorität geändert werden soll: ");
        String titel = scanner.nextLine();
        System.out.print("Neue Priorität (1=Hoch, 2=Mittel, 3=Niedrig): ");
        int prioritaet = leseIntegerEingabe();
        aufgabenService.prioritaetAktualisieren(titel, prioritaet);
    }

    private void aufgabeLoeschenUI() {
        System.out.print("Titel der zu löschenden Aufgabe: ");
        String titel = scanner.nextLine();
        System.out.print("Sind Sie sicher, dass Sie die Aufgabe löschen möchten? (j/n): ");
        String bestaetigung = scanner.nextLine();
        if (bestaetigung.equalsIgnoreCase("j")) {
            aufgabenService.aufgabeLoeschen(titel);
        } else {
            System.out.println("Löschvorgang abgebrochen.");
        }
    }

    private void aufgabenListeAnzeigen(List<Aufgabe> liste) {
        System.out.printf("%-20s | %-10s | %-8s | %s\n", "Titel", "Priorität", "Status", "Beschreibung");
        System.out.println("----------------------------------------------------------------------");
        for (Aufgabe aufgabe : liste) {
            System.out.println(aufgabe);
        }
    }

    private int leseIntegerEingabe() {
        while (true) {
            try {
                int eingabe = Integer.parseInt(scanner.nextLine());
                return eingabe;
            } catch (NumberFormatException e) {
                System.out.print("Ungültige Eingabe. Bitte eine Zahl eingeben: ");
            }
        }
    }
}
