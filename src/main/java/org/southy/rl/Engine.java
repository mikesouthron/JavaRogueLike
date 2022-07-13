package org.southy.rl;

import org.southy.rl.asciipanel.AsciiPanel;
import org.southy.rl.gen.Procgen;
import org.southy.rl.map.GameMap;
import org.southy.rl.map.Tile;

import java.awt.event.KeyEvent;
import java.util.List;

public class Engine {

    List<Entity> entities;
    Entity player;

    Logger logger;

    GameMap gameMap;

    public Engine(List<Entity> entities, Entity player, GameMap gameMap, Logger logger) {
        this.entities = entities;
        this.player = player;
        this.logger = logger;
        this.gameMap = gameMap;
        updateFov();
    }

    public void handleEvent(KeyEvent event) {
        var option = EventHandler.keyDown(event, logger);
        option.ifPresent(action -> action.perform(this, player));
        updateFov();
    }

    public void updateFov() {
        gameMap.computeFov(player.x, player.y);
        for (int i = 0; i < gameMap.visible.length; i++) {
            if (gameMap.visible[i] != null) {
                gameMap.explored[i] = gameMap.visible[i];
            }
        }
    }

    public void render(AsciiPanel panel) {
        panel.clear();
        gameMap.render(panel);
        entities.stream().filter(e -> gameMap.visible[Procgen.toIdx(e.x, e.y, gameMap.width)] != null).forEach(e -> panel.write(e.str, e.x, e.y, e.fg, e.bg));
        panel.repaint();
    }
}
