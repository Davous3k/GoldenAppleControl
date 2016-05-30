package net.terrocidepvp.goldenapplecontrol.handlers;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CoolDown {

    private long duration;
    private boolean useFormattedTime;
    private boolean useExpiredMsg;
    private List<String> consumeMsg;
    private List<String> coolDownMsg;
    private List<String> expiredMsg;

    private Map<UUID, Long> cooldowns = new ConcurrentHashMap<>(8, 0.9f, 1);

    public CoolDown(long duration,
                    boolean useFormattedTime,
                    boolean useExpiredMsg,
                    List<String> consumeMsg,
                    List<String> coolDownMsg,
                    List<String> expiredMsg) {
        this.duration = duration;
        this.useFormattedTime = useFormattedTime;
        this.useExpiredMsg = useExpiredMsg;
        this.consumeMsg = consumeMsg;
        this.coolDownMsg = coolDownMsg;
        this.expiredMsg = expiredMsg;
    }

    public long getDuration() {
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

    public Map<UUID, Long> getCooldowns() {
        return cooldowns;
    }
}
