package Citadelle.teamU.moteurjeu;

import Citadelle.teamU.moteurjeu.bots.*;
import Citadelle.teamU.moteurjeu.bots.malin.BotConstruitChere;
import Citadelle.teamU.moteurjeu.bots.malin.BotConstruitVite;
import Citadelle.teamU.moteurjeu.bots.malin.BotFocusRoi;
import Citadelle.teamU.moteurjeu.bots.malin.BotRichard;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
    //ordre dans la liste : 0: construit chere, 1: construit vite, 2: Aleatoire, 3: focus Roi, 4: égalité
    static  ArrayList<Float> pourcentageVictoire = new ArrayList<>(Arrays.asList((float) 0, (float) 0, (float) 0, (float) 0, (float) 0));
    static ArrayList<Float> moyennePoints= new ArrayList<>(Arrays.asList((float) 0, (float) 0, (float) 0, (float) 0, (float) 0));
    static ArrayList<Float> moyennePointsVictoire= new ArrayList<>(Arrays.asList((float) 0, (float) 0, (float) 0, (float) 0, (float) 0));
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
        while(i<nombre){
            Pioche pioche = new Pioche();
            Bot bot1 = new BotFocusRoi(pioche);
            Bot bot2 = new BotConstruitChere(pioche);
            Bot bot3 = new BotConstruitVite(pioche);
            Bot bot4 = new BotAleatoire(pioche);
            //On donne l'ordre dans lequel ils jouent 1->2->3->4->1...
            JouerPartie(bot1,bot2,bot3,bot4);
            Bot vainqueur=tour.getLeVainqueur();
            remplirListe(moyennePoints,moyennePoints.get(0)+bot2.getScore(),moyennePoints.get(1)+bot3.getScore(),moyennePoints.get(2)+bot4.getScore(),moyennePoints.get(3)+bot1.getScore(),(float) 0);
            if(vainqueur==null){
                cptNull++;
                int max = 0;
                for (Bot bot : new ArrayList<Bot>(Arrays.asList(bot1,bot2,bot3,bot4))){
                    if(bot.getScore()>max){
                        max=bot.getScore();
                    }
                }
                moyennePointsVictoire.set(4,moyennePointsVictoire.get(4)+max);
                moyennePoints.set(4,moyennePoints.get(4)+max);
            }
            else if(vainqueur.toString().contains("BotConstruitChere")){
                cptConstruitChere++;
                moyennePointsVictoire.set(0,moyennePointsVictoire.get(0)+bot2.getScore());
            }
            else if(vainqueur.toString().contains("Bot_qui_construit_vite")){
                cptConstruitVite++;
                moyennePointsVictoire.set(1,moyennePointsVictoire.get(1)+bot3.getScore());
            }
            else if(vainqueur.toString().contains("BotAleatoire")){
                cptAleatoire++;
                moyennePointsVictoire.set(2,moyennePointsVictoire.get(2)+bot4.getScore());
            }
            else if(vainqueur.toString().contains("BotQuiFocusRoi")){
                cptQuiFocusRoi++;
                moyennePointsVictoire.set(3,moyennePointsVictoire.get(3)+bot1.getScore());
            }
            i++;
        }
        remplirListe(moyennePoints,moyennePoints.get(0)/nombre,moyennePoints.get(1)/nombre,moyennePoints.get(2)/nombre,moyennePoints.get(3)/nombre,moyennePoints.get(4)/cptNull);
        remplirListe(moyennePointsVictoire,moyennePointsVictoire.get(0)/cptConstruitChere,moyennePointsVictoire.get(1)/cptConstruitVite,moyennePointsVictoire.get(2)/cptAleatoire,moyennePointsVictoire.get(3)/cptQuiFocusRoi,moyennePointsVictoire.get(4)/cptNull);
        remplirListe(pourcentageVictoire,((float)cptConstruitChere/nombre)*100,((float)cptConstruitVite/nombre)*100,((float)cptAleatoire/nombre)*100,((float)cptQuiFocusRoi/nombre)*100,((float)cptNull/nombre)*100);
        if(!csv){
            System.out.println("Comparaison des différents bots");
            System.out.println( "Taux de victoire : BotConstruitChere: "+ pourcentageVictoire.get(0) +"% ,BotQuiFocusRoi: "+ pourcentageVictoire.get(3) +"% ,Bot_qui_construit_vite :"+ pourcentageVictoire.get(1) +"% ,BotAleatoire :"+ pourcentageVictoire.get(2) +"%");
            System.out.println( "Taux de défaite : BotConstruitChere: "+ (100-pourcentageVictoire.get(0)) +"% ,BotQuiFocusRoi: "+ (100-pourcentageVictoire.get(3)) +"% ,Bot_qui_construit_vite :"+ (100-pourcentageVictoire.get(1)) +"% ,BotAleatoire :"+ (100-pourcentageVictoire.get(2)) +"%");
            System.out.println( "Taux d'égalité : "+pourcentageVictoire.get(4)+"%");
            System.out.println( "Score moyen : BotConstruitChere: "+ moyennePoints.get(0) +" ,BotQuiFocusRoi: "+ moyennePoints.get(3) +" ,Bot_qui_construit_vite :"+ moyennePoints.get(1) +" ,BotAleatoire :"+ moyennePoints.get(2) +" ,Egalité : "+moyennePoints.get(4));
        }
    }
    public static void simulation2(int nombre){
        int i=1;
        ArrayList<Float> cptPoints = new ArrayList<>(Arrays.asList((float)0,(float)0,(float)0,(float)0,(float)0));
        ArrayList<Float> cptVictoire = new ArrayList<>(Arrays.asList((float)0,(float)0,(float)0,(float)0,(float)0));

        while(i<=nombre){
            Pioche pioche = new Pioche();
            Bot bot1 = new BotFocusRoi(pioche);
            Bot bot2 = new BotFocusRoi(pioche);
            Bot bot3 = new BotFocusRoi(pioche);
            Bot bot4 = new BotFocusRoi(pioche);
            //On donne l'ordre dans lequel ils jouent 1->2->3->4->1...
            JouerPartie(bot1,bot2,bot3,bot4);
            Bot vainqueur=tour.getLeVainqueur();
            remplirListe(cptPoints,cptPoints.get(0)+bot1.getScore(),cptPoints.get(1)+bot2.getScore(),cptPoints.get(2)+bot3.getScore(),cptPoints.get(3)+bot4.getScore(),(float) 0);
            if (vainqueur!=null){
                int parse=Integer.parseInt(vainqueur.toString().substring(14));
                if (parse%4==1)cptVictoire.set(0,cptVictoire.get(0)+1);
                if (parse%4==2)cptVictoire.set(1,cptVictoire.get(1)+1);
                if (parse%4==3)cptVictoire.set(2,cptVictoire.get(2)+1);
                if (parse%4==0)cptVictoire.set(3,cptVictoire.get(3)+1);
            }else{
                cptVictoire.set(4,cptVictoire.get(4)+1);
                int max = 0;
                for (Bot bot : new ArrayList<Bot>(Arrays.asList(bot1,bot2,bot3,bot4))){
                    if(bot.getScore()>max){
                        max=bot.getScore();
                    }
                }
                cptPoints.set(4,cptPoints.get(4)+max);
            }
            i++;
        }

        float pourcent1=(cptVictoire.get(0)/nombre)*100;
        float pourcent2=(cptVictoire.get(1)/nombre)*100;
        float pourcent3=(cptVictoire.get(2)/nombre)*100;
        float pourcent4=(cptVictoire.get(3)/nombre)*100;
        float pourcentnull=(cptVictoire.get(4)/nombre)*100;
        System.out.println("\nSimulation de bot Roi");
        System.out.println("Taux de victoire : BotQuiFocusRoi1: "+pourcent1+"% ,BotQuiFocusRoi2: "+pourcent2+"% ,BotQuiFocusRoi3 :"+pourcent3+"% ,BotQuiFocusRoi4 :"+pourcent4+"%");
        System.out.println("Taux de défaite : BotQuiFocusRoi1: "+(100-pourcent1)+"% ,BotQuiFocusRoi2: "+(100-pourcent2)+"% ,BotQuiFocusRoi3: "+(100-pourcent3)+"% ,BotQuiFocusRoi4: "+(100-pourcent4)+"%");
        System.out.println("Taux d'égalité: "+pourcentnull+"%");
        System.out.println("Score moyen : BotQuiFocusRoi1: "+(cptPoints.get(0)/nombre)+" ,BotQuiFocusRoi2: "+(cptPoints.get(1)/nombre)+" ,BotQuiFocusRoi3: "+(cptPoints.get(2)/nombre)+" ,BotQuiFocusRoi4: "+(cptPoints.get(3)/nombre)+" ,Egalité: "+(cptPoints.get(4)/cptVictoire.get(4)));
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
        Bot bot2 = new BotRichard(pioche);
        Bot bot3 = new BotConstruitVite(pioche);
        Bot bot4 = new BotAleatoire(pioche);

        //On donne l'ordre dans lequel ils jouent 1->2->3->4->1...
        JouerPartie(bot1,bot2,bot3,bot4);




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
            float pVConstruitChereUpdate = (Float.parseFloat(allRows.get(1)[1])*total+ pourcentageVictoire.get(0) *nombre)/(total+nombre);
            float pVConstruitViteUpdate = (Float.parseFloat(allRows.get(2)[1])*total+pourcentageVictoire.get(1)*nombre)/(total+nombre);
            float pVFocusRoiUpdate = (Float.parseFloat(allRows.get(3)[1])*total+pourcentageVictoire.get(3)*nombre)/(total+nombre);
            float pVAleatoireUpdate = (Float.parseFloat(allRows.get(4)[1])*total+pourcentageVictoire.get(2)*nombre)/(total+nombre);
            float pVNullUpdate = (Float.parseFloat(allRows.get(5)[1])*total+pourcentageVictoire.get(4)*nombre)/(total+nombre);

            float ptConstruitChereUpdate = (Float.parseFloat(allRows.get(1)[3])*total+moyennePoints.get(0)*nombre)/(total+nombre);
            float ptConstruitViteUpdate = (Float.parseFloat(allRows.get(2)[3])*total+moyennePoints.get(1)*nombre)/(total+nombre);
            float ptFocusRoiUpdate = (Float.parseFloat(allRows.get(3)[3])*total+moyennePoints.get(3)*nombre)/(total+nombre);
            float ptAleatoireUpdate = (Float.parseFloat(allRows.get(4)[3])*total+moyennePoints.get(2)*nombre)/(total+nombre);
            float ptNullUpdate = (Float.parseFloat(allRows.get(5)[3])*total+moyennePoints.get(4)*nombre)/(total+nombre);

            float ptVConstruitChereUpdate = (Float.parseFloat(allRows.get(1)[4])*total+moyennePointsVictoire.get(0)*nombre)/(total+nombre);
            float ptVConstruitViteUpdate = (Float.parseFloat(allRows.get(2)[4])*total+moyennePointsVictoire.get(1)*nombre)/(total+nombre);
            float ptVFocusRoiUpdate = (Float.parseFloat(allRows.get(3)[4])*total+moyennePointsVictoire.get(3)*nombre)/(total+nombre);
            float ptVAleatoireUpdate = (Float.parseFloat(allRows.get(4)[4])*total+moyennePointsVictoire.get(2)*nombre)/(total+nombre);
            float ptVNullUpdate = (Float.parseFloat(allRows.get(5)[4])*total+moyennePointsVictoire.get(4)*nombre)/(total+nombre);


            total+=nombre;

            CSVWriter writer = new CSVWriter(new FileWriter(file, StandardCharsets.UTF_8));

            writer.writeNext(new String[]{"Simulations", "Pourcentage de victoire","Pourcentage de défaite","Score moyen", "Score moyen en cas de victoire"},false);
            writer.writeNext(new String[]{"Bot construit chère", pVConstruitChereUpdate +"",(100-pVConstruitChereUpdate) +"",ptConstruitChereUpdate+"",ptVConstruitChereUpdate+""},false);
            writer.writeNext(new String[]{"Bot construit vite", pVConstruitViteUpdate +"",(100-pVConstruitViteUpdate) +"",ptConstruitViteUpdate+"",ptVConstruitViteUpdate+""},false);
            writer.writeNext(new String[]{"Bot qui focus Roi", pVFocusRoiUpdate +"",(100-pVFocusRoiUpdate) +"",ptFocusRoiUpdate+"",ptVFocusRoiUpdate+""},false);
            writer.writeNext(new String[]{"Bot aléatoire", pVAleatoireUpdate +"",(100-pVAleatoireUpdate) +"",ptAleatoireUpdate+"",ptVAleatoireUpdate+""},false);
            writer.writeNext(new String[]{"Egalité", pVNullUpdate +"","--",ptNullUpdate+"",ptVNullUpdate+""},false);
            writer.writeNext(new String[]{"Nombre de simulations",total+""},false);
            writer.close();
        } catch (IOException e) {
            //On a pas encore le fichier
            CSVWriter writer = null;
            try {
                simulation1(nombre,true);
                writer = new CSVWriter(new FileWriter(file,StandardCharsets.UTF_8));
                writer.writeNext(new String[]{"Simulations", "Pourcentage de victoire","Pourcentage de défaite","Score moyen", "Score moyen en cas de victoire"},false);
                writer.writeNext(new String[]{"Bot construit chère", pourcentageVictoire.get(0) +"",(100-pourcentageVictoire.get(0))+"",moyennePoints.get(0)+"",moyennePointsVictoire.get(0)+""},false);
                writer.writeNext(new String[]{"Bot construit vite", pourcentageVictoire.get(1) +"",(100-pourcentageVictoire.get(1))+"",moyennePoints.get(1)+"",moyennePointsVictoire.get(1)+""},false);
                writer.writeNext(new String[]{"Bot qui focus Roi", pourcentageVictoire.get(3) +"",(100-pourcentageVictoire.get(3))+"",moyennePoints.get(3)+"",moyennePointsVictoire.get(3)+""},false);
                writer.writeNext(new String[]{"Bot aléatoire", pourcentageVictoire.get(2) +"",(100-pourcentageVictoire.get(2))+"",moyennePoints.get(2)+"",moyennePointsVictoire.get(2)+""},false);
                writer.writeNext(new String[]{"Egalité", pourcentageVictoire.get(4) +"","--",moyennePoints.get(4)+"",moyennePointsVictoire.get(4)+""},false);
                writer.writeNext(new String[]{"Nombre de simulations",nombre+""},false);
                writer.close();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } catch (CsvException e) {
            throw new RuntimeException("probleme CSV");
        }
    }
    private static void remplirListe(ArrayList<Float> list, float construitChere, float construitVite, float aleatoire, float focusRoi, float Null) {
        //list.clear();
        list.set(0,construitChere);
        list.set(1,construitVite);
        list.set(2,aleatoire);
        list.set(3,focusRoi);
        if(Null != 0) list.set(4,Null);
    }
}
