package Citadelle.teamU.cartes.roles;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;

public interface Role {


    //Les actions dépendent du role
    void actionSpecial(Bot bot);
}
