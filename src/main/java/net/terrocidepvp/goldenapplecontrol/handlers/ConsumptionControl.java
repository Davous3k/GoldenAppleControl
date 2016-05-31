package net.terrocidepvp.goldenapplecontrol.handlers;

import java.util.List;

public class ConsumptionControl {

    private int foodLevel;
    private float saturation;
    private List<String> effects;

    ConsumptionControl(int foodLevel,
                       float saturation,
                       List<String> effects) {
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
