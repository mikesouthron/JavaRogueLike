package org.southy.rl.entity;

import org.southy.rl.ColorUtils;
import org.southy.rl.components.Fighter;
import org.southy.rl.components.HostileEnemy;

import java.awt.*;

public class EntityFactory {

    public final static Actor player = new Actor(null, '@', Color.WHITE, "Player", new Fighter(30, 2, 5), HostileEnemy.class);
    public final static Actor orc = new Actor(null, 'o', ColorUtils.color(67, 127, 63), "Orc", new Fighter(10, 0, 3), HostileEnemy.class );
    public final static Actor troll = new Actor(null, 'T', ColorUtils.color(0, 127, 0), "Troll", new Fighter(16, 1, 4), HostileEnemy.class);

}
