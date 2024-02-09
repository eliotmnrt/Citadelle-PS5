package Citadelle.teamU.moteurJeu;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
public class CustomFormatter extends Formatter {
    public static final String ANSI_WHITE = "\033[0;37m";

    @Override
    public String format(LogRecord logRecord) {
        return ANSI_WHITE+logRecord.getMessage()+"\n";
    }
}
