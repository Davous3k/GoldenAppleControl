package net.terrocidepvp.goldenapplecontrol.handlers;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GoldenAppleCooldowns {
    // Allow other classes to use the methods.
    private static GoldenAppleCooldowns instance = new GoldenAppleCooldowns();

    public static GoldenAppleCooldowns getHandler() {
        return instance;
    }

    // Set up the HashMap.
    private final ConcurrentHashMap<UUID, Long> cdHashMap = new ConcurrentHashMap<UUID, Long>(8, 0.9f, 1);

    // It's a double because the extra processing power that BigDecimal uses
    // isn't needed in this scenario.
    public Double getGoldenAppleCD(final UUID playerUuid) {
        if (!cdHashMap.containsKey(playerUuid))
            return 0.0D;
        // Initialise the long GoldenAppleCD.
        double goldenAppleCd = cdHashMap.get(playerUuid);
        // Subtracts the current system time from GoldenAppleCD to return how
        // much time is remaining.
        goldenAppleCd -= System.currentTimeMillis();
        // Remove the cooldown if it has expired.
        if (goldenAppleCd <= 0L) {
            cdHashMap.remove(playerUuid);
            return 0.0D;
        }
        // Convert the cooldown back into seconds.
        goldenAppleCd /= 1000D;
        // Efficient method of rounding it to 2 decimal places, reducing clock
        // cycles compared to DecimalFormat.
        goldenAppleCd = Math.round(goldenAppleCd * 10) / 10.0d;
        return goldenAppleCd;
    }

    // currentTimeMillis would work efficiently using a long.
    public void setGoldenAppleCD(final UUID playerUuid, final long goldenAppleDuration) {
        // If the key exists in the HashMap already, remove it. Note that this
        // is SET, not get or remove.
        if (cdHashMap.containsKey(playerUuid)) {
            cdHashMap.remove(playerUuid);
        }
        // GoldenAppleCD is when the golden apple cooldown is going to expire.
        long goldenAppleCd = System.currentTimeMillis();
        // This adds the duration onto the golden apple cooldown.
        // It's multiplied by 1000 to convert it into milliseconds.
        goldenAppleCd += goldenAppleDuration * 1000;
        // Put the data in the HashMap.
        cdHashMap.put(playerUuid, goldenAppleCd);
    }
}
