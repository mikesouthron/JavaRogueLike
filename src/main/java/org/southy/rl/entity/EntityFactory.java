package org.southy.rl.entity;

import org.southy.rl.ColorUtils;

import java.awt.*;

public class EntityFactory {

    public final static Entity player = new Entity(null, '@', Color.WHITE, "Player", true);
    public final static Entity orc = new Entity(null, 'o', ColorUtils.color(67, 127, 63), "Orc", true);
    public final static Entity troll = new Entity(null, 'T', ColorUtils.color(0, 127, 0), "Troll", true);

}
