package org.southy.rl.components;

import org.southy.rl.entity.Actor;

import java.io.Serializable;

public class Equipment implements Serializable {

    Actor parent;

    //head, neck, body, back?, right hand, left hand, right finger, left finger, feet
    public Equipable[] items = new Equipable[9];

    public Equipment(Actor parent) {
        this.parent = parent;
    }

    public Range getMeleeAtkRange() {
        int str = parent.fighter.strength;

        Range baseEquipmentDamage = new Range(0, 1);
        double equipmentStrMod = 0;

        for (Equipable item : items) {
            if (item == null) {
                continue;
            }
            if (item.atk != null) {
                baseEquipmentDamage.low += item.atk.low;
                baseEquipmentDamage.high += item.atk.high;
            }

            equipmentStrMod += item.strMod;
        }

        int dmgMod = (int)(equipmentStrMod + str);
        return new Range(baseEquipmentDamage.low * dmgMod, baseEquipmentDamage.high * dmgMod);
    }

    public Range getMeleeAtkRange(Equipable remove, Equipable add) {
        int str = parent.fighter.strength;

        Range baseEquipmentDamage = new Range(0, 1);
        double equipmentStrMod = 0;

        for (Equipable item : items) {
            if (item == null) {
                continue;
            }
            if (item == remove) {
                if (add.atk != null) {
                    baseEquipmentDamage.low += add.atk.low;
                    baseEquipmentDamage.high += add.atk.high;
                }
                equipmentStrMod += add.strMod;
            } else {
                if (item.atk != null) {
                    baseEquipmentDamage.low += item.atk.low;
                    baseEquipmentDamage.high += item.atk.high;
                }
                equipmentStrMod += item.strMod;
            }
        }

        if (remove == null) {
            if (add.atk != null) {
                baseEquipmentDamage.low += add.atk.low;
                baseEquipmentDamage.high += add.atk.high;
            }
            equipmentStrMod += add.strMod;
        }

        int dmgMod = (int)(equipmentStrMod + str);
        return new Range(baseEquipmentDamage.low * dmgMod, baseEquipmentDamage.high * dmgMod);
    }

    public int calculateMeleeDamage() {
        return getMeleeAtkRange().random();
    }

    public Range getMeleeDefenseRange() {
        Range baseEquipmentArmour = new Range(1, 2);
        double equipmentArmourMod = 1;

        for (Equipable item : items) {
            if (item == null) {
                continue;
            }
            if (item.def != null) {
                baseEquipmentArmour.low += item.def.low;
                baseEquipmentArmour.high += item.def.high;
            }

            equipmentArmourMod += item.armourMod;
        }

        return new Range((int)(baseEquipmentArmour.low * equipmentArmourMod), (int)(baseEquipmentArmour.high * equipmentArmourMod));
    }

    public Range getMeleeDefenseRange(Equipable remove, Equipable add) {
        Range baseEquipmentArmour = new Range(1, 2);
        double equipmentArmourMod = 1;

        for (Equipable item : items) {
            if (item == null) {
                continue;
            }
            if (item == remove) {
                if (add.def != null) {
                    baseEquipmentArmour.low += add.def.low;
                    baseEquipmentArmour.high += add.def.high;
                }

                equipmentArmourMod += add.armourMod;
            } else {
                if (item.def != null) {
                    baseEquipmentArmour.low += item.def.low;
                    baseEquipmentArmour.high += item.def.high;
                }

                equipmentArmourMod += item.armourMod;
            }

        }

        if (remove == null) {
            if (add.def != null) {
                baseEquipmentArmour.low += add.def.low;
                baseEquipmentArmour.high += add.def.high;
            }

            equipmentArmourMod += add.armourMod;
        }

        return new Range((int)(baseEquipmentArmour.low * equipmentArmourMod), (int)(baseEquipmentArmour.high * equipmentArmourMod));
    }

    public int calculateMeleeDefense() {
        return getMeleeDefenseRange().random();
    }

    public int calculateHpRestore() {
        int hpRestore = 0;

        for (Equipable item : items) {
            if (item == null) {
                continue;
            }
            hpRestore += item.hpRestore;
        }

        return hpRestore;
    }

    public int calculateInCombatHpRestore() {
        int hpRestore = 0;

        for (Equipable item : items) {
            if (item == null) {
                continue;
            }
            hpRestore += item.inCombatHpRestore;
        }

        return hpRestore;
    }

    public void setParent(Actor parent) {
        this.parent = parent;
    }

    public void setItems(Equipable[] items) {
        this.items = items;
    }
}
