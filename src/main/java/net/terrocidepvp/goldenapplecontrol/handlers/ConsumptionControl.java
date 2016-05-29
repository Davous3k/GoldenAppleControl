package net.terrocidepvp.goldenapplecontrol.handlers;

import java.util.List;

public class ConsumptionControl {

    private final boolean enabled;
    private final int foodLevel;
    private final double saturation;
    private final List<String> effects;

    public ConsumptionControl(final boolean enabled,
                              final int foodLevel,
                              final double saturation,
                              final List<String> effects) {
        this.enabled = enabled;
        this.foodLevel = foodLevel;
        this.saturation = saturation;
        this.effects = effects;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getFoodLevel() {
        return foodLevel;
    }

    public double getSaturation() {
        return saturation;
    }

    public List<String> getEffects() {
        return effects;
    }
}
