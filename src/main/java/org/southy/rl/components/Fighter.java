package org.southy.rl.components;

public class Fighter extends BaseComponent {

    int maxHp;
    int hp;
    int defence;
    int power;

    public Fighter(int maxHp, int defence, int power) {
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.defence = defence;
        this.power = power;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, Math.min(hp, maxHp));
    }

}
