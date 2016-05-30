package net.terrocidepvp.goldenapplecontrol.handlers;

import java.util.List;

public class ConsumptionControl {

    private final int foodLevel;
    private final float saturation;
    private final List<String> effects;

    public ConsumptionControl(final int foodLevel,
                              final float saturation,
                              final List<String> effects) {
        this.foodLevel = foodLevel;
        this.saturation = saturation;
        this.effects = effects;
    }

    public int getFoodLevel() {
        return foodLevel;
    }

    public float getSaturation() {
        return saturation;
    }

    public List<String> getEffects() {
        return effects;
    }
}
