package net.terrocidepvp.goldenapplecontrol.handlers;

import java.util.List;

public class ConsumptionControl {

    private final int foodLevel;
    private final double saturation;
    private final List<String> effects;

    public ConsumptionControl(final int foodLevel,
                              final double saturation,
                              final List<String> effects) {
        this.foodLevel = foodLevel;
        this.saturation = saturation;
        this.effects = effects;
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
