package net.terrocidepvp.goldenapplecontrol.handlers;

import org.bukkit.Material;

public class Item {

    private Material material;
    private int data;
    private String placeholder;
    private String permissionNode;
    private CoolDown coolDown;
    private ConsumptionControl consumptionControl;

    Item(Material material,
         int data,
         String placeholder,
         String permissionNode,
         CoolDown coolDown,
         ConsumptionControl consumptionControl) {
        this.material = material;
        this.data = data;
        this.placeholder = placeholder;
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

    public String getPlaceholder() {
        return placeholder;
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
