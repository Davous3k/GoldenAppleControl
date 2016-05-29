package net.terrocidepvp.goldenapplecontrol.handlers;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EnchantedGoldenAppleCooldowns {
    // Allow other classes to use the methods.
    private static EnchantedGoldenAppleCooldowns instance = new EnchantedGoldenAppleCooldowns();

    public static EnchantedGoldenAppleCooldowns getHandler() {
        return instance;
    }

    // Set up the HashMap.
    private final ConcurrentHashMap<UUID, Long> cdHashMap = new ConcurrentHashMap<UUID, Long>(8, 0.9f, 1);

    // It's a double because the extra processing power that BigDecimal uses
    // isn't needed in this scenario.
    public Double getEnchantedGoldenAppleCD(final UUID playerUuid) {
        if (!cdHashMap.containsKey(playerUuid))
            return 0.0D;
        // Initialise the long EnchantedGoldenAppleCD.
        double enchantedGoldenAppleCd = cdHashMap.get(playerUuid);
        // Subtracts the current system time from EnchantedGoldenAppleCD to
        // return how much time is remaining.
        enchantedGoldenAppleCd -= System.currentTimeMillis();
        // Remove the cooldown if it has expired.
        if (enchantedGoldenAppleCd <= 0L) {
            cdHashMap.remove(playerUuid);
            return 0.0D;
        }
        // Convert the cooldown back into seconds.
        enchantedGoldenAppleCd /= 1000D;
        // Efficient method of rounding it to 2 decimal places, reducing clock
        // cycles compared to DecimalFormat.
        enchantedGoldenAppleCd = Math.round(enchantedGoldenAppleCd * 10) / 10.0d;

        return enchantedGoldenAppleCd;
    }

    // currentTimeMillis would work efficiently using a long.
    public void setEnchantedGoldenAppleCD(final UUID playerUuid, final long enchantedGoldenAppleDuration) {
        // If the key exists in the hashmap already, remove it. Note that this
        // is SET, not get or remove.
        if (cdHashMap.containsKey(playerUuid)) {
            cdHashMap.remove(playerUuid);
        }
        // EnchantedGoldenAppleCD is when the enchanted golden apple cooldown is
        // going to expire.
        long enchantedGoldenAppleCd = System.currentTimeMillis();
        // This adds the duration onto the enchanted golden apple cooldown.
        // It's multiplied by 1000 to convert it into milliseconds.
        enchantedGoldenAppleCd += enchantedGoldenAppleDuration * 1000;
        // Put the data in the hashmap.
        cdHashMap.put(playerUuid, enchantedGoldenAppleCd);
    }
}
