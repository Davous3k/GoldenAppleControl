package net.terrocidepvp.goldenapplecontrol.utils;

public class TimeUtil {

    public static String formatTime(double timeoutSeconds) {
        if (timeoutSeconds == 0.0d) {
            return ("0 seconds");
        }
        final long days = (long)timeoutSeconds / 86400;
        final long hours = (long)timeoutSeconds / 3600 % 24;
        final long minutes = (long)timeoutSeconds / 60 % 60;
        final double seconds = Math.round((timeoutSeconds % 60) * 10) / 10.0d;
        return (((days > 0.0d ? " " + days + " day" + (days != 1.0d ? "s" : "") : "") + (hours > 0.0d ? " " + hours + " hour" + (hours != 1.0d ? "s" : "") : "") + (minutes > 0.0d ? " " + minutes + " minute" + (minutes != 1.0d ? "s" : "") : "") + (seconds > 0 ? " " + seconds + " second" + (seconds != 1.0d ? "s" : "") : "")).replaceAll("^\\s+|\\s+$", ""));
    }

}
