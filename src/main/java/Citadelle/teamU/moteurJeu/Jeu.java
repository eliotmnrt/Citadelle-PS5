package Citadelle.teamU.moteurJeu;

import Citadelle.teamU.moteurJeu.bots.*;
import Citadelle.teamU.moteurJeu.bots.malin.*;

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

@SuppressWarnings("java:S106")
public class Jeu {

    private final List<Bot> botListe;
    private static Tour tour;
    //ordre dans la liste : 0: construit chere, 1: richard, 2: Aleatoire, 3: focus Roi, 4: égalité
    static  ArrayList<Float> pourcentageVictoire = new ArrayList<>(Arrays.asList((float) 0, (float) 0, (float) 0, (float) 0, (float) 0));
    static ArrayList<Float> moyennePoints= new ArrayList<>(Arrays.asList((float) 0, (float) 0, (float) 0, (float) 0, (float) 0));
    static ArrayList<Float> moyennePointsVictoire= new ArrayList<>(Arrays.asList((float) 0, (float) 0, (float) 0, (float) 0, (float) 0));
    static ArrayList<Bot> listeBot = new ArrayList<>(Arrays.asList(null,null,null,null));
    static int cptBot0=0;
    static int cptBot1=0;
    static int cptBot2=0;
    static int cptBot3=0;
    static int cptNull = 0;


