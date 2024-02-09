package Citadelle.teamU.cartes;

public enum TypeQuartier {
    JAUNE("noble"),BLEUE("religieux"),VERT("marchand"),ROUGE("militaire"),VIOLET("merveille");
    private final String type;
    TypeQuartier(String type){
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
