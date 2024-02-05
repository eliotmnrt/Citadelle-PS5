package Citadelle.teamU.moteurjeu;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
public class CustomFormatter extends Formatter {
    public static final String ANSI_WHITE = "\033[0;37m";

    @Override
    public String format(LogRecord record) {
        return ANSI_WHITE+record.getMessage()+"\n";
    }
}