    public Jeu(Bot...bots) {
        if(bots.length == 0){
            throw new IllegalArgumentException();
        }
        botListe = new ArrayList<>();
        botListe.addAll(Arrays.asList(bots));
        tour = new Tour(botListe);
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


    /**
     * sert pour les simulations de jeux entre différents bots
     * @param nombre nombre de parties
     * @param csv booleen si ajout de stats au .csv
     */
    public static void simulation1(int nombre,boolean csv){
        int i=1;
        cptBot0 = 0;
        cptBot1 = 0;
        cptBot2 = 0;
        cptBot3 = 0;
        while(i<nombre){
            Pioche pioche = new Pioche();
            listeBot.set(0,new BotFocusRoi(pioche));
            listeBot.set(1,new BotConstruitChere(pioche));
            listeBot.set(2,new BotRichard(pioche));
            listeBot.set(3,new BotFocusMarchand(pioche));
            jouerPartie(listeBot.get(0), listeBot.get(1), listeBot.get(2), listeBot.get(3));

            vainqueur(listeBot);

            i++;
        }
        if(cptBot0 !=0 && cptBot1 !=0 && cptBot2 !=0 && cptBot3 !=0 ){
            remplirListe(moyennePoints,moyennePoints.get(0)/nombre,moyennePoints.get(1)/nombre,moyennePoints.get(2)/nombre,moyennePoints.get(3)/nombre,moyennePoints.get(4)/cptNull);
            remplirListe(moyennePointsVictoire,moyennePointsVictoire.get(0)/cptBot0,moyennePointsVictoire.get(1)/cptBot1,moyennePointsVictoire.get(2)/cptBot2,moyennePointsVictoire.get(3)/cptBot3,moyennePointsVictoire.get(4)/cptNull);
            remplirListe(pourcentageVictoire,((float)cptBot0/nombre)*100,((float)cptBot1/nombre)*100,((float)cptBot2/nombre)*100,((float)cptBot3/nombre)*100,((float)cptNull/nombre)*100);
        } else {
            throw new IllegalArgumentException("division par 0");
        }
        if(!csv){
            System.out.println("Comparaison des différents bots");
            System.out.println( "Taux de victoire : "+listeBot.get(0).toString().split("_")[0]+": "+ pourcentageVictoire.get(0) +"% ,"+listeBot.get(1).toString().split("_")[0]+": "+ pourcentageVictoire.get(1) +"% ,"+listeBot.get(2).toString().split("_")[0]+" :"+ pourcentageVictoire.get(2) +"% ,"+listeBot.get(3).toString().split("_")[0]+" :"+ pourcentageVictoire.get(3) +"%");
            System.out.println( "Taux de défaite : "+listeBot.get(0).toString().split("_")[0]+": "+ (100-pourcentageVictoire.get(0)) +"% ,"+listeBot.get(1).toString().split("_")[0]+": "+ (100-pourcentageVictoire.get(1)) +"% ,"+listeBot.get(2).toString().split("_")[0]+" :"+ (100-pourcentageVictoire.get(2)) +"% ,"+listeBot.get(3).toString().split("_")[0]+" :"+ (100-pourcentageVictoire.get(3)) +"%");
            System.out.println( "Taux d'égalité : "+pourcentageVictoire.get(4)+"%");
            System.out.println( "Score moyen : "+listeBot.get(0).toString().split("_")[0]+": "+ moyennePoints.get(0) +" ,"+listeBot.get(1).toString().split("_")[0]+": "+ moyennePoints.get(1) +" ,"+listeBot.get(2).toString().split("_")[0]+" :"+ moyennePoints.get(2) +" ,"+listeBot.get(3).toString().split("_")[0]+" :"+ moyennePoints.get(3) +" ,Egalité : "+moyennePoints.get(4));
        }
    }

    public static void vainqueur(List<Bot> listeBot){
        Bot vainqueur = tour.getLeVainqueur();
        remplirListe(moyennePoints,moyennePoints.get(0)+ listeBot.get(0).getScore(),moyennePoints.get(1)+ listeBot.get(1).getScore(),moyennePoints.get(2)+ listeBot.get(2).getScore(),moyennePoints.get(3)+ listeBot.get(3).getScore(),(float) 0);

        if(vainqueur==null){
            cptNull++;
            int max = 0;
            for (Bot bot : new ArrayList<Bot>(Arrays.asList(listeBot.get(0), listeBot.get(1), listeBot.get(2), listeBot.get(3)))){
                if(bot.getScore()>max){
                    max=bot.getScore();
                }
            }
            moyennePointsVictoire.set(4,moyennePointsVictoire.get(4)+max);
            moyennePoints.set(4,moyennePoints.get(4)+max);
        }
        else if(vainqueur==listeBot.get(0)){
            cptBot0++;
            moyennePointsVictoire.set(0,moyennePointsVictoire.get(0)+listeBot.get(0).getScore());
        }
        else if(vainqueur==listeBot.get(1)){
            cptBot1++;
            moyennePointsVictoire.set(1,moyennePointsVictoire.get(1)+listeBot.get(1).getScore());
        }
        else if(vainqueur==listeBot.get(2)){
            cptBot2++;
            moyennePointsVictoire.set(2,moyennePointsVictoire.get(2)+listeBot.get(2).getScore());
        }
        else if(vainqueur==listeBot.get(3)){
            cptBot3++;
            moyennePointsVictoire.set(3,moyennePointsVictoire.get(3)+listeBot.get(3).getScore());
        }
    }


    /**
     * sert pour les simulations de jeux entre les botFocusRoi
     * @param nombre nombre de parties
     */
    public static void simulation2(int nombre){
        int i=1;
        ArrayList<Float> cptPoints = new ArrayList<>(Arrays.asList((float)0,(float)0,(float)0,(float)0,(float)0));
        ArrayList<Float> cptVictoire = new ArrayList<>(Arrays.asList((float)0,(float)0,(float)0,(float)0,(float)0));

        while(i<=nombre){
            Pioche pioche = new Pioche();
            listeBot.set(0,new BotFocusRoi(pioche));
            listeBot.set(1,new BotFocusRoi(pioche));
            listeBot.set(2,new BotFocusRoi(pioche));
            listeBot.set(3,new BotFocusRoi(pioche));
            //On donne l'ordre dans lequel ils jouent 1->2->3->4->1...
            jouerPartie(listeBot.get(0),listeBot.get(1),listeBot.get(2),listeBot.get(3));
            Bot vainqueur=tour.getLeVainqueur();
            remplirListe(cptPoints,cptPoints.get(0)+ listeBot.get(0).getScore(),cptPoints.get(1)+ listeBot.get(1).getScore(),cptPoints.get(2)+ listeBot.get(2).getScore(),cptPoints.get(3)+ listeBot.get(3).getScore(),(float) 0);
            if (vainqueur!=null){
                if (vainqueur==listeBot.get(0))cptVictoire.set(0,cptVictoire.get(0)+1);
                if (vainqueur==listeBot.get(1))cptVictoire.set(1,cptVictoire.get(1)+1);
                if (vainqueur==listeBot.get(2))cptVictoire.set(2,cptVictoire.get(2)+1);
                if (vainqueur==listeBot.get(3))cptVictoire.set(3,cptVictoire.get(3)+1);
            }else{
                cptVictoire.set(4,cptVictoire.get(4)+1);
                int max = 0;
                for (Bot bot : new ArrayList<Bot>(Arrays.asList(listeBot.get(0), listeBot.get(1), listeBot.get(2), listeBot.get(3)))){
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
        System.out.println("Taux de victoire : "+listeBot.get(0).toString().split("_")[0]+" 1: "+pourcent1+"% ,"+listeBot.get(1).toString().split("_")[0]+" 2: "+pourcent2+"% ,"+listeBot.get(2).toString().split("_")[0]+" 3 :"+pourcent3+"% ,"+listeBot.get(3).toString().split("_")[0]+" 4 :"+pourcent4+"%");
        System.out.println("Taux de défaite : "+listeBot.get(0).toString().split("_")[0]+" 1: "+(100-pourcent1)+"% ,"+listeBot.get(1).toString().split("_")[0]+" 2: "+(100-pourcent2)+"% ,"+listeBot.get(2).toString().split("_")[0]+" 3: "+(100-pourcent3)+"% ,"+listeBot.get(3).toString().split("_")[0]+" 4: "+(100-pourcent4)+"%");
        System.out.println("Taux d'égalité: "+pourcentnull+"%");
        System.out.println("Score moyen : "+listeBot.get(0).toString().split("_")[0]+" 1: "+(cptPoints.get(0)/nombre)+" ,"+listeBot.get(1).toString().split("_")[0]+" 2: "+(cptPoints.get(1)/nombre)+" ,"+listeBot.get(2).toString().split("_")[0]+" 3: "+(cptPoints.get(2)/nombre)+" ,"+listeBot.get(3).toString().split("_")[0]+" 4: "+(cptPoints.get(3)/nombre)+" ,Egalité: "+(cptPoints.get(4)/cptVictoire.get(4)));
    }

    // j'ai mis en static parce que ça me faisait une erreur

    /**
     * pour lancer une partie entre 4 bots
     * @param bot1  Bot
     * @param bot2  Bot
     * @param bot3  Bot
     * @param bot4  Bot
     */
    public static void jouerPartie(Bot bot1, Bot bot2, Bot bot3, Bot bot4){
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
        String log = "LOGGER";
        boolean rien = true;
        if(arg.demo){
            //Faire une demo
            Logger.getLogger(log).getParent().setLevel(Level.ALL);
            rien = false;
            gameStart();
        }
        if(arg.two){
            //faire 2 fois 1000 stats
            Logger.getLogger(log).getParent().setLevel(Level.OFF);
            rien = false;
            simulation1(1000,false);
            simulation2(1000);
        }
        Path path = Paths.get("stats", "gamestats.csv");
        if(arg.csv){
            Logger.getLogger(log).getParent().setLevel(Level.OFF);
            rien = false;
            updateCSV(path.toFile(),1000);
        }
        if(arg.csvG > 0){
            Logger.getLogger(log).getParent().setLevel(Level.OFF);
            rien = false;
            updateCSV(path.toFile(), arg.csvG);
        }
        if(rien){
            gameStart();
        }
    }

    public static void gameStart(){
        Pioche pioche = new Pioche();
        Bot bot1 = new BotFocusRoi(pioche);
        Bot bot2 = new BotConstruitChere(pioche);
        Bot bot3 = new BotRichard(pioche);
        Bot bot4 = new BotFocusMarchand(pioche);

        //On donne l'ordre dans lequel ils jouent 1->2->3->4->1...
        jouerPartie(bot1,bot2,bot3,bot4);
    }

    /**
     *permet d'update le csv avec les resultats des simulations
     * @param file fichier à update
     */
    public static void updateCSV(File file,int nombre) {
        try(CSVReader reader = new CSVReader(new FileReader(file))) {
            //On lis d'abord les valeurs actuels
            List<String[]> allRows = reader.readAll();
            simulation1(nombre,true);
            if(!(allRows.get(1)[0].equals(listeBot.get(0).toString().split("_")[0])&& allRows.get(2)[0].equals(listeBot.get(1).toString().split("_")[0]) && allRows.get(3)[0].equals(listeBot.get(2).toString().split("_")[0]) && allRows.get(4)[0].equals(listeBot.get(3).toString().split("_")[0]))){
                //Si un des nom de bot change on remet les données à 0
                throw new IOException("changement fichier");
            }
            float total = Float.parseFloat(allRows.get(6)[1]);
            float pVBot0 = (Float.parseFloat(allRows.get(1)[1])*total+ pourcentageVictoire.get(0) *nombre)/(total+nombre);
            float pVBot1 = (Float.parseFloat(allRows.get(2)[1])*total+pourcentageVictoire.get(1)*nombre)/(total+nombre);
            float pVBot2 = (Float.parseFloat(allRows.get(3)[1])*total+pourcentageVictoire.get(2)*nombre)/(total+nombre);
            float pVBot3 = (Float.parseFloat(allRows.get(4)[1])*total+pourcentageVictoire.get(3)*nombre)/(total+nombre);
            float pVNullUpdate = (Float.parseFloat(allRows.get(5)[1])*total+pourcentageVictoire.get(4)*nombre)/(total+nombre);

            float ptBot0 = (Float.parseFloat(allRows.get(1)[3])*total+moyennePoints.get(0)*nombre)/(total+nombre);
            float ptBot1 = (Float.parseFloat(allRows.get(2)[3])*total+moyennePoints.get(1)*nombre)/(total+nombre);
            float ptBot2 = (Float.parseFloat(allRows.get(3)[3])*total+moyennePoints.get(2)*nombre)/(total+nombre);
            float ptBot3 = (Float.parseFloat(allRows.get(4)[3])*total+moyennePoints.get(3)*nombre)/(total+nombre);
            float ptNullUpdate = (Float.parseFloat(allRows.get(5)[3])*total+moyennePoints.get(4)*nombre)/(total+nombre);

            float ptVBot0 = (Float.parseFloat(allRows.get(1)[4])*total+moyennePointsVictoire.get(0)*nombre)/(total+nombre);
            float ptVBot1 = (Float.parseFloat(allRows.get(2)[4])*total+moyennePointsVictoire.get(1)*nombre)/(total+nombre);
            float ptVBot2 = (Float.parseFloat(allRows.get(3)[4])*total+moyennePointsVictoire.get(2)*nombre)/(total+nombre);
            float ptVBot3 = (Float.parseFloat(allRows.get(4)[4])*total+moyennePointsVictoire.get(3)*nombre)/(total+nombre);
            float ptVNullUpdate = (Float.parseFloat(allRows.get(5)[4])*total+moyennePointsVictoire.get(4)*nombre)/(total+nombre);


            total+=nombre;

            CSVWriter writer = new CSVWriter(new FileWriter(file, StandardCharsets.UTF_8));

            writer.writeNext(new String[]{"Simulations", "Pourcentage de victoire","Pourcentage de défaite","Score moyen", "Score moyen en cas de victoire"},false);
            writer.writeNext(new String[]{listeBot.get(0).toString().split("_")[0], pVBot0 +"",(100-pVBot0) +"",ptBot0+"",ptVBot0+""},false);
            writer.writeNext(new String[]{listeBot.get(1).toString().split("_")[0], pVBot1 +"",(100-pVBot1) +"",ptBot1+"",ptVBot1+""},false);
            writer.writeNext(new String[]{listeBot.get(2).toString().split("_")[0], pVBot2 +"",(100-pVBot2) +"",ptBot2+"",ptVBot2+""},false);
            writer.writeNext(new String[]{listeBot.get(3).toString().split("_")[0], pVBot3 +"",(100-pVBot3) +"",ptBot3+"",ptVBot3+""},false);
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
                writer.writeNext(new String[]{listeBot.get(0).toString().split("_")[0], pourcentageVictoire.get(0) +"",(100-pourcentageVictoire.get(0))+"",moyennePoints.get(0)+"",moyennePointsVictoire.get(0)+""},false);
                writer.writeNext(new String[]{listeBot.get(1).toString().split("_")[0], pourcentageVictoire.get(1) +"",(100-pourcentageVictoire.get(1))+"",moyennePoints.get(1)+"",moyennePointsVictoire.get(1)+""},false);
                writer.writeNext(new String[]{listeBot.get(2).toString().split("_")[0], pourcentageVictoire.get(2) +"",(100-pourcentageVictoire.get(2))+"",moyennePoints.get(2)+"",moyennePointsVictoire.get(2)+""},false);
                writer.writeNext(new String[]{listeBot.get(3).toString().split("_")[0], pourcentageVictoire.get(3) +"",(100-pourcentageVictoire.get(3))+"",moyennePoints.get(3)+"",moyennePointsVictoire.get(3)+""},false);
                writer.writeNext(new String[]{"Egalité", pourcentageVictoire.get(4) +"","--",moyennePoints.get(4)+"",moyennePointsVictoire.get(4)+""},false);
                writer.writeNext(new String[]{"Nombre de simulations",nombre+""},false);
                writer.close();
            } catch (Exception ex) {
                throw new IllegalArgumentException(ex);
            }
        } catch (CsvException e) {
            throw new IllegalArgumentException("probleme CSV");
        }
    }


    private static void remplirListe(ArrayList<Float> list, float construitChere, float richard, float aleatoire, float focusRoi, float nullable) {
        list.set(0,construitChere);
        list.set(1,richard);
        list.set(2,aleatoire);
        list.set(3,focusRoi);
        if(nullable != 0) list.set(4,nullable);
    }
}
