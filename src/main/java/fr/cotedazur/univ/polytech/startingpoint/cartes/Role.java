package fr.cotedazur.univ.polytech.startingpoint.cartes;
import fr.cotedazur.univ.polytech.startingpoint.moteurjeu.Pioche;
import fr.cotedazur.univ.polytech.startingpoint.moteurjeu.bots.Bot;
import fr.cotedazur.univ.polytech.startingpoint.moteurjeu.bots.BotAleatoire;

import java.util.*;
public class Role {
    protected Bot bot;
    public <T extends  Bot> Role(T bot){
        this.bot=bot;
    }



}
