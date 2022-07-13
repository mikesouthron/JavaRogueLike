package org.southy.rl.entity;

import org.southy.rl.ColorUtils;

import java.awt.*;

public class EntityFactory {

    public final static Entity player = new Entity('@', Color.WHITE, "Player", true);
    public final static Entity orc = new Entity('o', ColorUtils.color(67, 127, 63), "Orc", true);
    public final static Entity troll = new Entity('T', ColorUtils.color(0, 127, 0), "Troll", true);

}
