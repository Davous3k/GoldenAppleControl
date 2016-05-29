package net.terrocidepvp.goldenapplecontrol.handlers;

import org.bukkit.Material;

public class Item {

    private final Material material;
    private final int data;
    private final CoolDown coolDown;
    private final ConsumptionControl consumptionControl;

    public Item(final Material material,
                final int data,
                final CoolDown coolDown,
                final ConsumptionControl consumptionControl) {
        this.material = material;
        this.data = data;
        this.coolDown = coolDown;
        this.consumptionControl = consumptionControl;
    }

    public Material getMaterial() {
        return material;
    }

    public int getData() {
        return data;
    }

    public CoolDown getCoolDown() {
        return coolDown;
    }

    public ConsumptionControl getConsumptionControl() {
        return consumptionControl;
    }
}
