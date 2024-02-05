package Citadelle.teamU.moteurjeu;

import Citadelle.teamU.moteurjeu.bots.*;
import Citadelle.teamU.moteurjeu.bots.malin.BotConstruitChere;
import Citadelle.teamU.moteurjeu.bots.malin.BotConstruitVite;
import Citadelle.teamU.moteurjeu.bots.malin.BotFocusRoi;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.beust.jcommander.JCommander;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Jeu {

    private List<Bot> botListe;
    private static Tour tour;
    static float cptConstruitChere;
    static float cptConstruitVite;
    static float cptAleatoire;
    static float cptQuiFocusRoi;

    public Jeu(Bot...bots) {
        if(bots.length == 0){
            throw new IllegalArgumentException();
        }
        botListe = new ArrayList<>();
        botListe.addAll(Arrays.asList(bots));
        this.tour = new Tour(botListe);
    }

    public void start(){

        int maxQuartiersConstruits = 0;
        while(maxQuartiersConstruits < 8) {
            tour.prochainTour();
            for (Bot bot: botListe){
                if(bot.getQuartiersConstruits().size() > maxQuartiersConstruits){
                    maxQuartiersConstruits = bot.getQuartiersConstruits().size();
                }
            }
        }
    }
    public List<Bot> getBotListe() {
        return botListe;
    }



    public static void simulation(int nombre,boolean csv){
        int i=1;
        cptConstruitChere=0;
        cptConstruitVite=0;
        cptAleatoire=0;
        cptQuiFocusRoi=0;
        while(i<nombre){
            Pioche pioche = new Pioche();
            Bot bot1 = new BotFocusRoi(pioche);
            Bot bot2 = new BotConstruitChere(pioche);
            Bot bot3 = new BotConstruitVite(pioche);
            Bot bot4 = new BotAleatoire(pioche);
            //On donne l'ordre dans lequel ils jouent 1->2->3->4->1...
            JouerPartie(bot1,bot2,bot3,bot4);
            Bot vainqueur=tour.getLeVainqueur();
            //System.out.println(vainqueur);
            //System.out.println(vainqueur.toString());
            if(vainqueur.toString().contains("BotConstruitChere")){
                cptConstruitChere++;
            }
            if(vainqueur.toString().contains("Bot_qui_construit_vite")){
                cptConstruitVite++;
            }
            if(vainqueur.toString().contains("BotAleatoire")){
                cptAleatoire++;
            }
            if(vainqueur.toString().contains("BotQuiFocusRoi")){
                cptQuiFocusRoi++;
            }
            i++;
        }
        if(!csv) System.out.println( "BotConstruitChere: "+(cptConstruitChere/1000)*100+"% ,BotQuiFocusRoi: "+(cptQuiFocusRoi/1000)*100+"% ,Bot_qui_construit_vite :"+(cptConstruitVite/1000)*100+",% BotAleatoire :"+(cptAleatoire/1000)*100+"%");
    }
    // j'ai mis en static parce que ça me faisait une erreur
    public static void JouerPartie(Bot bot1, Bot bot2, Bot bot3, Bot bot4){
        bot1.setOrdreChoixRole(1);
        bot2.setOrdreChoixRole(2);
        bot3.setOrdreChoixRole(3);
        bot4.setOrdreChoixRole(4);
        Jeu jeu = new Jeu(bot1, bot2, bot3, bot4);
        jeu.start();
    }
    public static void main (String... args){

        Args arg = new Args();
        JCommander.newBuilder()
                .addObject(arg)
                .build()
                .parse(args);

        if(arg.demo){
            System.out.println("demo detecté");
            Logger.getLogger("LOGGER").getParent().setLevel(Level.ALL);
            //Faire une demo
        }else if(arg.two){
            System.out.println("2 thousand detecté");
            Logger.getLogger("LOGGER").getParent().setLevel(Level.OFF);
            simulation(1000,false);
            //faire 2 fois 1000 stats
        }else if(arg.csv){
            Logger.getLogger("LOGGER").getParent().setLevel(Level.OFF);
            Path path = Paths.get("stats","gamestats.csv");
            updateCSV(path.toFile());
        }


        //On donne l'ordre dans lequel ils jouent 1->2->3->4->1...
        //JouerPartie(bot1,bot2,bot3,bot4); // je pouvais pas l'appeler dans main sans mettre en static

    }

    private static void updateCSV(File file) {
        try {
            int nombre = 1000;
            //On lis d'abord les valeurs actuels
            CSVReader reader;
            reader = new CSVReader(new FileReader(file));
            List<String[]> allRows = reader.readAll();
            float cptConstruitChereAvant = Float.parseFloat(allRows.get(0)[1])/nombre;
            float cptConstruitViteAvant = Float.parseFloat(allRows.get(1)[1])/nombre;
            float cptAleatoireAvant = Float.parseFloat(allRows.get(2)[1])/nombre;
            float cptQuiFocusRoiAvant = Float.parseFloat(allRows.get(3)[1])/nombre;
            float total = Float.parseFloat(allRows.get(4)[1]);


            CSVWriter writer = new CSVWriter(new FileWriter(file));

            simulation(nombre,true);



            writer.writeNext(new String[]{"Bot construit chère",cptConstruitChere+""},false);
            writer.writeNext(new String[]{"Bot construit vite",cptConstruitVite+""},false);
            writer.writeNext(new String[]{"Bot qui focus Roi",cptQuiFocusRoi+""},false);
            writer.writeNext(new String[]{"Bot aléatoire",cptAleatoire+""},false);
            int total2 = (int) (cptConstruitChere + cptConstruitVite + cptQuiFocusRoi + cptAleatoire);
            writer.writeNext(new String[]{"Total",1000+""},false);
            writer.close();
            lireCSV(file);
        } catch (IOException e) {
            throw new RuntimeException("probleme ecriture");
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }

    private static void lireCSV(File file) {
        //Build reader instance
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(file));
            //Read all rows at once
            List<String[]> allRows = reader.readAll();

            //Read CSV line by line and use the string array as you want
            for (String[] row : allRows) {
                System.out.println(Arrays.toString(row));
            }
        } catch (FileNotFoundException e) {
            System.out.println("erreur 1");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("erreur 2");
            throw new RuntimeException(e);
        } catch (CsvException e) {
            System.out.println("erreur 3");
            throw new RuntimeException(e);
        }
    }
}
