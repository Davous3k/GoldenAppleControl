package net.terrocidepvp.goldenapplecontrol.utils;

import java.util.Map;
import java.util.UUID;

public class CooldownUtil {

    public static double getCooldown(Map<UUID, Long> cooldowns, UUID playerUuid) {
        if (!cooldowns.containsKey(playerUuid))
            return 0.0d;
        double duration = cooldowns.get(playerUuid);
        duration -= System.currentTimeMillis();
        if (duration <= 0L) {
            cooldowns.remove(playerUuid);
            return 0.0d;
        }
        duration /= 1000d;
        return Math.round(duration * 10) / 10.0d;
    }

    public static void setCooldown(Map<UUID, Long> cooldowns, UUID playerUuid, long duration) {
        if (cooldowns.containsKey(playerUuid)) {
            cooldowns.remove(playerUuid);
        }
        long durationSeconds = System.currentTimeMillis();
        durationSeconds += duration * 1000;
        cooldowns.put(playerUuid, durationSeconds);
    }

}
