package Citadelle.teamU.moteurJeu;
import com.beust.jcommander.Parameter;

public class Args {
    @Parameter(names = "--demo")
    public boolean demo = false;
    @Parameter(names = "--2thousands")
    public boolean two = false;

    @Parameter(names = "--csv")
    public boolean csv = false;
}
