package Citadelle.teamU.moteurjeu;

import Citadelle.teamU.moteurjeu.bots.*;
import Citadelle.teamU.moteurjeu.bots.malin.BotConstruitChere;
import Citadelle.teamU.moteurjeu.bots.malin.BotConstruitVite;
import Citadelle.teamU.moteurjeu.bots.malin.BotFocusRoi;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.beust.jcommander.JCommander;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.util.logging.Level;

public class Jeu {

    private List<Bot> botListe;
    private static Tour tour;
    // pv = pourcentage Victoire
    static float pVConstruitChere;
    static float pVConstruitVite;
    static float pVAleatoire;
    static float pVFocusRoi;
    static float pVNull;
    //pt = points
    static float ptConstruitChere;
    static float ptConstruitVite;
    static float ptAleatoire;
    static float ptFocusRoi;
    static float ptNull;
    //ptV = point en cas de Vicoire
    static float ptVConstruitChere;
    static float ptVConstruitVite;
    static float ptVAleatoire;
    static float ptVFocusRoi;
    static float ptVNull;
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



    public static void simulation1(int nombre,boolean csv){
        int i=1;
        int cptConstruitChere=0;
        int cptConstruitVite=0;
        int cptAleatoire=0;
        int cptQuiFocusRoi=0;
        int cptNull = 0;
        ptConstruitChere=0;
        ptConstruitVite=0;
        ptAleatoire=0;
        ptFocusRoi=0;
        ptNull = 0;
        ptVConstruitChere=0;
        ptVConstruitVite=0;
        ptVAleatoire=0;
        ptVFocusRoi=0;
        ptVNull = 0;
        while(i<nombre){
            Pioche pioche = new Pioche();
            Bot bot1 = new BotFocusRoi(pioche);
            Bot bot2 = new BotConstruitChere(pioche);
            Bot bot3 = new BotConstruitVite(pioche);
            Bot bot4 = new BotAleatoire(pioche);
            //On donne l'ordre dans lequel ils jouent 1->2->3->4->1...
            JouerPartie(bot1,bot2,bot3,bot4);
            Bot vainqueur=tour.getLeVainqueur();
            ptFocusRoi+=bot1.getScore();
            ptConstruitChere+=bot2.getScore();
            ptConstruitVite+=bot3.getScore();
            ptAleatoire+=bot4.getScore();
            if(vainqueur==null){
                cptNull++;
                int max = 0;
                for (Bot bot : new ArrayList<Bot>(Arrays.asList(bot1,bot2,bot3,bot4))){
                    if(bot.getScore()>max){
                        max=bot.getScore();
                    }
                }
                ptNull+=max;
                ptVNull+=max;
            }
            else if(vainqueur.toString().contains("BotConstruitChere")){
                cptConstruitChere++;
                ptVConstruitChere+=bot2.getScore();
            }
            else if(vainqueur.toString().contains("Bot_qui_construit_vite")){
                cptConstruitVite++;
                ptVConstruitVite+=bot3.getScore();
            }
            else if(vainqueur.toString().contains("BotAleatoire")){
                cptAleatoire++;
                ptVAleatoire+=bot4.getScore();
            }
            else if(vainqueur.toString().contains("BotQuiFocusRoi")){
                cptQuiFocusRoi++;
                ptVFocusRoi+=bot1.getScore();
            }
            i++;
        }
        //pt par partie
        ptFocusRoi/=nombre;
        ptConstruitChere/=nombre;
        ptConstruitVite/=nombre;
        ptAleatoire/=nombre;
        ptNull/=cptNull;
        //points pas partie gagné
        ptVFocusRoi/=cptQuiFocusRoi;
        ptVConstruitChere/=cptConstruitChere;
        ptVConstruitVite/=cptConstruitVite;
        ptVAleatoire/=cptAleatoire;
        ptVNull/=cptNull;
        // pv = pourcentage Victoire
        pVConstruitChere =((float)cptConstruitChere/nombre)*100;
        pVFocusRoi =((float)cptQuiFocusRoi/nombre)*100;
        pVConstruitVite =((float)cptConstruitVite/nombre)*100;
        pVAleatoire =((float)cptAleatoire/nombre)*100;
        pVNull =((float)cptNull/nombre)*100;
        if(!csv){
            System.out.println("Comparaison des différents bots");
            System.out.println( "Taux de victoire : BotConstruitChere: "+ pVConstruitChere +"% ,BotQuiFocusRoi: "+ pVFocusRoi +"% ,Bot_qui_construit_vite :"+ pVConstruitVite +"% ,BotAleatoire :"+ pVAleatoire +"% ,Egalité : "+pVNull+"%");
            System.out.println( "Moyenne de points : BotConstruitChere: "+ ptConstruitChere +"points ,BotQuiFocusRoi: "+ ptFocusRoi +"points ,Bot_qui_construit_vite :"+ ptConstruitVite +"points ,BotAleatoire :"+ ptAleatoire +"points ,Egalité : "+ptNull+"points");
        }
    }
    public static void simulation2(int nombre){
        int i=1;
        int cptQuiFocusRoi1=0;
        int cptQuiFocusRoi2=0;
        int cptQuiFocusRoi3=0;
        int cptQuiFocusRoi4=0;
        int cptNull=0;

        while(i<=nombre){
            Pioche pioche = new Pioche();
            Bot bot1 = new BotFocusRoi(pioche);
            Bot bot2 = new BotFocusRoi(pioche);
            Bot bot3 = new BotFocusRoi(pioche);
            Bot bot4 = new BotFocusRoi(pioche);
            //On donne l'ordre dans lequel ils jouent 1->2->3->4->1...
            JouerPartie(bot1,bot2,bot3,bot4);
            Bot vainqueur=tour.getLeVainqueur();
            if (vainqueur!=null){
                int parse=Integer.parseInt(vainqueur.toString().substring(14));
                if (parse%4==1) cptQuiFocusRoi1++;
                if (parse%4==2) cptQuiFocusRoi2++;
                if (parse%4==3) cptQuiFocusRoi3++;
                if (parse%4==0) cptQuiFocusRoi4++;
            }else{
                cptNull++;
            }
            i++;
        }

        float pourcent1=((float)cptQuiFocusRoi1/nombre)*100;
        float pourcent2=((float)cptQuiFocusRoi2/nombre)*100;
        float pourcent3=((float)cptQuiFocusRoi3/nombre)*100;
        float pourcent4=((float)cptQuiFocusRoi4/nombre)*100;
        float pourcentnull=((float)cptNull/nombre)*100;
        System.out.println("Simulation de bot Roi");
        System.out.println( "BotQuiFocusRoi1: "+pourcent1+"% ,BotQuiFocusRoi2: "+pourcent2+"% ,BotQuiFocusRoi3 :"+pourcent3+"% ,BotQuiFocusRoi4 :"+pourcent4+"% , Egalité : "+pourcentnull+"%");

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
            //Faire une demo
            Logger.getLogger("LOGGER").getParent().setLevel(Level.ALL);

        }else if(arg.two){
            //faire 2 fois 1000 stats
            Logger.getLogger("LOGGER").getParent().setLevel(Level.OFF);
            simulation1(1000,false);
            simulation2(1000);
        }else if(arg.csv){
            Logger.getLogger("LOGGER").getParent().setLevel(Level.OFF);
            Path path = Paths.get("stats","gamestats.csv");
            updateCSV(path.toFile());
        }

