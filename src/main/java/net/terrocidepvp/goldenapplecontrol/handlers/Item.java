package net.terrocidepvp.goldenapplecontrol.handlers;

import org.bukkit.Material;

public class Item {

    private final Material material;
    private final int data;
    private final String permissionNode;
    private final CoolDown coolDown;
    private final ConsumptionControl consumptionControl;

    public Item(final Material material,
                final int data,
                final String permissionNode,
                final CoolDown coolDown,
                final ConsumptionControl consumptionControl) {
        this.material = material;
        this.data = data;
        this.permissionNode = permissionNode;
        this.coolDown = coolDown;
        this.consumptionControl = consumptionControl;
    }

    public Material getMaterial() {
        return material;
    }

    public int getData() {
        return data;
    }

    public String getPermissionNode() {
        return permissionNode;
    }

    public CoolDown getCoolDown() {
        return coolDown;
    }

    public ConsumptionControl getConsumptionControl() {
        return consumptionControl;
    }
}
