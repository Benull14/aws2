package com.aws.service;

import com.aws.model.Aufgabe;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AufgabenService {
    private List<Aufgabe> aufgabenListe;
    private static final String DATEI_PFAD = "data/aufgaben.txt";

    public AufgabenService() {
        this.aufgabenListe = new ArrayList<>();
        datenLaden();
    }

    // Methode zum Hinzufügen einer Aufgabe
    public void aufgabeHinzufuegen(Aufgabe aufgabe) {
        aufgabenListe.add(aufgabe);
        System.out.println("Aufgabe hinzugefügt: " + aufgabe.getTitel());
    }

    // Getter für die Aufgabenliste
    public List<Aufgabe> getAufgabenListe() {
        return aufgabenListe;
    }

    // Methode zum Aktualisieren des Aufgabenstatus
    public boolean aufgabeAlsErledigtMarkieren(String titel) {
        for (Aufgabe aufgabe : aufgabenListe) {
            if (aufgabe.getTitel().equalsIgnoreCase(titel)) {
                aufgabe.setErledigt(true);
                System.out.println("Aufgabe als erledigt markiert: " + titel);
                return true;
            }
        }
        System.out.println("Aufgabe nicht gefunden: " + titel);
        return false;
    }

    // Neue Methode: Aufgaben suchen
    public List<Aufgabe> aufgabenSuchen(String suchbegriff) {
        return aufgabenListe.stream()
                .filter(a -> a.getTitel().toLowerCase().contains(suchbegriff.toLowerCase())
                        || a.getBeschreibung().toLowerCase().contains(suchbegriff.toLowerCase())
                        || (a.isErledigt() ? "erledigt" : "offen").contains(suchbegriff.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Neue Methode: Priorität einer Aufgabe aktualisieren
    public boolean prioritaetAktualisieren(String titel, int neuePrioritaet) {
        for (Aufgabe aufgabe : aufgabenListe) {
            if (aufgabe.getTitel().equalsIgnoreCase(titel)) {
                aufgabe.setPrioritaet(neuePrioritaet);
                System.out.println("Priorität aktualisiert für: " + titel);
                return true;
            }
        }
        System.out.println("Aufgabe nicht gefunden: " + titel);
        return false;
    }

    // Neue Methode: Aufgabe löschen
    public boolean aufgabeLoeschen(String titel) {
        for (int i = 0; i < aufgabenListe.size(); i++) {
            Aufgabe aufgabe = aufgabenListe.get(i);
            if (aufgabe.getTitel().equalsIgnoreCase(titel)) {
                aufgabenListe.remove(i);
                System.out.println("Aufgabe gelöscht: " + titel);
                return true;
            }
        }
        System.out.println("Aufgabe nicht gefunden: " + titel);
        return false;
    }

    // Datenpersistenz: Daten speichern
    public void datenSpeichern() {
        try {
            File datei = new File(DATEI_PFAD);
            File verzeichnis = datei.getParentFile();

            if (verzeichnis != null && !verzeichnis.exists()) {
                verzeichnis.mkdirs(); // Erstellt alle nicht existierenden Verzeichnisse
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(datei))) {
                oos.writeObject(aufgabenListe);
                System.out.println("Daten wurden erfolgreich gespeichert.");
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Speichern der Daten: " + e.getMessage());
        }
    }

    // Datenpersistenz: Daten laden
    @SuppressWarnings("unchecked")
    private void datenLaden() {
        File datei = new File(DATEI_PFAD);
        if (datei.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(datei))) {
                aufgabenListe = (List<Aufgabe>) ois.readObject();
                System.out.println("Daten wurden erfolgreich geladen.");
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Fehler beim Laden der Daten: " + e.getMessage());
            }
        } else {
            System.out.println("Keine vorhandenen Daten zum Laden gefunden.");
        }
    }

}
