package fr.cotedazur.univ.polytech.startingpoint.moteurjeu;

import fr.cotedazur.univ.polytech.startingpoint.moteurjeu.bots.Bot;

public class Affichage {
    // classe de gestion de tout les prints
    private Bot bot1;
    private int nbTour;
    public Affichage(Bot bot, int nbTour){
        this.bot1=bot;
        this.nbTour=nbTour;
    }
    public void afficheBot(){
        System.out.println("Tour "+nbTour);
        System.out.println("Role du BOT aléatoire : "+bot1.getRole());
        System.out.println("Quartier dans le main du BOT aléatoire: "+bot1.getQuartierMain());
        System.out.println("Quartier construit du BOT aléatoire: "+bot1.getQuartiersConstruits());
        System.out.println("Nombre d'or du BOT aléatoire: "+bot1.getOr());
        System.out.println("Points de victore du BOT aléatoire: "+bot1.getScore()+"\n");
    }
    public void testNarration(){
        //if(bot1.faireActionDeBase());
    }
}
