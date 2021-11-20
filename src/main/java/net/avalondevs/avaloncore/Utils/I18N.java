package net.avalondevs.avaloncore.Utils;

import lombok.Getter;
import net.avalondevs.avaloncore.Main;
import org.bukkit.configuration.Configuration;

import java.io.File;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class I18N {

    @Getter
    public static I18N instance;
    private final Configuration defaultBundle;
    private final DateFormat dateFormat;
    private final transient Map<String, MessageFormat> messageFormatCache = new HashMap<>();

    public I18N() {

        defaultBundle = ConfigUtil.ensureConfig(new File(Main.getInstance().getDataFolder(), "messages.yml"));
        instance = this;
        dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

    }

    public String concat(List<String> list) {

        StringBuilder builder = new StringBuilder();

        for (String s : list) {
            builder.append(s).append("\n");
        }

        return builder.toString();

    }

    /**
     * Will retrieve a message from the current bundle.
     *
     * @param string the message to be found or and empty string
     * @return
     */
    public String translate(final String string) {
        if (!defaultBundle.contains(string))
            return "";

        if (defaultBundle.isList(string))
            return concat(defaultBundle.getStringList(string));

        return defaultBundle.getString(string);
    }

    /**
     * Convert a message using the MessageFormat standard
     *
     * @param string the input
     * @param args   the arguments
     * @return the formatted message
     */
    public String format(String string, Object... args) {

        String format = translate(string);
        MessageFormat messageFormat = messageFormatCache.get(format);
        if (messageFormat == null) {
            try {
                messageFormat = new MessageFormat(format);
            } catch (final IllegalArgumentException e) {
                format = format.replaceAll("\\{(\\D*?)\\}", "\\[$1\\]");
                messageFormat = new MessageFormat(format);
            }
            messageFormatCache.put(format, messageFormat);
        }
        return Color.fmt(messageFormat.format(args));

    }

    public String formatDate(Date date) {

        return dateFormat.format(date);

    }

    public String formatPeriod(long period) {


        int seconds = (int) (period / 1000);

        String day = "";
        String hour = "";
        String min = "";
        String sec = "";

        int days = seconds / (60 * 60 * 24);
        if (days == 1) {
            day = "1 day";
        } else if (days > 1) {
            day = days + " days";
        }
        seconds -= days * (60 * 60 * 24);

        int hours = seconds / (60 * 60);
        if (hours == 1) {
            hour = "1 hour";
        } else if (hours > 1) {
            hour = hours + " hours";
        }
        seconds -= hours * (60 * 60);

        int minutes = seconds / 60;
        if (minutes == 1) {
            min = "1 minute";
        } else if (minutes > 1) {
            min = minutes + " minutes";
        }
        seconds -= minutes * 60;

        if (seconds == 1) {
            sec = "1 second";
        } else if (seconds > 1) {
            sec = seconds + " seconds";
        }
        String fin = day + " " + hour + " " + min + " " + sec;

        if (hour.equals("")) {
            fin = day + " " + min + " " + sec;
            if (min.equals("")) {
                fin = day + " " + sec;
            }
        } else if (min.equals("")) {
            fin = day + " " + hour + " " + sec;
        }

        return fin.trim();

    }

}
