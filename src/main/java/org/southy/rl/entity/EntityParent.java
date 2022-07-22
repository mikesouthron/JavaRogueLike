package org.southy.rl.entity;

import org.southy.rl.components.Inventory;
import org.southy.rl.exceptions.Impossible;
import org.southy.rl.map.GameMap;

import java.util.List;

public interface EntityParent {

    GameMap gamemap();

    default List<Entity> getEntities() throws Impossible {
        throw new Impossible("Parent has no entities");
    }

    default Inventory getInventory() throws Impossible {
        throw new Impossible("Parent has no inventory");
    }

}
