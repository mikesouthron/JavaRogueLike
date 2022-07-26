package org.southy.rl.components;

public enum BodyPart {

    HEAD(']'), NECK('"'), BODY('['), BACK('('), HAND('/'), OFF_HAND(')'), FINGER('='), FEET(')');

    public char symbol;

    BodyPart(char symbol) {
        this.symbol = symbol;
    }
}
