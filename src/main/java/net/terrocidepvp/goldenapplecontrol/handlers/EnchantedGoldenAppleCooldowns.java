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

    public Double getEnchantedGoldenAppleCD(final UUID playerUuid) {
        if (!cdHashMap.containsKey(playerUuid))
            return 0.0D;
        double enchantedGoldenAppleCd = cdHashMap.get(playerUuid);
        enchantedGoldenAppleCd -= System.currentTimeMillis();
        if (enchantedGoldenAppleCd <= 0L) {
            cdHashMap.remove(playerUuid);
            return 0.0D;
        }
        enchantedGoldenAppleCd /= 1000D;
        enchantedGoldenAppleCd = Math.round(enchantedGoldenAppleCd * 10) / 10.0d;

        return enchantedGoldenAppleCd;
    }
    public void setEnchantedGoldenAppleCD(final UUID playerUuid, final long enchantedGoldenAppleDuration) {
        if (cdHashMap.containsKey(playerUuid)) {
            cdHashMap.remove(playerUuid);
        }
        long enchantedGoldenAppleCd = System.currentTimeMillis();
        enchantedGoldenAppleCd += enchantedGoldenAppleDuration * 1000;
        cdHashMap.put(playerUuid, enchantedGoldenAppleCd);
    }
}
