package net.terrocidepvp.goldenapplecontrol.handlers;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CoolDown {

    private final double duration;
    private final boolean useFormattedTime;
    private final boolean useExpiredMsg;
    private final List<String> consumeMsg;
    private final List<String> coolDownMsg;
    private final List<String> expiredMsg;

    private final ConcurrentHashMap<UUID, Long> coolDown = new ConcurrentHashMap<>(8, 0.9f, 1);

    public CoolDown(final double duration,
                    final boolean useFormattedTime,
                    final boolean useExpiredMsg,
                    final List<String> consumeMsg,
                    final List<String> coolDownMsg,
                    final List<String> expiredMsg) {
        this.duration = duration;
        this.useFormattedTime = useFormattedTime;
        this.useExpiredMsg = useExpiredMsg;
        this.consumeMsg = consumeMsg;
        this.coolDownMsg = coolDownMsg;
        this.expiredMsg = expiredMsg;
    }

    public double getDuration() {
        return duration;
    }

    public boolean isUseFormattedTime() {
        return useFormattedTime;
    }

    public boolean isUseExpiredMsg() {
        return useExpiredMsg;
    }

    public List<String> getConsumeMsg() {
        return consumeMsg;
    }

    public List<String> getCooldownMsg() {
        return coolDownMsg;
    }

    public List<String> getExpiredMsg() {
        return expiredMsg;
    }

    public ConcurrentHashMap<UUID, Long> getCoolDown() {
        return coolDown;
    }
}
