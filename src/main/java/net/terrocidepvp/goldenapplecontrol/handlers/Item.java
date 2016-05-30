package net.terrocidepvp.goldenapplecontrol.handlers;

import org.bukkit.Material;

public class Item {

    private Material material;
    private int data;
    private String permissionNode;
    private CoolDown coolDown;
    private ConsumptionControl consumptionControl;

    public Item(Material material,
                int data,
                String permissionNode,
                CoolDown coolDown,
                ConsumptionControl consumptionControl) {
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