        Pioche pioche = new Pioche();
        Bot bot1 = new BotFocusRoi(pioche);
        Bot bot2 = new BotConstruitChere(pioche);
        Bot bot3 = new BotConstruitVite(pioche);
        Bot bot4 = new BotAleatoire(pioche);

        //On donne l'ordre dans lequel ils jouent 1->2->3->4->1...
        //JouerPartie(bot1,bot2,bot3,bot4);



        
        //JouerPartie(bot1,bot2,bot3,bot4); // je pouvais pas l'appeler dans main sans mettre en static

    }

    private static void updateCSV(File file) {
        int nombre = 1000;
        try {
            //On lis d'abord les valeurs actuels
            CSVReader reader;
            reader = new CSVReader(new FileReader(file));
            List<String[]> allRows = reader.readAll();
            simulation1(nombre,true);
            float total = Float.parseFloat(allRows.get(6)[1]);
            float pVConstruitChereUpdate = (Float.parseFloat(allRows.get(1)[1])*total+pVConstruitChere*nombre)/(total+nombre);
            float pVConstruitViteUpdate = (Float.parseFloat(allRows.get(2)[1])*total+pVConstruitVite*nombre)/(total+nombre);
            float pVFocusRoiUpdate = (Float.parseFloat(allRows.get(3)[1])*total+pVFocusRoi*nombre)/(total+nombre);
            float pVAleatoireUpdate = (Float.parseFloat(allRows.get(4)[1])*total+pVAleatoire*nombre)/(total+nombre);
            float pVNullUpdate = (Float.parseFloat(allRows.get(5)[1])*total+pVNull*nombre)/(total+nombre);

            float ptConstruitChereUpdate = (Float.parseFloat(allRows.get(1)[2])*total+ptConstruitChere*nombre)/(total+nombre);
            float ptConstruitViteUpdate = (Float.parseFloat(allRows.get(2)[2])*total+ptConstruitVite*nombre)/(total+nombre);
            float ptFocusRoiUpdate = (Float.parseFloat(allRows.get(3)[2])*total+ptFocusRoi*nombre)/(total+nombre);
            float ptAleatoireUpdate = (Float.parseFloat(allRows.get(4)[2])*total+ptAleatoire*nombre)/(total+nombre);
            float ptNullUpdate = (Float.parseFloat(allRows.get(5)[2])*total+ptNull*nombre)/(total+nombre);

            float ptVConstruitChereUpdate = (Float.parseFloat(allRows.get(1)[2])*total+ptVConstruitChere*nombre)/(total+nombre);
            float ptVConstruitViteUpdate = (Float.parseFloat(allRows.get(2)[2])*total+ptVConstruitVite*nombre)/(total+nombre);
            float ptVFocusRoiUpdate = (Float.parseFloat(allRows.get(3)[2])*total+ptVFocusRoi*nombre)/(total+nombre);
            float ptVAleatoireUpdate = (Float.parseFloat(allRows.get(4)[2])*total+ptVAleatoire*nombre)/(total+nombre);
            float ptVNullUpdate = (Float.parseFloat(allRows.get(5)[2])*total+ptVNull*nombre)/(total+nombre);

            total+=nombre;

            CSVWriter writer = new CSVWriter(new FileWriter(file));

            writer.writeNext(new String[]{"Bot", "Pourcentage de victoire","Points à la fin", "Points en cas de victoire"},false);
            writer.writeNext(new String[]{"Bot construit chère", pVConstruitChereUpdate +"",ptConstruitChereUpdate+"",ptVConstruitChereUpdate+""},false);
            writer.writeNext(new String[]{"Bot construit vite", pVConstruitViteUpdate +"",ptConstruitViteUpdate+"",ptVConstruitViteUpdate+""},false);
            writer.writeNext(new String[]{"Bot qui focus Roi", pVFocusRoiUpdate +"",ptFocusRoiUpdate+"",ptVFocusRoiUpdate+""},false);
            writer.writeNext(new String[]{"Bot aléatoire", pVAleatoireUpdate +"",ptAleatoireUpdate+"",ptVAleatoireUpdate+""},false);
            writer.writeNext(new String[]{"Egalité", pVNullUpdate +"",ptNullUpdate+"",ptVNullUpdate+""},false);
            writer.writeNext(new String[]{"Total",total+""},false);
            writer.close();
            lireCSV(file);
        } catch (IOException e) {
            //On a pas encore le fichier
            CSVWriter writer = null;
            try {
                simulation1(nombre,true);
                writer = new CSVWriter(new FileWriter(file));
                writer.writeNext(new String[]{"Bot", "Pourcentage de victoire","Points à la fin", "Points en cas de victoire"},false);
                writer.writeNext(new String[]{"Bot construit chère", pVConstruitChere +"",ptConstruitChere+"",ptVConstruitChere+""},false);
                writer.writeNext(new String[]{"Bot construit vite", pVConstruitVite +"",ptConstruitVite+"",ptVConstruitVite+""},false);
                writer.writeNext(new String[]{"Bot qui focus Roi", pVFocusRoi +"",ptFocusRoi+"",ptVFocusRoi+""},false);
                writer.writeNext(new String[]{"Bot aléatoire", pVAleatoire +"",ptAleatoire+"",ptVAleatoire+""},false);
                writer.writeNext(new String[]{"Egalité", pVNull +"",ptNull+"",ptVNull+""},false);
                writer.writeNext(new String[]{"Total",nombre+""},false);
                writer.close();
                lireCSV(file);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } catch (CsvException e) {
            throw new RuntimeException("probleme CSV");
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
